package Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Vector;

public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

    public MultiLineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setEditable(false); //this line doesn't seem to be doing anything
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setForeground(Color.red);

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        }
        else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setFont(table.getFont());
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));
                setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        }
        else {
            setBorder(new EmptyBorder(1, 2, 1, 2));
        }

        if (value != "") {
            setBackground(Color.orange);
        } else {
            setBackground(Color.yellow);
        }

        setText((value == null) ? "" : value.toString());
        setEditable(false); //this line doesn't seem to be doing anything
        return this;
    }
}

