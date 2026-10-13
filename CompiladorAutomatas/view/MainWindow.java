package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Ventana principal de la aplicación.
 * Ensambla los componentes visuales; no contiene lógica del compilador.
 */
public class MainWindow extends JFrame {

    private final CodeEditor codeEditor;
    private final TokenTablePanel tokenTablePanel;
    private final ErrorPanel errorPanel;
    private final MenuBar menuBar;
    private final JButton compileButton;
    private final JLabel statusLabel;

    public MainWindow() {
        super("Compilador - Autómatas");

        codeEditor = new CodeEditor();
        tokenTablePanel = new TokenTablePanel();
        errorPanel = new ErrorPanel();
        menuBar = new MenuBar();
        statusLabel = new JLabel();
        statusLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        updateStatus();

        setJMenuBar(menuBar);

        // Barra de herramientas con el botón Compilar.
        compileButton = new JButton("Compilar");
        compileButton.setToolTipText("Compilar (F5)");
        compileButton.setFocusable(false);
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(compileButton);

        // La barra de estado se actualiza cada vez que se mueve el cursor.
        codeEditor.addCaretListener(e -> updateStatus());

        // Arriba: editor a la izquierda y tablas a la derecha.
        JSplitPane topSplit = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, codeEditor, tokenTablePanel);
        topSplit.setResizeWeight(1.0);
        topSplit.setContinuousLayout(true);

        // Abajo: panel de errores.
        JSplitPane mainSplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, topSplit, errorPanel);
        mainSplit.setResizeWeight(1.0);
        mainSplit.setContinuousLayout(true);
        mainSplit.setDividerLocation(410);

        add(toolBar, BorderLayout.NORTH);
        add(mainSplit, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 450));
        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    private void updateStatus() {
        statusLabel.setText("Línea: " + codeEditor.getCaretLine()
                + "   Columna: " + codeEditor.getCaretColumn());
    }

    /** Acción al presionar el botón Compilar. */
    public void setOnCompile(Runnable action) {
        compileButton.addActionListener(e -> action.run());
    }

    /** Muestra el diálogo para elegir un archivo. Devuelve null si se cancela. */
    public File chooseFileToOpen() {
        JFileChooser chooser = new JFileChooser();
        File examples = new File("examples");
        chooser.setCurrentDirectory(examples.isDirectory()
                ? examples.getAbsoluteFile()
                : new File(System.getProperty("user.dir")));
        chooser.setDialogTitle("Abrir archivo de código");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Archivos de texto (*.txt)", "txt"));
        chooser.setAcceptAllFileFilterUsed(true);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    /** Muestra un mensaje de error interno en un cuadro de diálogo. */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public CodeEditor getCodeEditor() {
        return codeEditor;
    }

    public TokenTablePanel getTokenTablePanel() {
        return tokenTablePanel;
    }

    public ErrorPanel getErrorPanel() {
        return errorPanel;
    }

    public MenuBar getAppMenuBar() {
        return menuBar;
    }
}