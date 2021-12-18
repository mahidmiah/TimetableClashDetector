package Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

    private int rowN;
    private int colN;
    private ArrayList<java.util.List<Integer>> clashSlots = new ArrayList<>();

    public MultiLineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setEditable(false); //this line doesn't seem to be doing anything
    }

    public void clashDetectionInitiate(java.util.List<java.util.List<java.lang.Integer>> slots, int year, int term, int week){
        ArrayList<java.util.List<Integer>> listOLists = new ArrayList<>();

        for (java.util.List<java.lang.Integer> slot : slots){
            if (slot.get(0) == year && slot.get(1) == term && slot.get(2) == week){
                this.clashSlots.add(slot);
            }
        }
        //System.out.println("Test: " + this.clashSlots.toString());
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

//        if (value != "") {
//            setBackground(Color.orange);
//        } else {
//            setBackground(Color.yellow);
//        }

        for (java.util.List<Integer> slot : clashSlots){
            if (row == slot.get(3) && column == slot.get(4)){
                setBackground(Color.red);
                setForeground(Color.yellow);
            }
        }

//        if (row == this.rowN && column == this.colN && value != "") {
//            setBackground(Color.cyan);
//            setForeground(Color.GREEN);
//        }



        setText((value == null) ? "" : value.toString());
        setEditable(false); //this line doesn't seem to be doing anything
        return this;
    }
}

