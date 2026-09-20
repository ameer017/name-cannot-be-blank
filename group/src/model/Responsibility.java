package model;

/**
 * Represents a task assigned within a group.
 */
public class Responsibility {
    public enum Status {
        NOT_STARTED("Not Started"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        BLOCKED("Blocked");

        private final String label;

        Status(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private String id;
    private String title;
    private String description;
    private String deadline;
    private Status status;
    private Group group; // Group getting the task
    private Student assignedMember;

    public Responsibility(String id, String title, String description, String deadline, Group group,
            Student assignedMember) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = Status.NOT_STARTED;
        this.group = group;
        this.assignedMember = assignedMember;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Student getAssignedMember() {
        return assignedMember;
    }

    public void setAssignedMember(Student assignedMember) {
        this.assignedMember = assignedMember;
    }
}