package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int tenTasks = 10;

    private final List<Task> browsingHistory;

    public InMemoryHistoryManager() {
        browsingHistory = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }

    public void add(Task task) {
        if (browsingHistory.size() == tenTasks) {
            browsingHistory.remove(0);
        }
        browsingHistory.add(task);
    }
}
