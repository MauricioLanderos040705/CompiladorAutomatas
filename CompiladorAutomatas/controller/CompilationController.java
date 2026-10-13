package controller;

import model.compiler.CompilationResult;
import model.compiler.Compiler;
import model.errors.CompilerError;
import model.lexer.Token;
import model.symboltable.Symbol;
import view.ErrorPanel;
import view.MainWindow;
import view.TokenTablePanel;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Coordina las acciones de compilación: toma el código del editor,
 * lo envía al Compiler y muestra los resultados en la ventana.
 */
public class CompilationController {

    private static final File SYMBOL_FILE = new File("data", "Tabla de símbolos.dat");

    private final MainWindow window;
    private final Compiler compiler;

    public CompilationController(MainWindow window) {
        this.window = window;
        this.compiler = new Compiler(SYMBOL_FILE);
    }

    /** Al iniciar: crea el archivo y muestra las palabras reservadas en la tabla. */
    public void initialize() {
        try {
            compiler.initialize();
            showSymbols(compiler.getSymbols());
        } catch (IOException | IllegalArgumentException e) {
            window.showErrorMessage(
                    "No se pudo crear la Tabla de símbolos:\n" + e.getMessage());
        }
    }

    /** Acción del botón Compilar. */
    public void compile() {
        try {
            CompilationResult result = compiler.compile(window.getCodeEditor().getSourceCode());
            showSymbols(result.getSymbols());
            showTokens(result.getTokens());
            showErrors(result);
            window.getTokenTablePanel().showTokensTab();
        } catch (IOException | IllegalArgumentException e) {
            window.showErrorMessage(
                    "No se pudo completar la compilación:\n" + e.getMessage());
        }
    }

    private void showSymbols(List<Symbol> symbols) {
        TokenTablePanel panel = window.getTokenTablePanel();
        panel.clearRows();
        for (Symbol s : symbols) {
            panel.addRow(s.getId(), s.getLexeme(), s.getCategory().getLabel());
        }
    }

    private void showTokens(List<Token> tokens) {
        TokenTablePanel panel = window.getTokenTablePanel();
        panel.clearTokens();
        int number = 1;
        for (Token t : tokens) {
            panel.addToken(number++, t.getLexeme(), t.getType().getLabel(),
                    t.getLine(), t.getColumn());
        }
    }

    private void showErrors(CompilationResult result) {
        ErrorPanel panel = window.getErrorPanel();
        panel.clearErrors();
        int number = 1;
        for (CompilerError e : result.getErrors()) {
            panel.addError(number++, e.getType().getLabel(), e.getDescription(),
                    e.getLine(), e.getColumn());
        }
        panel.setSummary(result.getErrorSummary(), result.hasErrors());
    }
}