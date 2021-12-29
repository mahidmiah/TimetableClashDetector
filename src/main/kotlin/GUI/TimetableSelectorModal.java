package GUI;

import Timetable.Timetable;
import use_cases.timetable.fetch_timetables.FetchTimetables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * Modal to select Timetables
 * Helpful links:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
 *
 */
public class TimetableSelectorModal extends JFrame {
    private JPanel mainPanel;
    private JComboBox timetableComboBox;
    private JButton okBtn;
    private JButton cancelBtn;
    private JLabel timetableInstructionLabel;


    // DATA
    private ArrayList<Timetable> selectorTimetables = new ArrayList<Timetable>();

    public TimetableSelectorModal(){
        super("Timetable Selector");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setupComboBoxData();

        this.pack();
    }

    public void setupComboBoxData() {
        FetchTimetables fetchTimetables = new FetchTimetables();
        this.selectorTimetables = fetchTimetables.fetchAsArrayList();
        System.out.println("TIMETABLES: " + selectorTimetables);
        ArrayList<Integer> timetablesIds = this.flatTimetableArrayListToIds(this.selectorTimetables);
        System.out.println("TIMETABLES IDS: " + timetablesIds);
        Integer[] ids = timetablesIds.toArray(new Integer[timetablesIds.size()]);
        System.out.println("TIMETABLES IDS[]: " + ids);


        ComboBoxRenderer renderer= new ComboBoxRenderer();
        timetableComboBox.setRenderer(renderer);
        for (Timetable timetable : this.selectorTimetables) {

            timetableComboBox.addItem(timetable.getID());
        }



    }

    private ArrayList<Integer> flatTimetableArrayListToIds(ArrayList<Timetable> timetablesList){
        ArrayList<Integer> idsList = new ArrayList<Integer>();

        for (Timetable timetable : timetablesList) {
            idsList.add(timetable.getID());
        }

        return idsList;

    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Timetable findTimetableById(ArrayList<Timetable> localTimetables, Integer id){
            for (int i = 0; i < localTimetables.size(); i++) {
                Timetable timetable = localTimetables.get(i);
                if (timetable.getID() == id) {
                    return timetable;
                }
            }
            return null;
        }

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            int selectedIndex = ((Integer)value).intValue();
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            System.out.println("VALUE: + " + value);
            Timetable foundTimetable = this.findTimetableById(selectorTimetables, (Integer) value);
            if (foundTimetable != null) {
                setText(foundTimetable.getName().toString());
            } else {
                setText("Timetable not found");
            }

            return this;

        }

    }

    public static void main(String[] args){
        JFrame jFrame = new TimetableSelectorModal();
        jFrame.setVisible(true);
    }
}
