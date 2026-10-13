package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Sección de la ventana donde se reportan los errores de compilación.
 * Solo muestra datos simples; no conoce las clases del modelo.
 */
public class ErrorPanel extends JPanel {

    private static final Color ERROR_COLOR = new Color(0xB0, 0x1E, 0x1E);
    private static final Color OK_COLOR = new Color(0x1E, 0x7B, 0x34);

    private final DefaultTableModel tableModel;
    private final JLabel summaryLabel = new JLabel("Aún no se ha analizado el código");

    public ErrorPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Errores"));

        tableModel = new DefaultTableModel(
                new Object[]{"No.", "Tipo", "Descripción", "Línea", "Col."}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(520);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(45);

        summaryLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));

        add(summaryLabel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setPreferredSize(new Dimension(400, 160));
    }

    /** Quita todos los errores mostrados. */
    public void clearErrors() {
        tableModel.setRowCount(0);
    }

    /** Agrega un error al final de la tabla. */
    public void addError(int number, String type, String description, int line, int column) {
        tableModel.addRow(new Object[]{number, type, description, line, column});
    }

    /** Actualiza el resumen; rojo si hay errores, verde si no. */
    public void setSummary(String text, boolean hasErrors) {
        summaryLabel.setText(text);
        summaryLabel.setForeground(hasErrors ? ERROR_COLOR : OK_COLOR);
    }
}
