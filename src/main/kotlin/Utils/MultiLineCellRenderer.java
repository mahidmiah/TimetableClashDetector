package Utils;

import Persistence.Entities.activity.ActivityModel;
import Timetable.Activity;
import scala.math.Ordering;

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
    private int detectionMode;
    private ArrayList<ActivityModel> actModelsList = new ArrayList<>();

    public MultiLineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setEditable(false); //this line doesn't seem to be doing anything
    }

    public void clashDetectionInitiate(java.util.List<java.util.List<java.lang.Integer>> slots, int year, int term, int week, int mode){
        System.out.println("slots: " + slots);
        for (java.util.List<java.lang.Integer> slot : slots){
            System.out.println("SLOT ELEM: " + slot);
            if (slot.get(0) == year && slot.get(1) == term && slot.get(2) == week){
                this.clashSlots.add(slot);
            }
        }
        System.out.println("clashSlots: " + this.clashSlots);
        this.detectionMode = mode;
    }

    public void clashDetectionInitiate(){

    }

    public void clashDetectionInitiate(scala.collection.immutable.List<scala.collection.immutable.List<java.lang.Object>> slots, int year, int term, int week, int mode){
        String x = slots.toString();
        String[] slotsToString = x.split("List\\(");
        for (String slt : slotsToString){
            if (slt.length() != 0){
                if (Integer.parseInt(String.valueOf(slt.charAt(0))) == year && Integer.parseInt(String.valueOf(slt.charAt(3))) == term && Integer.parseInt(String.valueOf(slt.charAt(6))) == week){
                    ArrayList<Integer> slot=new ArrayList<Integer>();
                    slot.add(Integer.parseInt(String.valueOf(slt.charAt(0))));
                    slot.add(Integer.parseInt(String.valueOf(slt.charAt(3))));
                    slot.add(Integer.parseInt(String.valueOf(slt.charAt(6))));
                    slot.add(Integer.parseInt(String.valueOf(slt.charAt(9))));
                    slot.add(Integer.parseInt(String.valueOf(slt.charAt(12))));
                    this.clashSlots.add(slot.stream().toList());
                }
            }
        }
        this.detectionMode = mode;
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

        for (java.util.List<Integer> slot : clashSlots){
            int count = 0;
            for (int elemSlot : slot) {

                //System.out.println("ELEM SLOT: " + count + " = " + elemSlot);
                count += 1;
            }
            if (row == slot.get(3) && column == slot.get(4)){
                if (this.detectionMode == 0){
                    setBackground(Color.red);
                    setForeground(Color.yellow);
                }
                else {
                    setBackground(Color.orange);
                    setForeground(Color.white);
                }
            }
        }

        setText((value == null) ? "" : value.toString());
        setEditable(false); //this line doesn't seem to be doing anything
        return this;
    }
}

