package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.taskType = TaskType.EPIC;
    }

    public Epic(String title, String description, Status status, int id, List<Integer> subtasks) {
        super(title, description, status, id);
        this.subtasks = subtasks;
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id, TaskType taskType, String title, Status status, String description) {
        super(title, description, status, id);
        this.subtasks = subtasks;
        this.taskType = TaskType.EPIC;
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "\n" + "task.Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
