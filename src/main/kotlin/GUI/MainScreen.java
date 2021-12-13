package GUI;

import Timetable.Timetable;
import Timetable.Week;
import Timetable.Day;
import Timetable.Activity;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainScreen extends JFrame{
    private final DefaultTableModel TableModel;
    private final HashMap<Double, Integer> doubleTimeSlotToInt;
    private JLabel courseNameLabel;
    private JButton insertActivityButton;
    private JButton removeActivityButton;
    private JList courseModuleJList;
    private JButton addCourseModuleButton;
    private JButton removeCourseModuleButton;
    private JRadioButton year1RadioButton;
    private JRadioButton year2RadioButton;
    private JRadioButton year3RadioButton;
    private JRadioButton Term1RadioButton;
    private JLabel yearLabel;
    private JLabel termLabel;
    private JLabel courseModulesLabel;
    private JLabel manageActivitesLabel;
    private JRadioButton Term2RadioButton;
    private JPanel panelMain;
    private JPanel yearAndTermPanel;
    private JPanel manageActivitiesPanel;
    private JPanel courseModulesPanel;
    private JPanel menuPanel;
    private JTable timeTable;
    private JPanel tablePanel;
    private JScrollPane ScrollPane;

    public MainScreen(){

        super("Main Page");

        this.doubleTimeSlotToInt = new HashMap<Double, Integer>();

        double counter = 9.0;
        int couunter_= 0;
        while (counter < 21.5) {
            this.doubleTimeSlotToInt.put(counter, couunter_);
            counter = counter +0.5;
            couunter_ ++;
        }

        this.yearAndTermPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.black));
        this.courseModulesPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.black));

        this.yearLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
        this.termLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));

        this.courseModulesLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
        this.manageActivitesLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));

        this.timeTable.setVisible(true);
        this.timeTable.setOpaque(false);

        String[] columns = new String[] {
                "Time Slot", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
        };

        this.TableModel = new DefaultTableModel(30, 0);
        TableModel.setColumnIdentifiers(columns);

        this.panelMain.setPreferredSize(new Dimension(775, 400));
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    public void updateAtCell(int Row, int Column){

    }

//    public void update(Timetable timetable){
//        for (Week termWeek : timetable.getTable().values()){
//            for (Day day : termWeek.getDays()){
//                for (double timeslot : day.getTimeSlot().keySet()){
//                    if(day.getTimeSlot().get(timeslot) != null){
//                        List<String> acts = new ArrayList<String>();
//                        for (Activity act : day.getTimeSlot().get(timeslot)){
//                            acts.add(Integer.toString(act.getModule()));
//                        }
//                        this.TableModel.setValueAt(acts, this.doubleTimeSlotToInt.get(timeslot), day.getDayOfWeek()+1);
//                    }
//                }
//            }
//        }
//        this.timeTable.setModel(this.TableModel);
//    }

}

