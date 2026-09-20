package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized In-Memory Storage for application state.
 */
public class DataStore {
    private List<Student> students = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Responsibility> responsibilities = new ArrayList<>();

    public DataStore() {
        // Seed initial data for testing
        Student s1 = new Student("STU001", "IBRAHIM Amiruddeen", "ibrahimamiruddeen4245@gmail.com");
        Student s2 = new Student("STU002", "DANIEL OTU Courage Daniel", "otucourage969@gmail.com");
        Student s3 = new Student("STU003", "GO'AR Ritmunu Miracle", "miraclemusa03@gmail.com");
        students.add(s1);
        students.add(s2);
        students.add(s3);

        Group g1 = new Group("GRP001", "Alpha Team", "AI Automation Project");
        g1.addMember(s1);
        g1.addMember(s2);
        groups.add(g1);

        responsibilities
                .add(new Responsibility("TSK001", "Design Database", "Create ERD and schema", "2026-10-05", g1, s1));
        responsibilities
                .add(new Responsibility("TSK002", "Develop GUI", "Implement Swing interface", "2026-10-12", g1, s2));
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Responsibility> getResponsibilities() {
        return responsibilities;
    }
}