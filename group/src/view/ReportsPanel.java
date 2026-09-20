package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {
    private DataStore dataStore;
    private JTextArea reportDisplay;

    public ReportsPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGroupReport = new JButton("Group Summary Report");
        JButton btnMemberReport = new JButton("Member Contribution Report");
        JButton btnTaskReport = new JButton("Task Status Report");

        btnGroupReport.addActionListener(e -> generateGroupReport());
        btnMemberReport.addActionListener(e -> generateMemberReport());
        btnTaskReport.addActionListener(e -> generateTaskReport());

        topPanel.add(btnGroupReport);
        topPanel.add(btnMemberReport);
        topPanel.add(btnTaskReport);

        add(topPanel, BorderLayout.NORTH);

        reportDisplay = new JTextArea();
        reportDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportDisplay.setEditable(false);
        add(new JScrollPane(reportDisplay), BorderLayout.CENTER);
    }

    private void generateGroupReport() {
        StringBuilder sb = new StringBuilder("=== GROUP SUMMARY REPORT ===\n\n");
        for (Group g : dataStore.getGroups()) {
            long total = dataStore.getResponsibilities().stream().filter(r -> r.getGroup().equals(g)).count();
            long completed = dataStore.getResponsibilities().stream()
                    .filter(r -> r.getGroup().equals(g) && r.getStatus() == Responsibility.Status.COMPLETED).count();
            double progress = total == 0 ? 0 : ((double) completed / total) * 100;

            sb.append(String.format(
                    "Group: %s\nMembers: %d\nTotal Tasks: %d | Completed: %d\nProgress: %.2f%%\n-------------------------------\n",
                    g.getName(), g.getMembers().size(), total, completed, progress));
        }
        reportDisplay.setText(sb.toString());
    }

    private void generateMemberReport() {
        StringBuilder sb = new StringBuilder("=== MEMBER CONTRIBUTION REPORT ===\n\n");
        for (Student s : dataStore.getStudents()) {
            long totalAssigned = dataStore.getResponsibilities().stream().filter(r -> s.equals(r.getAssignedMember()))
                    .count();
            long completed = dataStore.getResponsibilities().stream()
                    .filter(r -> s.equals(r.getAssignedMember()) && r.getStatus() == Responsibility.Status.COMPLETED)
                    .count();
            double contribution = totalAssigned == 0 ? 0 : ((double) completed / totalAssigned) * 100;

            sb.append(String.format(
                    "Student: %s (%s)\nAssigned Tasks: %d | Completed: %d\nContribution Score: %.2f%%\n-------------------------------\n",
                    s.getName(), s.getId(), totalAssigned, completed, contribution));
        }
        reportDisplay.setText(sb.toString());
    }

    private void generateTaskReport() {
        StringBuilder sb = new StringBuilder("=== TASK STATUS REPORT ===\n\n");
        sb.append(
                String.format("%-10s %-20s %-15s %-15s %-12s\n", "Task ID", "Title", "Group", "Assigned To", "Status"));
        sb.append("=========================================================================\n");
        for (Responsibility r : dataStore.getResponsibilities()) {
            sb.append(String.format("%-10s %-20s %-15s %-15s %-12s\n",
                    r.getId(), r.getTitle(), r.getGroup().getName(),
                    r.getAssignedMember() != null ? r.getAssignedMember().getName() : "None",
                    r.getStatus()));
        }
        reportDisplay.setText(sb.toString());
    }
}