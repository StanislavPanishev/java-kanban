package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.*;

class FileBackedTaskManagerTest {
    @Test
    public void shouldLoadFromFile() throws IOException {
        File file = File.createTempFile("temp", "temp");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        Task task1 = new Task("Спринт 3", "Повторить матерал", Status.NEW);
        fileBackedTaskManager.createTask(task1);
        Epic epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW);
        fileBackedTaskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, 1);
        fileBackedTaskManager.createSubtask(subtask1);
        Assertions.assertFalse(fileBackedTaskManager.getHistory().contains(subtask1), "История просмотра" +
                " удаленной подзадачи загружена из файла");
    }
}