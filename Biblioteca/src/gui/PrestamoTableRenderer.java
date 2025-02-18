package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PrestamoTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column == 4) { // Columna de "Fecha Devolución"
            if (value != null && value.toString().equals("Pendiente")) {
                c.setForeground(Color.RED); // Texto rojo para préstamos sin devolver
            } else {
                c.setForeground(Color.BLACK);
            }
        } else {
            c.setForeground(Color.BLACK);
        }

        return c;
    }
}
