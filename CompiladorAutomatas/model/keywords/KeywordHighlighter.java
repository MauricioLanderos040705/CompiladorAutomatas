package model.keywords;

import model.lexer.Lexer;
import model.lexer.Token;
import model.lexer.TokenType;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Colorea las palabras reservadas mientras se escribe.
 * Cada vez que cambia el texto, vuelve a pintar todo el documento.
 */
public class KeywordHighlighter {

    /** Color de las palabras reservadas (definido por el equipo). */
    public static final Color KEYWORD_COLOR = new Color(0xD3, 0x54, 0x00);

    private final JTextPane textPane;
    private final Lexer lexer = new Lexer();
    private final SimpleAttributeSet normalStyle = new SimpleAttributeSet();
    private final SimpleAttributeSet keywordStyle = new SimpleAttributeSet();
    private boolean pending = false;

    public KeywordHighlighter(JTextPane textPane) {
        this.textPane = textPane;

        StyleConstants.setForeground(normalStyle, textPane.getForeground());
        StyleConstants.setBold(normalStyle, false);

        StyleConstants.setForeground(keywordStyle, KEYWORD_COLOR);
        StyleConstants.setBold(keywordStyle, true);

        // Solo se escuchan inserciones y borrados. Cambiar estilos genera
        // "changedUpdate", que se ignora para no provocar un ciclo.
        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { schedule(); }
            @Override public void removeUpdate(DocumentEvent e) { schedule(); }
            @Override public void changedUpdate(DocumentEvent e) { }
        });
    }

    /** No se puede modificar el documento dentro del listener: se difiere. */
    private void schedule() {
        if (pending) {
            return;
        }
        pending = true;
        SwingUtilities.invokeLater(() -> {
            pending = false;
            highlight();
        });
    }

    /** Repinta todo el texto: base normal y encima las palabras reservadas. */
    public void highlight() {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            String text = doc.getText(0, doc.getLength());
            doc.setCharacterAttributes(0, text.length(), normalStyle, true);

            for (Token token : lexer.tokenize(text)) {
                if (token.getType() == TokenType.RESERVED_WORD) {
                    int lineStart = doc.getDefaultRootElement()
                            .getElement(token.getLine() - 1).getStartOffset();
                    int start = lineStart + token.getColumn() - 1;
                    doc.setCharacterAttributes(start, token.getLexeme().length(), keywordStyle, true);
                }
            }
        } catch (BadLocationException e) {
            // No debería ocurrir: se lee el documento completo.
        }
    }
}
