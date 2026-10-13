package model.compiler;

import model.automata.AutomatonIdentifier;
import model.automata.AutomatonNumber;
import model.automata.AutomatonResult;
import model.errors.ErrorManager;
import model.errors.ErrorType;
import model.keywords.ReservedWords;
import model.lexer.Lexer;
import model.lexer.Token;
import model.lexer.TokenType;
import model.symboltable.Symbol;
import model.symboltable.SymbolTable;
import model.symboltable.SymbolTableFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordina las fases de la compilación. No implementa la lógica de cada fase:
 * usa el Lexer, los AFD, la tabla de símbolos y el administrador de errores.
 * No conoce la interfaz gráfica.
 */
public class Compiler {

    private final File symbolFile;
    private final Lexer lexer = new Lexer();
    private final AutomatonIdentifier identifierAutomaton = new AutomatonIdentifier();
    private final AutomatonNumber numberAutomaton = new AutomatonNumber();
    private final SymbolTable symbolTable = new SymbolTable();
    private final ErrorManager errorManager = new ErrorManager();
    private SymbolTableFile symbolTableFile;

    public Compiler(File symbolFile) {
        this.symbolFile = symbolFile;
    }

    /** Deja la tabla y el archivo solo con las palabras reservadas (al iniciar). */
    public void initialize() throws IOException {
        resetSymbolTable();
    }

    /** Copia de los símbolos actuales, en orden de incorporación. */
    public List<Symbol> getSymbols() {
        return new ArrayList<>(symbolTable.getAll());
    }

    /**
     * Compila el código fuente.
     *
     * @throws IOException si falla la escritura del archivo (error interno, no del código)
     */
    public CompilationResult compile(String source) throws IOException {
        // Paso 5: crear/reiniciar el archivo "Tabla de símbolos"
        resetSymbolTable();
        errorManager.clear();

        // Paso 6: separar en tokens
        List<Token> tokens = lexer.tokenize(source);

        // Pasos 7 y 8 (identificadores y números) y 9 (errores), en orden de aparición
        for (Token t : tokens) {
            Symbol.Category category = null;

            if (t.getType() == TokenType.IDENTIFIER) {
                AutomatonResult result = identifierAutomaton.recognize(t.getLexeme());
                if (result.isAccepted()) {
                    category = Symbol.Category.IDENTIFIER;
                } else {
                    reportRejected(ErrorType.INVALID_IDENTIFIER, t, result);
                }
            } else if (t.getType() == TokenType.NUMBER) {
                AutomatonResult result = numberAutomaton.recognize(t.getLexeme());
                if (result.isAccepted()) {
                    category = AutomatonNumber.STATE_DECIMAL.equals(result.getFinalState())
                            ? Symbol.Category.DECIMAL : Symbol.Category.INTEGER;
                } else {
                    reportRejected(ErrorType.INVALID_NUMBER, t, result);
                }
            } else if (t.getType() == TokenType.UNKNOWN) {
                errorManager.report(ErrorType.UNKNOWN_SYMBOL,
                        "Símbolo no reconocido: '" + t.getLexeme() + "'",
                        t.getLine(), t.getColumn());
            }

            if (category == null) {
                continue;
            }
            Symbol added = symbolTable.add(t.getLexeme(), category);
            if (added != null) {
                symbolTableFile.append(added);
            }
        }

        return new CompilationResult(tokens, getSymbols(),
                new ArrayList<>(errorManager.getAll()), errorManager.getSummary());
    }

    /** Vacía la tabla, la vuelve a llenar con las reservadas y reescribe el archivo. */
    private void resetSymbolTable() throws IOException {
        symbolTable.clear();
        for (String word : ReservedWords.getAll()) {
            symbolTable.add(word, Symbol.Category.RESERVED_WORD);
        }
        symbolTableFile = new SymbolTableFile(symbolFile);
        symbolTableFile.reset();
        for (Symbol s : symbolTable.getAll()) {
            symbolTableFile.append(s);
        }
    }

    /** Reporta un token rechazado por un AFD, apuntando al carácter donde falló. */
    private void reportRejected(ErrorType type, Token t, AutomatonResult result) {
        errorManager.report(type,
                "'" + t.getLexeme() + "': " + result.getMessage(),
                t.getLine(), t.getColumn() + result.getErrorIndex());
    }
}