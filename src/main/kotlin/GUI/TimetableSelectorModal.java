package GUI;

import Timetable.Timetable;
import kotlin.jvm.internal.markers.KMutableList;
import use_cases.timetable.fetch_timetables.FetchTimetables;

import javax.swing.*;
import java.util.ArrayList;

public class TimetableSelectorModal extends JFrame {
    private JPanel mainPanel;
    private JComboBox timetableComboBox;
    private JButton okBtn;
    private JButton cancelBtn;
    private JLabel timetableInstructionLabel;


    // DATA
    private ArrayList<Timetable> timetables = new ArrayList<Timetable>();

    public TimetableSelectorModal(){
        super("Timetable Selector");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setupComboBoxData();

        this.pack();
    }

    public void setupComboBoxData() {
        FetchTimetables fetchTimetables = new FetchTimetables();
        this.timetables = fetchTimetables.fetchAsArrayList();
        System.out.println("TIMETABLES: " + timetables);
        ArrayList<Integer> timetablesIds = this.flatTimetableArrayListToIds(this.timetables);
        Integer[] ids = (Integer[]) timetablesIds.toArray(new Integer[timetablesIds.size()]);
        System.out.println("IDS: " + ids);
        JComboBox timetableComboBox = new JComboBox(ids);


    }

    private ArrayList<Integer> flatTimetableArrayListToIds(ArrayList<Timetable> timetablesList){
        ArrayList<Integer> idsList = new ArrayList<Integer>();

        for (Timetable timetable : timetablesList) {
            idsList.add(timetable.getID());
        }

        return idsList;

    }

    public static void main(String[] args){
        JFrame jFrame = new TimetableSelectorModal();
        jFrame.setVisible(true);
    }
}
