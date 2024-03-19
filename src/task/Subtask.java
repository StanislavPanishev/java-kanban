package task;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicID = epicID;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, TaskType taskType, String title, Status status, String description, int epicID) {
        super(title, description, status, id);
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
                '}';
    }
}
