package manager;

import exception.ManagerSaveException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import task.*;

import java.io.*;

import static task.TaskType.*;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    private FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {

        File file = new File("src\\manager\\File");
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(file);

        FileBackedTaskManager fileBackedTasksManager1 = FileBackedTaskManager.loadFromFile(file);
        System.out.println(fileBackedTasksManager1);

        Task task1 = new Task("Спринт 3", "Повторить матерал", Status.NEW);
        fileBackedTasksManager.saveTask(task1);
        Task task2 = new Task("Спринт 4", "Выполнить ТЗ", Status.NEW);
        fileBackedTasksManager.saveTask(task2);
        Epic epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW);
        fileBackedTasksManager.saveEpic(epic1);
        Epic epic2 = new Epic("Важный эпик 2", "Переезд", Status.NEW);
        fileBackedTasksManager.saveEpic(epic2);
        Subtask subtask1 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, 1);
        fileBackedTasksManager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подготовка к переезду", "Покормить катус", Status.NEW, 1);
        fileBackedTasksManager.saveSubtask(subtask2);
        Subtask subtask3 = new Subtask("Переезд", "Собрать коробки", Status.NEW, 2);
        fileBackedTasksManager.saveSubtask(subtask3);

        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getTask(2);
        fileBackedTasksManager.getEpic(3);
        fileBackedTasksManager.getEpic(4);
        fileBackedTasksManager.getSubtask(5);
        fileBackedTasksManager.getSubtask(6);
        fileBackedTasksManager.getSubtask(7);

        task1.setStatus(Status.DONE);
        fileBackedTasksManager.updateTask(task1);

        task2.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.updateTask(task2);

        subtask1.setStatus(Status.DONE);
        fileBackedTasksManager.updateSubtask(subtask1);

        subtask2.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.updateSubtask(subtask2);

        subtask3.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.updateSubtask(subtask3);

        fileBackedTasksManager.updateEpic(epic1);
        fileBackedTasksManager.updateEpic(epic2);

        fileBackedTasksManager.printAll();

    }

    private void save() {
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(this.toString());
        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("id,type,title,status,description,epicId\n");
        for (Task task : super.getTasks().values()) {
            stringBuilder.append(String.format("%s,%s,%s,%s,%s\n", task.getId(), TASK, task.getTitle(),
                    task.getStatus(), task.getDescription()));
        }
        for (Epic epic : super.getEpics().values()) {
            stringBuilder.append(String.format("%s,%s,%s,%s,%s\n", epic.getId(), EPIC, epic.getTitle(),
                    epic.getStatus(), epic.getDescription()));
        }
        for (Subtask subtask : super.getSubtasks().values()) {
            stringBuilder.append(String.format("%s,%s,%s,%s,%s,%s\n", subtask.getId(), SUBTASK, subtask.getTitle(),
                    subtask.getStatus(), subtask.getDescription(), subtask.getEpicID()));
        }
        stringBuilder.append("\n");
        stringBuilder.append(historyToString(historyManager));
        return stringBuilder.toString();
    }

    private static Task fromString(String value) throws RuntimeException {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType taskType = TaskType.valueOf(values[1]);
        String title = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];

        switch (taskType) {
            case TASK:
                return new Task(id, taskType, title, status, description);
            case EPIC:
                return new Epic(id, taskType, title, status, description);
            case SUBTASK:
                return new Subtask(id, taskType, title, status, description, Integer.parseInt(values[5]));
            default:
                throw new RuntimeException("Тип задачи неопределен");
        }
    }

    private static String historyToString(HistoryManager historyManager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            stringBuilder.append(task.getId()).append(",");
        }
        if (stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    protected static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        List<String> listString;
        try {
            listString = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < listString.size(); i++) {
                String str = listString.get(i);
                if (str.isBlank()) {
                    break;
                }
                Task task = fromString(listString.get(i));
                if (task.getTaskType() == TASK) {
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                }
                if (task.getTaskType() == EPIC) {
                    Epic epic = (Epic) task;
                    fileBackedTaskManager.epics.put(epic.getId(), epic);
                }
                if (task.getTaskType() == SUBTASK) {
                    Subtask subtask = (Subtask) task;
                    fileBackedTaskManager.subtasks.put(subtask.getId(), subtask);
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public List<Task> getTasksList() {
        List<Task> task = super.getTasksList();
        save();
        return task;
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        ArrayList<Epic> epic = super.getEpicsList();
        save();
        return epic;
    }

    @Override
    public List<Subtask> getSubtasksList() {
        List<Subtask> subtask = super.getSubtasksList();
        save();
        return subtask;
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }

    @Override
    public Task getTask(int idNumber) {
        Task task = super.getTask(idNumber);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int idNumber) {
        Epic epic = super.getEpic(idNumber);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int idNumber) {
        Subtask subtask = super.getSubtask(idNumber);
        save();
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int idNumber) {
        super.deleteTask(idNumber);
        save();
    }

    @Override
    public void deleteEpic(int idNumber) {
        super.deleteEpic(idNumber);
        save();
    }

    @Override
    public void deleteSubtask(int idNumber) {
        super.deleteSubtask(idNumber);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

}



