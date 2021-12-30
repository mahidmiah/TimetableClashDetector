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

    private ArrayList<java.util.List<Integer>> clashSlots = new ArrayList<>();
    private int detectionMode;
    private ArrayList<ActivityModel> actModelsList = new ArrayList<>();

    // DEFAULT CODE - ALREADY PROVIDED IN CLASS
    public MultiLineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setEditable(false); //this line doesn't seem to be doing anything
    }

    // KOTLIN VERSION OF CLASH DETECTION INITIATOR
    public void clashDetectionInitiate(java.util.List<java.util.List<java.lang.Integer>> slots, int year, int term, int week, int mode){
        System.out.println("slots: " + slots);
        for (java.util.List<java.lang.Integer> slot : slots){

            // ITERATES THROUGH RECIEVED CLASH DATA AND ONLY USES CLASHES IF THEY MATCH THE CURRENT YEAR, TERM, AND WEEK BEING DISPLAYED
            if (slot.get(0) == year && slot.get(1) == term && slot.get(2) == week){
                this.clashSlots.add(slot);
            }
        }
        this.detectionMode = mode; // USED TO DISPLAY THE CLASHES IN DIFFERENT COLOURS (TO SHOW THE DIFFERENCE BETWEEN THE CLASH SYSTEM BEING USED)
    }

    // SCALA VERSION OF CLASH DETECTION INITIATOR
    public void clashDetectionInitiate(scala.collection.immutable.List<scala.collection.immutable.List<java.lang.Object>> slots, int year, int term, int week, int mode){
        /* I WAS UNABLE TO ITERATE THROUGH THE SCALA LIST OBJECT IN JAVA, I WAS ALSO NOT ABLE TO CONVERT INTO THE REQUIRED JAVA ARRAYSLIST OBJECT.
        * SO I THEREFORE WORKED AROUND THIS PROBLEM RATHER THEN FIXING
        * MY SOLUTION THAT WORKS AS INTENDED, IS TO CALL THE THE SCALA LIST OBJEC TO A STRING
        * THEN SPLITTING THE STRING USING THE '.SPLIT()' METHOD TO PULL THE INIDIVIUAL ELEMETNS FROM THE LIST
        * USING TH EXCTRACTED STRINGS (STORED IN ARRAY), THE REQUIRED JAVA ARRAYS LIST OBJECT COULD BE GENERATED*/

        String x = slots.toString(); // CALLS THE SCALA OBJECT TO STRING
        String[] slotsToString = x.split("List\\("); // THE STRING IS THEN SPLIT TO GET THE INDIVIDUAL ELEMENTS

        // ITERATES THROUGH RECIEVED CLASH DATA AND ONLY USES CLASHES IF THEY MATCH THE CURRENT YEAR, TERM, AND WEEK BEING DISPLAYED
        for (String slt : slotsToString){
            //LOOPS THROUGH THE EXTRACTED ELEMENTS (FROM THE SPLIT STRING)
            if (slt.length() != 0){
                //ONLY USES CLASHES IF THEY MATCH THE CURRENT YEAR, TERM, AND WEEK BEING DISPLAYED
                if (Integer.parseInt(String.valueOf(slt.charAt(0))) == year && Integer.parseInt(String.valueOf(slt.charAt(3))) == term && Integer.parseInt(String.valueOf(slt.charAt(6))) == week){
                    // GENERATES THE REQUIRED ARRAY LIST OBJECT USING THE EXTRACTED ELEMENT FROM THE SPLIT STRING
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
        this.detectionMode = mode;// USED TO DISPLAY THE CLASHES IN DIFFERENT COLOURS (TO SHOW THE DIFFERENCE BETWEEN THE CLASH SYSTEM BEING USED)
    }

    // DEFAULT CODE - ALREADY PROVIDED IN CLASS
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


        //CODE THAT DISPLAYS THE CLASHES
        for (java.util.List<Integer> slot : clashSlots){
            if (row == slot.get(3) && column == slot.get(4)){
                if (this.detectionMode == 0){
                    // IF CLASH MODE IS 0 (KOTLIN) IT DISPLAYS THE CELLS WITH BACKGROUND COLOUR OF RED AND TEXT COLOUR YELLOW
                    setBackground(Color.red);
                    setForeground(Color.yellow);
                }
                else {
                    // IF CLASH MODE IS 1 (SCALA) IT DISPLAYS THE CELLS WITH BACKGROUND COLOUR OF ORANGE AND TEXT COLOUR WHITE
                    setBackground(Color.orange);
                    setForeground(Color.white);
                }
            }
        }

        // DEFAULT CODE - ALREADY PROVIDED IN CLASS
        setText((value == null) ? "" : value.toString());
        setEditable(false); //this line doesn't seem to be doing anything
        return this;
    }
}

