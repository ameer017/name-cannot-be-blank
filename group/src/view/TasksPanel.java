package view;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TasksPanel extends JPanel {
    private DataStore dataStore;
    private JTable taskTable;
    private DefaultTableModel taskTableModel;
    private JComboBox<Group> cmbGroups;
    private JComboBox<Student> cmbMembers;
    private JComboBox<Responsibility.Status> cmbStatus;

    public TasksPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        setLayout(new BorderLayout(10, 10));

        // Filter / Controls Panel using GridBagLayout
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setBorder(BorderFactory.createTitledBorder("Assign & Manage Tasks"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbGroups = new JComboBox<>();
        cmbMembers = new JComboBox<>();

        // Add "All" option alongside status values for filtering
        cmbStatus = new JComboBox<>(Responsibility.Status.values());

        // Row 0: Group and Member selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlsPanel.add(new JLabel("Select Group:"), gbc);
        gbc.gridx = 1;
        controlsPanel.add(cmbGroups, gbc);
        gbc.gridx = 2;
        controlsPanel.add(new JLabel("Assigned Member:"), gbc);
        gbc.gridx = 3;
        controlsPanel.add(cmbMembers, gbc);

        // Row 1: Initial Status for Task Creation
        gbc.gridx = 0;
        gbc.gridy = 1;
        controlsPanel.add(new JLabel("Initial Status:"), gbc);
        gbc.gridx = 1;
        controlsPanel.add(cmbStatus, gbc);

        cmbGroups.addActionListener(e -> updateMembersCombo());

        add(controlsPanel, BorderLayout.NORTH);

        // Task Table
        taskTableModel = new DefaultTableModel(
                new String[] { "ID", "Title", "Group", "Assigned To", "Deadline", "Status" }, 0);
        taskTable = new JTable(taskTableModel);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddTask = new JButton("Create Task");
        JButton btnUpdateStatus = new JButton("Update Status");

        btnAddTask.addActionListener(e -> createTask());
        btnUpdateStatus.addActionListener(e -> updateTaskStatus());

        actionPanel.add(btnAddTask);
        actionPanel.add(btnUpdateStatus);
        add(actionPanel, BorderLayout.SOUTH);

        refreshData();
    }

    private void updateMembersCombo() {
        cmbMembers.removeAllItems();
        Group sel = (Group) cmbGroups.getSelectedItem();
        if (sel != null) {
            for (Student s : sel.getMembers()) {
                cmbMembers.addItem(s);
            }
        }
    }

    private void createTask() {
        Group selectedGroup = (Group) cmbGroups.getSelectedItem();
        Student selectedStudent = (Student) cmbMembers.getSelectedItem();
        Responsibility.Status selectedStatus = (Responsibility.Status) cmbStatus.getSelectedItem();

        if (selectedGroup == null || selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Please select a group and an assigned member.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String title = JOptionPane.showInputDialog(this, "Task Title:");
        String desc = JOptionPane.showInputDialog(this, "Task Description:");
        String deadline = JOptionPane.showInputDialog(this, "Deadline (YYYY-MM-DD):");

        if (title != null && !title.trim().isEmpty()) {
            Responsibility task = new Responsibility("TSK" + (dataStore.getResponsibilities().size() + 1), title, desc,
                    deadline, selectedGroup, selectedStudent);

            // Set status selected from cmbStatus
            if (selectedStatus != null) {
                task.setStatus(selectedStatus);
            }

            dataStore.getResponsibilities().add(task);
            refreshData();
        }
    }

    private void updateTaskStatus() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a task to update.");
            return;
        }

        String taskId = (String) taskTableModel.getValueAt(row, 0);
        Responsibility task = dataStore.getResponsibilities().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElse(null);

        if (task != null) {
            Responsibility.Status newStatus = (Responsibility.Status) JOptionPane.showInputDialog(
                    this, "Select New Status:", "Update Status",
                    JOptionPane.QUESTION_MESSAGE, null, Responsibility.Status.values(), task.getStatus());
            if (newStatus != null) {
                task.setStatus(newStatus);
                refreshData();
            }
        }
    }

    public void refreshData() {
        cmbGroups.removeAllItems();
        for (Group g : dataStore.getGroups()) {
            cmbGroups.addItem(g);
        }
        updateMembersCombo();

        taskTableModel.setRowCount(0);
        for (Responsibility r : dataStore.getResponsibilities()) {
            taskTableModel.addRow(new Object[] {
                    r.getId(),
                    r.getTitle(),
                    r.getGroup().getName(),
                    r.getAssignedMember() != null ? r.getAssignedMember().getName() : "Unassigned",
                    r.getDeadline(),
                    r.getStatus()
            });
        }
    }
}