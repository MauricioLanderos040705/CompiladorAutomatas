package controller;

import java.io.File;
import java.io.IOException;
import model.keywords.KeywordHighlighter;
import view.MainWindow;

/**
 * Coordinación general de la interfaz.
 * Crea la ventana, activa el coloreado, atiende el menú y el botón,
 * y delega la compilación en CompilationController.
 */
public class MainController {

    private final MainWindow window;
    private final FileController fileController;
    private final CompilationController compilationController;
    private final KeywordHighlighter highlighter;

    public MainController() {
        this.window = new MainWindow();
        this.fileController = new FileController();
        this.highlighter = new KeywordHighlighter(window.getCodeEditor().getTextPane());
        this.compilationController = new CompilationController(window);

        compilationController.initialize();
        connectActions();
    }

    /** Muestra la ventana principal. */
    public void start() {
        window.setVisible(true);
    }

    /** Devuelve el código fuente escrito actualmente en el editor. */
    public String getSourceCode() {
        return window.getCodeEditor().getSourceCode();
    }

    /** Conecta el menú y el botón con sus acciones. */
    private void connectActions() {
        window.getAppMenuBar().setOnOpen(this::openFile);
        window.getAppMenuBar().setOnExit(() -> System.exit(0));
        window.getAppMenuBar().setOnCompile(compilationController::compile);
        window.setOnCompile(compilationController::compile);
    }

    /** Pide un archivo al usuario, lo lee y lo carga en el editor. */
    private void openFile() {
        File file = window.chooseFileToOpen();
        if (file == null) {
            return; // el usuario canceló
        }
        try {
            String content = fileController.readTextFile(file);
            window.getCodeEditor().setSourceCode(content);
            window.setTitle("Compilador - Autómatas - " + file.getName());
        } catch (IOException e) {
            window.showErrorMessage("No se pudo leer el archivo:\n" + e.getMessage());
        }
    }
}