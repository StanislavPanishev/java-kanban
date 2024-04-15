package task;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Duration;


public class Epic extends Task {
    private List<Integer> subtasks = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        this.taskType = TaskType.EPIC;
    }

    public Epic(String title, String description, Status status, int id, List<Integer> subtasks, LocalDateTime startTime, Duration duration) {
        super(title, description, status, id, startTime, duration);
        this.subtasks = subtasks;
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id, TaskType taskType, String title, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(title, description, status, id, startTime, duration);
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
                ", startTime=" + startTime.format(formatter) +
                ", duration=" + duration +
                '}';
    }
}
