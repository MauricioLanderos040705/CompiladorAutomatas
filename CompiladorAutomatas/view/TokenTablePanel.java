package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Panel con dos pestañas: "Tabla de símbolos" y "Tokens".
 * Solo muestra datos simples; no conoce las clases del modelo.
 */
public class TokenTablePanel extends JPanel {

    private final DefaultTableModel symbolModel;
    private final DefaultTableModel tokenModel;
    private final JTabbedPane tabs = new JTabbedPane();

    public TokenTablePanel() {
        super(new BorderLayout());

        symbolModel = createModel("No.", "Lexema", "Categoría");
        JTable symbolTable = new JTable(symbolModel);
        symbolTable.setFillsViewportHeight(true);
        symbolTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        symbolTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        symbolTable.getColumnModel().getColumn(2).setPreferredWidth(130);

        tokenModel = createModel("No.", "Lexema", "Tipo", "Línea", "Col.");
        JTable tokenTable = new JTable(tokenModel);
        tokenTable.setFillsViewportHeight(true);
        tokenTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        tokenTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        tokenTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        tokenTable.getColumnModel().getColumn(3).setPreferredWidth(45);
        tokenTable.getColumnModel().getColumn(4).setPreferredWidth(40);

        tabs.addTab("Tabla de símbolos", new JScrollPane(symbolTable));
        tabs.addTab("Tokens", new JScrollPane(tokenTable));
        add(tabs, BorderLayout.CENTER);
        setPreferredSize(new Dimension(380, 400));
    }

    private DefaultTableModel createModel(Object... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // ----- Tabla de símbolos -----

    public void clearRows() {
        symbolModel.setRowCount(0);
    }

    public void addRow(int number, String lexeme, String category) {
        symbolModel.addRow(new Object[]{number, lexeme, category});
    }

    // ----- Tokens -----

    public void clearTokens() {
        tokenModel.setRowCount(0);
    }

    public void addToken(int number, String lexeme, String type, int line, int column) {
        tokenModel.addRow(new Object[]{number, lexeme, type, line, column});
    }

    /** Pasa a la pestaña de tokens. */
    public void showTokensTab() {
        tabs.setSelectedIndex(1);
    }
}