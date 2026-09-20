package view;

import model.DataStore;
import model.Group;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentsGroupsPanel extends JPanel {
    private DataStore dataStore;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JList<Group> groupList;
    private DefaultListModel<Group> groupListModel;
    private JTextArea txtGroupDesc;

    public StudentsGroupsPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        setLayout(new BorderLayout(10, 10));

        // JSplitPane dividing Students and Groups
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createStudentPanel(), createGroupPanel());
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        refreshData();
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Students Management"));

        studentTableModel = new DefaultTableModel(new String[] { "ID", "Name", "Email", "Group" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable table cells
            }
        };
        studentTable = new JTable(studentTableModel);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add Student");
        JButton btnDelete = new JButton("Delete Student");
        JButton btnAssignGroup = new JButton("Assign Selected Student to Group");

        btnAdd.addActionListener(e -> {
            StudentDialog dialog = new StudentDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Student",
                    null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Student s = new Student(dialog.getStudentId(), dialog.getStudentName(), dialog.getStudentEmail());
                dataStore.getStudents().add(s);
                refreshData();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                String id = (String) studentTableModel.getValueAt(row, 0);

                // Remove student from group if assigned
                for (Group g : dataStore.getGroups()) {
                    g.getMembers().removeIf(s -> s.getId().equals(id));
                }

                dataStore.getStudents().removeIf(s -> s.getId().equals(id));
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Select a student to delete.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAssignGroup.addActionListener(e -> assignStudentToSelectedGroup());

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnAssignGroup);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGroupPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Groups Management"));

        groupListModel = new DefaultListModel<>();
        groupList = new JList<>(groupListModel);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        txtGroupDesc = new JTextArea(4, 20);
        txtGroupDesc.setEditable(false);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(new JScrollPane(groupList), BorderLayout.CENTER);
        topPanel.add(new JScrollPane(txtGroupDesc), BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.CENTER);

        groupList.addListSelectionListener(e -> updateGroupDetails());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAddGroup = new JButton("Create Group");

        btnAddGroup.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Group Name:");
            String desc = JOptionPane.showInputDialog(this, "Enter Group Description:");
            if (name != null && !name.trim().isEmpty()) {
                Group g = new Group("GRP" + (dataStore.getGroups().size() + 1), name.trim(),
                        desc != null ? desc.trim() : "");
                dataStore.getGroups().add(g);
                refreshData();
            }
        });

        btnPanel.add(btnAddGroup);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void assignStudentToSelectedGroup() {
        int selectedRow = studentTable.getSelectedRow();
        Group selectedGroup = groupList.getSelectedValue();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table first.", "Selection Missing",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedGroup == null) {
            JOptionPane.showMessageDialog(this, "Please select a group from the list on the right.",
                    "Selection Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) studentTableModel.getValueAt(selectedRow, 0);
        Student student = dataStore.getStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst()
                .orElse(null);

        if (student != null) {
            // Remove from any existing group first (since a student belongs to only one
            // group at a time)
            for (Group g : dataStore.getGroups()) {
                g.removeMember(student);
            }

            // Add to new group
            selectedGroup.addMember(student);

            JOptionPane.showMessageDialog(this,
                    student.getName() + " has been assigned to " + selectedGroup.getName() + "!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshData();
        }
    }

    private void updateGroupDetails() {
        Group selected = groupList.getSelectedValue();
        if (selected != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Description: ").append(selected.getDescription()).append("\n");
            sb.append("Members (").append(selected.getMembers().size()).append("):\n");
            if (selected.getMembers().isEmpty()) {
                sb.append(" - No members assigned");
            } else {
                for (Student m : selected.getMembers()) {
                    sb.append(" - ").append(m.getName()).append(" (").append(m.getId()).append(")\n");
                }
            }
            txtGroupDesc.setText(sb.toString());
        } else {
            txtGroupDesc.setText("");
        }
    }

    public void refreshData() {
        // Refresh Students Table
        studentTableModel.setRowCount(0);
        for (Student s : dataStore.getStudents()) {
            String gName = "Unassigned";
            for (Group g : dataStore.getGroups()) {
                if (g.getId().equals(s.getGroupId())) {
                    gName = g.getName();
                    break;
                }
            }
            studentTableModel.addRow(new Object[] { s.getId(), s.getName(), s.getEmail(), gName });
        }

        // Refresh Group List
        groupListModel.clear();
        for (Group g : dataStore.getGroups()) {
            groupListModel.addElement(g);
        }

        updateGroupDetails();
    }
}