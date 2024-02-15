package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import java.util.List;


class InMemoryTaskManagerTest {

    //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    void shouldSaveTaskAndCanFindThemById() {
        TaskManager taskManager = new InMemoryTaskManager();
        final List<Task> tasks = taskManager.getTasksList();
        Task task1 = new Task("Спринт 3", "Повторить матерал", Status.NEW);
        taskManager.saveTask(task1);
        tasks.add(task1);
        Assertions.assertNotNull(taskManager, "Task не добавлен");
        Assertions.assertNotNull(taskManager.getTask(task1.getId()), "Task не найден по Id");
        Assertions.assertNotNull(task1, "Задача не найдена.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldSaveEpicAndCanFindThemById() {
        TaskManager taskManager = new InMemoryTaskManager();
        final List<Epic> epics = taskManager.getEpicsList();
        Epic epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW);
        taskManager.saveEpic(epic1);
        epics.add(epic1);
        Assertions.assertNotNull(taskManager, "Epic не добавлен");
        Assertions.assertNotNull(taskManager.getEpic(epic1.getId()), "Epic не найден по Id");
        Assertions.assertNotNull(epic1, "Задача не найдена.");
        Assertions.assertEquals(1, epics.size(), "Неверное количество задач.");
        Assertions.assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldSaveSubtaskAndCanFindThemByEpicID() {
        TaskManager taskManager = new InMemoryTaskManager();
        final List<Subtask> subtasks = taskManager.getSubtasksList();
        Subtask subtask1 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, 1);
        taskManager.saveSubtask(subtask1);
        subtasks.add(subtask1);
        Assertions.assertNotNull(taskManager, "Subtask не добавлен");
        Assertions.assertNotNull(subtask1, "Задача не найдена.");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }
}