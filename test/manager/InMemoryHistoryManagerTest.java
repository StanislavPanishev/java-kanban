package manager;

import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void addTaskSaveThePreviousVersionAndData() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void getRidOfRepeatViewsInTheHistory() {
        TaskManager historyManager = new InMemoryTaskManager();
        Task task1 = new Task("Test addNewTask1", "Test addNewTask1 description", Status.NEW);
        Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description", Status.NEW);
        historyManager.saveTask(task1);
        historyManager.saveTask(task2);
        historyManager.getTask(task1.getId());
        historyManager.getTask(task2.getId());
        historyManager.getTask(task1.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Неправильный размер истории просмотров");
        assertEquals(task1, history.get(1), "Не отображается последний просмотр");
    }

    @Test
    void removeTaskFromTheHistory() {
        TaskManager historyManager = new InMemoryTaskManager();
        Task task1 = new Task("Test addNewTask1", "Test addNewTask1 description", Status.NEW);
        historyManager.saveTask(task1);
        historyManager.getTask(task1.getId());
        historyManager.deleteTask(task1.getId());
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
    }
}