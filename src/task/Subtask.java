package task;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicID = epicID;
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
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}