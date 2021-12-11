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
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class MainScreen extends JFrame{
    private final DefaultTableModel TableModel;
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

        Object[][] data = new Object[][] {
                {1, "John", 40.0, false },
                {2, "Rambo", 70.0, false },
                {3, "Zorro", 60.0, true },
        };

        this.TableModel = new DefaultTableModel(0, 0);
        TableModel.setColumnIdentifiers(columns);

//        this.timeTable.setModel(new DefaultTableModel(null, columns));
        this.panelMain.setPreferredSize(new Dimension(750, 300));
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    public void update(Timetable timetable){
        for (Week termWeek : timetable.getTable().values()){
            for (Day day : termWeek.getDays()){
                for (double timeslot : day.getTimeSlot().keySet()){
                    //this.TableModel.addRow(new Object[] {timeslot,"Test2","Test3"});
                    System.out.println(day.getDayOfWeek());
                    System.out.println(timeslot);
                    System.out.println(day.getTimeSlot().get(timeslot));
                    System.out.println("------------------------------------------");
                }
            }
        }
        this.timeTable.setModel(this.TableModel);
    }

}

