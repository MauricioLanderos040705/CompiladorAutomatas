import controller.MainController;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación.
 * Solo inicia la interfaz; no contiene lógica del compilador.
 */
public class Main {

    public static void main(String[] args) {
        // Swing debe crearse en el Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(() -> {
            MainController controller = new MainController();
            controller.start();
        });
    }
}