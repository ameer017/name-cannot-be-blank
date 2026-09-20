package view;

import model.DataStore;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private DataStore dataStore;
    private JTabbedPane tabbedPane;
    private StudentsGroupsPanel studentsGroupsPanel;
    private TasksPanel tasksPanel;
    private ReportsPanel reportsPanel;

    public MainFrame() {
        setTitle("Group Project Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dataStore = new DataStore();

        // Setup MenuBar
        setupMenuBar();

        // JTabbedPane Navigation
        tabbedPane = new JTabbedPane();
        studentsGroupsPanel = new StudentsGroupsPanel(dataStore);
        tasksPanel = new TasksPanel(dataStore);
        reportsPanel = new ReportsPanel(dataStore);

        tabbedPane.addTab("Students & Groups", studentsGroupsPanel);
        tabbedPane.addTab("Tasks & Assignment", tasksPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        tabbedPane.addChangeListener(e -> {
            studentsGroupsPanel.refreshData();
            tasksPanel.refreshData();
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Group Project Management System v1.0\nBuilt with Java Swing & AWT (MVC Architecture)",
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
}