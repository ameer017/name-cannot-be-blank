package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a project group.
 */
public class Group {
    private String id;
    private String name;
    private String description;
    private List<Student> members;

    public Group(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getMembers() {
        return members;
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            student.setGroupId(this.id);
        }
    }

    public void removeMember(Student student) {
        if (members.remove(student)) {
            student.setGroupId(null);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}