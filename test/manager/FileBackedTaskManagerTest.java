package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    void beforeEach() throws IOException {
        File file = File.createTempFile("temp", "temp");
        taskManager = FileBackedTaskManager.loadFromFile(file);
        initTasks();

    }

    @Test
    public void shouldLoadFromFile() throws IOException {
        taskManager.createTask(task1);
        Epic epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, LocalDateTime.now().plusHours(3), Duration.ofMinutes(60), 1);
        taskManager.createSubtask(subtask1);
        Assertions.assertFalse(taskManager.getHistory().contains(subtask1), "История просмотра" +
                " удаленной подзадачи загружена из файла");
    }
}