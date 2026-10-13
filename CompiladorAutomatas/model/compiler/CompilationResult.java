package model.compiler;

import model.errors.CompilerError;
import model.lexer.Token;
import model.symboltable.Symbol;

import java.util.Collections;
import java.util.List;

/** Resultado de una compilación: tokens, tabla de símbolos y errores detectados. */
public class CompilationResult {

    private final List<Token> tokens;
    private final List<Symbol> symbols;
    private final List<CompilerError> errors;
    private final String errorSummary;

    public CompilationResult(List<Token> tokens, List<Symbol> symbols,
                             List<CompilerError> errors, String errorSummary) {
        this.tokens = Collections.unmodifiableList(tokens);
        this.symbols = Collections.unmodifiableList(symbols);
        this.errors = Collections.unmodifiableList(errors);
        this.errorSummary = errorSummary;
    }

    public List<Token> getTokens() { return tokens; }
    public List<Symbol> getSymbols() { return symbols; }
    public List<CompilerError> getErrors() { return errors; }
    public String getErrorSummary() { return errorSummary; }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}