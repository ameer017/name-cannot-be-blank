import model.DataStore;
import model.Responsibility;
import view.MainFrame;
import view.ReportsPanel;
import view.StudentDialog;
import view.StudentsGroupsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Off-screen capture helper for the assignment report. Not part of the application.
 */
public class CaptureScreenshots {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Path out = Path.of(args.length > 0 ? args[0] : "report/screenshots");
        Files.createDirectories(out);

        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(980, 640);
        frame.setLocation(40, 40);

        // Complete one seed task so reports show non-zero progress.
        DataStore store = (DataStore) field(frame, "dataStore");
        if (!store.getResponsibilities().isEmpty()) {
            store.getResponsibilities().get(0).setStatus(Responsibility.Status.COMPLETED);
        }

        frame.setVisible(true);
        frame.toFront();
        sleep(700);

        JTabbedPane tabs = (JTabbedPane) field(frame, "tabbedPane");
        StudentsGroupsPanel studentsPanel = (StudentsGroupsPanel) field(frame, "studentsGroupsPanel");
        ReportsPanel reportsPanel = (ReportsPanel) field(frame, "reportsPanel");

        JList<?> groupList = (JList<?>) field(studentsPanel, "groupList");
        if (groupList.getModel().getSize() > 0) {
            groupList.setSelectedIndex(0);
        }
        sleep(250);
        capture(frame, out.resolve("01-students-groups.png"));

        StudentDialog dialog = new StudentDialog(frame, "Add Student", null);
        dialog.setModal(false);
        dialog.setVisible(true);
        sleep(250);
        captureWindow(dialog, out.resolve("02-add-student-dialog.png"));
        dialog.dispose();

        tabs.setSelectedIndex(1);
        sleep(350);
        capture(frame, out.resolve("03-tasks.png"));

        tabs.setSelectedIndex(2);
        invoke(reportsPanel, "generateGroupReport");
        sleep(250);
        capture(frame, out.resolve("04-group-summary-report.png"));

        invoke(reportsPanel, "generateMemberReport");
        sleep(250);
        capture(frame, out.resolve("05-member-contribution-report.png"));

        invoke(reportsPanel, "generateTaskReport");
        sleep(250);
        capture(frame, out.resolve("06-task-status-report.png"));

        frame.dispose();
        System.out.println("Wrote screenshots to " + out.toAbsolutePath());
        System.exit(0);
    }

    private static void capture(Window window, Path path) throws Exception {
        captureWindow(window, path);
    }

    private static void captureWindow(Window window, Path path) throws Exception {
        window.repaint();
        sleep(120);
        int w = Math.max(window.getWidth(), 1);
        int h = Math.max(window.getHeight(), 1);
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        window.paint(g);
        g.dispose();
        ImageIO.write(image, "png", path.toFile());
        System.out.println("Saved " + path.getFileName() + " (" + w + "x" + h + ")");
    }

    private static Object field(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void invoke(Object target, String name) throws Exception {
        Method method = target.getClass().getDeclaredMethod(name);
        method.setAccessible(true);
        method.invoke(target);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
