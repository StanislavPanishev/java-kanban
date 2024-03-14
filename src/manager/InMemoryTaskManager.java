package manager;

import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private static int idNumber = 0;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int id(Task task) {
        task.setId(++idNumber);
        return idNumber;
    }

    @Override
    public void saveTask(Task task) {
        tasks.put(id(task), task);
    }

    @Override
    public void saveEpic(Epic epic) {
        epics.put(id(epic), epic);
        changeEpicStatus(epic);
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        subtasks.put(id(subtask), subtask);
        int epicIdOfSubTask = subtask.getEpicID();
        Epic epic = epics.get(epicIdOfSubTask);
        if (epic != null) {
            changeEpicStatus(epic);
        }
    }

    // 2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // a. Получение списка всех задач.

    @Override

    public List<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //  b. Удаление всех задач.
    @Override
    public void deleteTask() {
        tasks.clear();
    }

    @Override
    public void deleteEpic() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtask() {
        for (Integer sub : subtasks.keySet()) {
            Subtask subtask = subtasks.get(sub);
            if (subtask != null) {
                Epic epic = epics.get(subtask.getEpicID());
                if (epic != null) {
                    epic.getSubtasks().clear();
                    changeEpicStatus(epic);
                }
            }
        }
        subtasks.clear();
    }

    // c. Получение по идентификатору.
    @Override
    public Task getTask(int idNumber) {
        Task task = tasks.get(idNumber);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int idNumber) {
        Epic epic = epics.get(idNumber);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int idNumber) {
        Subtask subtask = subtasks.get(idNumber);
        historyManager.add(subtask);
        return subtask;
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public Task createTask(Task task) {
        return new Task(task.getTitle(), task.getDescription(), task.getStatus());
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        return new Subtask(subtask.getTitle(), subtask.getDescription(), subtask.getStatus(), subtask.getEpicID());
    }

    @Override
    public Epic createEpic(Epic epic) {
        return new Epic(epic.getTitle(), epic.getDescription(), epic.getStatus());
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int idUpdatedSubTask = subtask.getId();
        if (subtasks.containsKey(idUpdatedSubTask)) {
            subtasks.put(idUpdatedSubTask, subtask);
        }
        int epicIdForStatus = subtask.getEpicID();
        Epic epic = epics.get(epicIdForStatus);
        if (epic != null) {
            changeEpicStatus(epic);
        }
    }

    @Override
    public void updateEpic(Epic epic) {

        int idUpdatedEpic = epic.getId();
        Status currentEpicStatus = epics.get(idUpdatedEpic).getStatus();
        Epic newEpic = new Epic(epic.getTitle(), epic.getDescription(), currentEpicStatus, epic.getId(), epic.getSubtasks());
        if (epics.containsKey(idUpdatedEpic)) {
            epics.put(idUpdatedEpic, newEpic);
            changeEpicStatus(newEpic);
        }
    }

    //  f. Удаление по идентификатору.
    @Override
    public void deleteTask(int idNumber) {
        tasks.remove(idNumber);
        historyManager.remove(idNumber);
    }

    @Override
    public void deleteEpic(int idNumber) {
        epics.remove(idNumber);
        historyManager.remove(idNumber);
    }

    @Override
    public void deleteSubtask(int idNumber) {
        subtasks.remove(idNumber);
        historyManager.remove(idNumber);
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();

    }

    // 3. Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Subtask> subtaskList(int idNumber) {
        ArrayList<Subtask> listSubtasks = new ArrayList<>();
        for (int subtaskNumber : subtasks.keySet()) {
            Subtask subtask = subtasks.get(subtaskNumber);
            if (subtask != null && idNumber == subtask.getEpicID()) {
                listSubtasks.add(subtask);
            }
        }
        return listSubtasks;
    }

    @Override
    public void changeEpicStatus(Epic epic) {
        int epicID = epic.getId();
        ArrayList<Subtask> updatedListOfSubtasks = subtaskList(epicID);
        int doneCounter = 0;
        int newCounter = 0;
        for (Subtask subtask : updatedListOfSubtasks) {
            switch (subtask.getStatus()) {
                case NEW:
                    newCounter++;
                    break;
                case IN_PROGRESS:
                    break;
                case DONE:
                    doneCounter++;
                    break;
            }
        }
        if ((updatedListOfSubtasks.isEmpty()) || (newCounter == updatedListOfSubtasks.size())) {
            epic.setStatus(Status.NEW);
        } else if (doneCounter == updatedListOfSubtasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void printAll() {
        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }

    @Override
    public int getIdNumber() {
        return idNumber;
    }

    @Override
    public void setIdNumber(int idNumber) {
        InMemoryTaskManager.idNumber = idNumber;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}


