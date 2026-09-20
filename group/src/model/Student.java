package model;

/**
 * This class serves as a data model containing a student details (ID, name,
 * email)
 * and tracks the student's assigned project group membership state.
 */
public class Student {

    private String id;

    private String name;

    private String email;

    private String groupId; // null by default until assigned to a group

    public Student(String id, String name, String email) {

        this.id = id;

        this.name = name;

        this.email = email;

        this.groupId = null;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override

    public String toString() {

        return name + " (" + id + ")";

    }

}