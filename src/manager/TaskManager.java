package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    List<Task> getHistory();

    int id(Task task);

    void saveTask(Task task);

    void saveEpic(Epic epic);

    void saveSubtask(Subtask subtask);

    List<Task> getTasksList();

    ArrayList<Epic> getEpicsList();

    List<Subtask> getSubtasksList();

    //  b. Удаление всех задач.
    void deleteTask();

    void deleteEpic();

    void deleteSubtask();

    // c. Получение по идентификатору.
    Task getTask(int idNumber);

    Epic getEpic(int idNumber);

    Subtask getSubtask(int idNumber);

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    Task createTask(Task task);

    Subtask createSubtask(Subtask subtask);

    Epic createEpic(Epic epic);

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    //  f. Удаление по идентификатору.
    void deleteTask(int idNumber);

    void deleteEpic(int idNumber);

    void deleteSubtask(int idNumber);

    void deleteAll();

    // 3. Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    ArrayList<Subtask> subtaskList(int idNumber);

    void changeEpicStatus(Epic epic);

    void printAll();

    int getIdNumber();

    void setIdNumber(int idNumber);

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();

    List<Task> getPrioritizedTasks();
}
