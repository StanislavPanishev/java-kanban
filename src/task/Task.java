package task;

import java.util.Objects;

public class Task {
    protected int id;
    private String title;
    private String description;
    private Status status;
    protected TaskType taskType;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public Task(String title, String description, Status status, int id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public Task(int id, TaskType taskType, String title, Status status, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\n" + "task.Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId() && Objects.equals(getTitle(), task.getTitle()) && Objects.equals(getDescription(), task.getDescription()) && getStatus() == task.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getStatus());
    }
}
