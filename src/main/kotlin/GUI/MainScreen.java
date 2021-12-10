package GUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MainScreen extends JFrame{
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

        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    public static void main(String[] args){
        MainScreen main = new MainScreen();
        main.setVisible(true);
    }
}

