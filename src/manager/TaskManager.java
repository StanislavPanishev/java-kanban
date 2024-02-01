package manager;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public int idNumber = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int id(Task task) {
        task.setId(++idNumber);
        return idNumber;
    }

    public void saveTask(Task task) {
        tasks.put(id(task), task);
    }

    public void saveEpic(Epic epic) {
        epics.put(id(epic), epic);
        changeEpicStatus(epic);
    }

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

    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //  b. Удаление всех задач.
    public void deleteTask() {
        tasks.clear();
    }

    public void deleteEpic() {
        epics.clear();
        subtasks.clear();
    }

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
    public Task getTask(int idNumber) {
        return tasks.get(idNumber);
    }

    public Epic getEpic(int idNumber) {
        return epics.get(idNumber);
    }

    public Subtask getSubtask(int idNumber) {
        return subtasks.get(idNumber);
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    public Task createTask(Task task) {
        return new Task(task.getTitle(), task.getDescription(), task.getStatus());
    }

    public Subtask createSubtask(Subtask subtask) {
        return new Subtask(subtask.getTitle(), subtask.getDescription(), subtask.getStatus(), subtask.getEpicID());
    }

    public Epic createEpic(Epic epic) {
        return new Epic(epic.getTitle(), epic.getDescription(), epic.getStatus());
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

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
    public void deleteTask(int idNumber) {
        tasks.remove(idNumber);
    }

    public void deleteEpic(int idNumber) {
        epics.remove(idNumber);
    }

    public void deleteSubtask(int idNumber) {
        subtasks.remove(idNumber);
    }

    public void deleteAll() {
        deleteTask();
        deleteEpic();
        deleteSubtask();
    }

    // 3. Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
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

    private void changeEpicStatus(Epic epic) {
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

    public void printAll() {
        System.out.println(getTasksList());
        System.out.println(getEpicsList());
        System.out.println(getSubtasksList());
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}


