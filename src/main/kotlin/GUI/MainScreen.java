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
    private DefaultTableModel TableModel;
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
    private JButton displayTableButton;
    private JLabel weekLabel;
    private JRadioButton Week2RadioButton;
    private JButton clashDetectionButton;

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

        this.courseNameLabel.setText(timetable.getDisplayLabel());

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

        this.panelMain.setPreferredSize(new Dimension(1000, 700));
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        if (table.getCourseType().equals("postgraduate")){ //Postgraduate
            this.year2RadioButton.setEnabled(false);
            this.year3RadioButton.setEnabled(false);
        }

        ButtonGroup yearGroupRadioGroup = new ButtonGroup();
        yearGroupRadioGroup.add(year1RadioButton);
        year1RadioButton.setActionCommand("1");

        yearGroupRadioGroup.add(year2RadioButton);
        year2RadioButton.setActionCommand("2");

        yearGroupRadioGroup.add(year3RadioButton);
        year3RadioButton.setActionCommand("3");


        ButtonGroup termGroupRadioGroup = new ButtonGroup();
        termGroupRadioGroup.add(Term1RadioButton);
        Term1RadioButton.setActionCommand("1");

        termGroupRadioGroup.add(Term2RadioButton);
        Term2RadioButton.setActionCommand("2");


        ButtonGroup weekGroupRadioGroup = new ButtonGroup();
        weekGroupRadioGroup.add(Week1RadioButton);
        Week1RadioButton.setActionCommand("1");

        weekGroupRadioGroup.add(Week2RadioButton);
        Week2RadioButton.setActionCommand("2");

        update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);

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
                    if(!timetable.getModules().containsKey(Integer.parseInt(moduleIDTextField.getText()))){
                        table.addModule(Integer.parseInt(moduleIDTextField.getText()), moduleNameTextField.getText(), trueFalseDict.get(optionalChoiceGroup.getSelection().getActionCommand()));
                        updateModulesList(table);
                    }
                    else {
                        JOptionPane.showMessageDialog(panelMain, "Module ID already exists!", "Module ID Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(panelMain, "Please ensure all fields have correct input!", "Add Module Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        displayTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);
            }
        });

        removeCourseModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courseModuleJList.getSelectedValue() != null){
                    int result = JOptionPane.showConfirmDialog(panelMain,"Are you sure you will like to remove the module?", "Remove Module",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        int moduleID = Integer.parseInt(String.valueOf(courseModuleJList.getSelectedValue().toString().charAt(4)));
                        timetable.removeModule(moduleID);
                        update(Integer.parseInt(yearGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(termGroupRadioGroup.getSelection().getActionCommand()), Integer.parseInt(weekGroupRadioGroup.getSelection().getActionCommand()), table);
                        JOptionPane.showMessageDialog(panelMain, "Module and all relevant activities have successfully been removed.", "Success", JOptionPane.PLAIN_MESSAGE);

                    }
                    else if (result == JOptionPane.NO_OPTION){
                        JOptionPane.showMessageDialog(panelMain, "Module has not been removed", "Module removal", JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(panelMain, "No Module has been selected", "Remove Module Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(panelMain, "No Module has been selected", "Remove Module Error", JOptionPane.ERROR_MESSAGE);
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
                else {
                    this.TableModel.setValueAt("", this.doubleTimeSlotToInt.get(slot.getKey()), day.getDayNumber()+1);
                }
            }
        }
        this.timeTable.setModel(this.TableModel);
        this.timeTable.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
        this.timeTable.getColumnModel().getColumn(0).setPreferredWidth(10);

        updateModulesList(Timetable);

    }

}

