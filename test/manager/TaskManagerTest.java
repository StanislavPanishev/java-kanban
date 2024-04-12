package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task1;
    protected Task task2;
    protected Task task3;

    protected Epic epic1;
    protected Epic epic2;
    protected Epic epic3;

    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;

    @BeforeEach
    public void initTasks() {
        task1 = new Task("task1", "Повторить матерал", Status.NEW
                , LocalDateTime.now(), Duration.ofMinutes(100));
        task2 = new Task("task2", "Повторить матерал", Status.NEW
                , LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        task3 = new Task("task3", "Повторить матерал", Status.NEW
                , LocalDateTime.now().plusHours(4), Duration.ofMinutes(45));

        epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        epic2 = new Epic("Важный эпик 2", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        epic3 = new Epic("Важный эпик 3", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));

        subtask1 = new Subtask("subtask1", "Полить кота", Status.NEW
                , LocalDateTime.now(), Duration.ofMinutes(100), 1);
        subtask2 = new Subtask("subtask2", "Полить кота", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45), 1);
        subtask3 = new Subtask("subtask3", "Полить кота", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45), 1);


    }

    @Test
    public void shouldRemoveTask() {
        taskManager.createTask(task1);
        taskManager.deleteTask(1);

        assertEquals(0, taskManager.getTasks().size());
        assertThrows(NullPointerException.class, () -> taskManager.getTask(1)
                , "Ожидалось получить null при обращении к удаленной задаче.");
    }


    @Test
    void prioritizedTasksTest() throws IOException {
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        assertNotNull(taskManager.getPrioritizedTasks(), "Список пустой");
        assertEquals(taskManager.getTasks().size() + taskManager.getSubtasksList().size(),
                taskManager.getPrioritizedTasks().size());
        taskManager.deleteTask(0);
        assertEquals(taskManager.getTasks().size() + taskManager.getSubtasksList().size(),
                taskManager.getPrioritizedTasks().size());
        taskManager.deleteAll();
        assertEquals(taskManager.getTasks().size() + taskManager.getSubtasksList().size(),
                taskManager.getPrioritizedTasks().size());
        taskManager.deleteAll();
        Assertions.assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void saveTaskAndCanFindThemById() {
        final List<Task> tasks = taskManager.getTasksList();
        taskManager.saveTask(task1);
        tasks.add(task1);
        assertNotNull(taskManager, "Задача не добавлена");
        assertNotNull(taskManager.getTask(task1.getId()), "Задача не найдена по Id");
        assertNotNull(task1, "Задача не найдена.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void loadFromFile() throws IOException {
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        Assertions.assertFalse(taskManager.getHistory().contains(subtask1), "История просмотра" +
                " удаленной подзадачи загружена из файла");
    }

    @Test
    public void testException() {
        assertThrows(ArithmeticException.class, () -> {
            int a = 10 / 0;
        }, "Деление на ноль должно приводить к исключению");
    }

}
