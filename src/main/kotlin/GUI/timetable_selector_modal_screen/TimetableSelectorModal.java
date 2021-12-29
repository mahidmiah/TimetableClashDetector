package GUI.timetable_selector_modal_screen;

import Timetable.Timetable;
import use_cases.timetable.fetch_timetables.FetchTimetables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * Modal Screen to select Timetables
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

    //Events
    /**
     * Listener triggered when the user confirms the Timetable selection
     */
    public ITimetableConfirmationListener timetableConfirmationListener;


    // DATA
    /**
     * List of timetables
     */
    private ArrayList<Timetable> selectorTimetables = new ArrayList<Timetable>();

    public TimetableSelectorModal(){
        super("Timetable Selector");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setupComboBoxData();


        this.pack();

        TimetableSelectorModal self = this;
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedId = self.timetableComboBox.getSelectedItem();
                Timetable foundTimetable = self.findTimetableById(self.selectorTimetables, (Integer) selectedId);
                if (foundTimetable != null) {
                    self.onTimetableConfirmation(foundTimetable);
                }

            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                self.dispose();
            }
        });
    }

    public void setupComboBoxData() {
        FetchTimetables fetchTimetables = new FetchTimetables();
        this.selectorTimetables = fetchTimetables.fetchAsArrayList();
        //System.out.println("TIMETABLES: " + selectorTimetables);

        /*
        Here if we need to flat the ArrayList to just Ids.
        ArrayList<Integer> timetablesIds = this.flatTimetableArrayListToIds(this.selectorTimetables);
        System.out.println("TIMETABLES IDS: " + timetablesIds);
        Integer[] ids = timetablesIds.toArray(new Integer[timetablesIds.size()]);
        System.out.println("TIMETABLES IDS[]: " + ids);

         */


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

    public void onTimetableConfirmation(Timetable timetable){
        if (this.timetableConfirmationListener != null) {
            this.timetableConfirmationListener.actionPerformed(timetable);
        }
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


    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }



        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {

            Object timetableId = value;
            int selectedIndex = ((Integer)timetableId).intValue();
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            Timetable foundTimetable = findTimetableById(selectorTimetables, (Integer) timetableId);
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
