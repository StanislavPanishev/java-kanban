import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(String title, String description,  Status status, int id, List<Integer> subtasks) {
        super(title, description, status, id);
        this.subtasks = subtasks;
    }
    public void addIdOfSubtasks(Subtask subtask) {
        subtasks.add(subtask.getId());
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
        return "\n" + "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
