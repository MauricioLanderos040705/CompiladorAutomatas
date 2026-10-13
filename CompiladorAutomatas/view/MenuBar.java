package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Barra de menú de la aplicación.
 * Solo avisa de las acciones; quien las atiende es el controlador.
 */
public class MenuBar extends JMenuBar {

    private final JMenuItem openItem = new JMenuItem("Abrir archivo...");
    private final JMenuItem exitItem = new JMenuItem("Salir");
    private final JMenuItem compileItem = new JMenuItem("Compilar");

    public MenuBar() {
        JMenu fileMenu = new JMenu("Archivo");
        openItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        add(fileMenu);

        JMenu analysisMenu = new JMenu("Análisis");
        compileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        analysisMenu.add(compileItem);
        add(analysisMenu);
    }

    public void setOnOpen(Runnable action) {
        openItem.addActionListener(e -> action.run());
    }

    public void setOnExit(Runnable action) {
        exitItem.addActionListener(e -> action.run());
    }

    /** Acción al elegir "Compilar". */
    public void setOnCompile(Runnable action) {
        compileItem.addActionListener(e -> action.run());
    }
}