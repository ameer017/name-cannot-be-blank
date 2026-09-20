package controller;

import view.MainFrame;
import javax.swing.SwingUtilities;

public class AppController {
    public void start() {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}