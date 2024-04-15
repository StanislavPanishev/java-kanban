package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicID;


    public Subtask(String title, String description, Status status, LocalDateTime localDateTime, Duration duration, int epicID) {
        super(title, description, status, localDateTime, duration);
        this.epicID = epicID;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, TaskType taskType, String title, Status status, String description, LocalDateTime localDateTime, Duration duration, int epicID) {
        super(title, description, status, id, localDateTime, duration);
        this.epicID = epicID;
        this.taskType = TaskType.SUBTASK;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "\n" + "task.Subtask{" +
                "epicID=" + epicID +
                ", id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + startTime.format(formatter) +
                ", duration=" + duration +
                '}';
    }
}
