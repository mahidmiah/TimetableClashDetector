package GUI;

import Timetable.Timetable;
import Timetable.Week;
import Timetable.Day;
import Timetable.Activity;
import Timetable.Module;
import Utils.MultiLineCellRenderer;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MainScreen extends JFrame{
    private final DefaultTableModel TableModel;
    private final HashMap<Double, Integer> doubleTimeSlotToInt;
    public Timetable table;
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
    private JRadioButton Week1RadioButton;
    private JButton button1;
    private JLabel weekLabel;
    private JRadioButton Week2RadioButton;

    public MainScreen(Timetable timetable){

        super("Main Page");

        this.doubleTimeSlotToInt = new HashMap<Double, Integer>();

        this.table = timetable;

        double counter = 9.0;
        int counter_= 0;
        while (counter < 21.5) {
            this.doubleTimeSlotToInt.put(counter, counter_);
            counter = counter +0.5;
            counter_ ++;
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

        this.TableModel = new DefaultTableModel(25, 0);
        TableModel.setColumnIdentifiers(columns);

        this.timeTable.setRowHeight(75);

        this.panelMain.setPreferredSize(new Dimension(900, 600));
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        addCourseModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(5, 3));

                JLabel moduleIDLabel = new JLabel();
                JLabel moduleNameLabel = new JLabel();
                JLabel moduleIsOptionalLabel = new JLabel();

                JTextField moduleIDTextField = new JTextField(10);
                JTextField moduleNameTextField = new JTextField(10);

                moduleIDLabel.setText("Module ID:");
                moduleNameLabel.setText("Module Name");
                moduleIsOptionalLabel.setText("Is optional:");

                JRadioButton trueRadioButton = new JRadioButton();
                trueRadioButton.setActionCommand("True");
                JRadioButton falseRadioButton = new JRadioButton();
                falseRadioButton.setActionCommand("False");

                trueRadioButton.setText("True");
                falseRadioButton.setText("False");

                ButtonGroup optionalChoiceGroup = new ButtonGroup();
                optionalChoiceGroup.add(trueRadioButton);
                optionalChoiceGroup.add(falseRadioButton);

                panel.add(moduleIDLabel);
                panel.add(moduleIDTextField);

                panel.add(moduleNameLabel);
                panel.add(moduleNameTextField);

                panel.add(moduleIsOptionalLabel);
                panel.add(trueRadioButton);
                panel.add(falseRadioButton);

                Hashtable<String, Boolean> trueFalseDict = new Hashtable<String, Boolean>();
                trueFalseDict.put("True", true);
                trueFalseDict.put("False", false);

                JOptionPane.showMessageDialog(panelMain, panel);

                if (!moduleIDTextField.getText().isEmpty() && !moduleNameTextField.getText().isEmpty() && !optionalChoiceGroup.getSelection().getActionCommand().isEmpty()){
                    System.out.println("Module ID: " +Integer.parseInt(moduleIDTextField.getText()));
                    System.out.println("Module Name: " + moduleNameTextField.getText());
                    System.out.println("Is Optional: " + trueFalseDict.get(optionalChoiceGroup.getSelection().getActionCommand()));
                    table.addModule(Integer.parseInt(moduleIDTextField.getText()), moduleNameTextField.getText(), trueFalseDict.get(optionalChoiceGroup.getSelection().getActionCommand()));
                    updateModulesList(table);
                }
                else{
                    System.out.println("Error: Missing fields!");
                    JOptionPane.showMessageDialog(panelMain, "Please ensure all fields have correct input!", "Add Module Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void updateAtCell(int Row, int Column){

    }

    public void updateModulesList(Timetable Timetable){
        DefaultListModel listOfModules = new DefaultListModel();
        Collection<Module> modules = Timetable.getModules().values();
        for (Module mod : modules){
            listOfModules.addElement(mod.toString());
        }

        this.courseModuleJList.setModel(listOfModules);
    }

    public void update(int Year, int Term, int Week, Timetable Timetable){

        double time = 9.0;
        int row = 0;
        while(time < 21.5){
            this.TableModel.setValueAt(time, row, 0);
            time += 0.5;
            row += 1;
        }

        Week week = Timetable.getTable().get(Year).getTerms().get(Term).getWeeks().get(Week);
        for (Day day : week.getDays().values()){
            HashMap<Double, List<Activity>> timeslot = day.getTimeSlot();
            for (Map.Entry<Double, List<Activity>> slot : timeslot.entrySet()){
                if (slot.getValue() != null){
                    StringBuilder activities = new StringBuilder();
                    for (Activity act : slot.getValue()){
                        activities.append(act.toString()).append("\n");
                    }
                    this.TableModel.setValueAt(activities, this.doubleTimeSlotToInt.get(slot.getKey()), day.getDayNumber()+1);
                }
            }
        }
        this.timeTable.setModel(this.TableModel);
        this.timeTable.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
        this.timeTable.getColumnModel().getColumn(0).setPreferredWidth(10);

        updateModulesList(Timetable);

    }

}

