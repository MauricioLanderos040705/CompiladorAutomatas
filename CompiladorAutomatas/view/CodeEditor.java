package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * Área donde el usuario escribe el código fuente.
 * Contiene únicamente componentes gráficos; no conoce las reglas del lenguaje.
 */
public class CodeEditor extends JPanel {

    private final JTextPane textPane;

    public CodeEditor() {
        super(new BorderLayout());

        // JTextPane permite estilos por fragmento de texto (se usará en el punto 3).
        // Se redefine getScrollableTracksViewportWidth para que las líneas largas
        // NO se partan y aparezca barra de desplazamiento horizontal.
        textPane = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                Component parent = getParent();
                return parent == null
                        || getUI().getPreferredSize(this).width <= parent.getWidth();
            }
        };

        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        textPane.setBorder(new EmptyBorder(6, 8, 6, 8));

        add(new JScrollPane(textPane), BorderLayout.CENTER);
    }

    /**
     * Devuelve el texto exacto del documento.
     * Se usa Document.getText y no JTextPane.getText porque este último puede
     * cambiar los saltos de línea en Windows ("\r\n"), lo que desplazaría las
     * posiciones que más adelante se reportarán en los errores.
     */
    public String getSourceCode() {
        Document doc = textPane.getDocument();
        try {
            return doc.getText(0, doc.getLength());
        } catch (BadLocationException e) {
            return "";
        }
    }

    /** Reemplaza todo el contenido del editor (lo usará el punto 4). */
    public void setSourceCode(String code) {
        textPane.setText(code);
        textPane.setCaretPosition(0);
    }

    /** Borra el contenido del editor. */
    public void clear() {
        textPane.setText("");
    }

    /** Línea actual del cursor, empezando en 1. */
    public int getCaretLine() {
        Element root = textPane.getDocument().getDefaultRootElement();
        return root.getElementIndex(textPane.getCaretPosition()) + 1;
    }

    /** Columna actual del cursor, empezando en 1. */
    public int getCaretColumn() {
        Element root = textPane.getDocument().getDefaultRootElement();
        int pos = textPane.getCaretPosition();
        int lineIndex = root.getElementIndex(pos);
        return pos - root.getElement(lineIndex).getStartOffset() + 1;
    }

    /** Permite a la ventana enterarse de los movimientos del cursor. */
    public void addCaretListener(CaretListener listener) {
        textPane.addCaretListener(listener);
    }

    /** Acceso al componente interno (lo usará KeywordHighlighter en el punto 3). */
    public JTextPane getTextPane() {
        return textPane;
    }
}