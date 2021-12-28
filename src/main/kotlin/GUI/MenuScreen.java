package GUI;

import Persistence.Entities.course.CourseModel;
import Persistence.Entities.course_module.CourseModuleModel;
import Persistence.Entities.course_type.CourseTypeModel;
import Persistence.Entities.timetable.TimetableModel;
import Timetable.Timetable;
import use_cases.course.InsertCourse;
import use_cases.course.InsertCourseResult;
import use_cases.course_module.insert_course_module.UseCaseError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class MenuScreen extends JFrame{
    private JButton createNewTimetableButton;
    private JButton loadTimetableButton;
    private JPanel mainPanel;

    public MenuScreen(){
        super("Menu");

        this.mainPanel.setPreferredSize(new Dimension(510, 150));
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        createNewTimetableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(5, 3));

                JLabel courseIDLabel = new JLabel();
                JLabel courseNameLabel = new JLabel();
                JLabel courseStartYearLabel = new JLabel();
                JLabel courseEndYearLabel = new JLabel();
                JLabel courseTypeLabel = new JLabel();

                JTextField courseIDTextField = new JTextField(10);
                JTextField courseNameTextField = new JTextField(10);
                JTextField courseStartYearTextField = new JTextField(10);
                JTextField courseEndYearTextField = new JTextField(10);

                courseIDLabel.setText("Course ID:");
                courseNameLabel.setText("Course Name");
                courseStartYearLabel.setText("Course Start Year:");
                courseEndYearLabel.setText("Course End Year:");

                JRadioButton trueRadioButton = new JRadioButton();
                trueRadioButton.setActionCommand("Undergraduate");
                JRadioButton falseRadioButton = new JRadioButton();
                falseRadioButton.setActionCommand("Postgraduate");

                trueRadioButton.setText("Undergraduate");
                falseRadioButton.setText("Postgraduate");

                ButtonGroup courseTypeGroup = new ButtonGroup();
                courseTypeGroup.add(trueRadioButton);
                courseTypeGroup.add(falseRadioButton);

                panel.add(courseIDLabel);
                panel.add(courseIDTextField);

                panel.add(courseNameLabel);
                panel.add(courseNameTextField);

                panel.add(courseStartYearLabel);
                panel.add(courseStartYearTextField);

                panel.add(courseEndYearLabel);
                panel.add(courseEndYearTextField);

                panel.add(trueRadioButton);
                panel.add(falseRadioButton);

                Hashtable<String, Boolean> trueFalseDict = new Hashtable<String, Boolean>();
                trueFalseDict.put("Undergraduate", true);
                trueFalseDict.put("Postgraduate", false);

                JOptionPane.showMessageDialog(mainPanel, panel);
                if (!courseNameTextField.getText().isEmpty() && !courseStartYearTextField.getText().isEmpty() && !courseStartYearTextField.getText().isEmpty() && courseTypeGroup.getSelection() != null){
//                    if(!timetable.getModules().containsKey(Integer.parseInt(moduleIDTextField.getText()))){
//                        table.addModule(Integer.parseInt(moduleIDTextField.getText()), moduleNameTextField.getText(), trueFalseDict.get(optionalChoiceGroup.getSelection().getActionCommand()));
//                        updateModulesList(table);
//                    }
//                    else {
//                        JOptionPane.showMessageDialog(panelMain, "Module ID already exists!", "Module ID Error", JOptionPane.ERROR_MESSAGE);
//                    }




                    InsertCourse courseCreator = new InsertCourse();

                    courseCreator.setCourseName(courseNameTextField.getText());
                    courseCreator.setStartYear(Integer.parseInt(courseStartYearTextField.getText()));
                    courseCreator.setEndYear(Integer.parseInt(courseEndYearTextField.getText()));
                    courseCreator.setCourseType(courseTypeGroup.getSelection().getActionCommand());

                    System.out.println("GetActionCommand: " + courseTypeGroup.getSelection().getActionCommand());
                    try {
                        InsertCourseResult courseResult = courseCreator.insert();
                        CourseModel newCourse = courseResult.getCourseModel();
                        TimetableModel newTimetable = courseResult.getTimetableModel();
                        boolean isUndergraduate = courseResult.getCourseTypeModel().getLabel().equals(CourseTypeModel.UNDERGRADUATE);
                        //Timetable timetable = new Timetable(Integer.parseInt(courseIDTextField.getText()), courseNameTextField.getText(), Integer.parseInt(courseStartYearTextField.getText()), Integer.parseInt(courseEndYearTextField.getText()), trueFalseDict.get(courseTypeGroup.getSelection().getActionCommand()));

                        Timetable timetable = new Timetable(
                                newTimetable.getId_timetable(),
                                newCourse.getName(),
                                newCourse.getStart_year(),
                                newCourse.getEnd_year(),
                                isUndergraduate);

                        dispose();

                        MainScreen mainScreen = new MainScreen(timetable);
                        mainScreen.setVisible(true);
                    } catch (UseCaseError useCaseError) {
                        String title = useCaseError.getTitleToDisplay();
                        String message = useCaseError.getMessageToDisplay();
                        JOptionPane.showMessageDialog(mainPanel, message, title, JOptionPane.ERROR_MESSAGE);
                    }


                }
                else{
                    JOptionPane.showMessageDialog(mainPanel, "Please ensure all fields have correct input!", "Add New Course Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }
}
