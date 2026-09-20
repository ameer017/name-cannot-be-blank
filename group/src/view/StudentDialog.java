package view;

import model.Student;
import javax.swing.*;
import java.awt.*;

/**
 * Modal Dialog for Adding or Editing Student Records.
 */
public class StudentDialog extends JDialog {
    private JTextField txtId = new JTextField(15);
    private JTextField txtName = new JTextField(15);
    private JTextField txtEmail = new JTextField(15);
    private boolean confirmed = false;

    public StudentDialog(Frame owner, String title, Student student) {
        super(owner, title, true);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        if (student != null) {
            txtId.setText(student.getId());
            txtId.setEditable(false);
            txtName.setText(student.getName());
            txtEmail.setText(student.getEmail());
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmed = true;
            setVisible(false);
        });

        btnCancel.addActionListener(e -> setVisible(false));

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getStudentId() {
        return txtId.getText().trim();
    }

    public String getStudentName() {
        return txtName.getText().trim();
    }

    public String getStudentEmail() {
        return txtEmail.getText().trim();
    }
}