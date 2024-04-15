import manager.*;
import task.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Спринт 3", "Повторить матерал", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(15));
        taskManager.saveTask(task1);
        Task task2 = new Task("Спринт 4", "Выполнить ТЗ", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        taskManager.saveTask(task2);
        Epic epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(6), Duration.ofMinutes(120));
        taskManager.saveEpic(epic1);
        Epic epic2 = new Epic("Важный эпик 2", "Переезд", Status.NEW,
                LocalDateTime.now().plusHours(8), Duration.ofMinutes(60));
        taskManager.saveEpic(epic2);
        Subtask subtask1 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, LocalDateTime.now().plusHours(12), Duration.ofMinutes(60), 1);
        taskManager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подготовка к переезду", "Покормить катус", Status.NEW, LocalDateTime.now().plusHours(24), Duration.ofMinutes(60), 1);
        taskManager.saveSubtask(subtask2);
        Subtask subtask3 = new Subtask("Переезд", "Собрать коробки", Status.NEW, LocalDateTime.now().plusHours(48), Duration.ofMinutes(60), 2);
        taskManager.saveSubtask(subtask3);

        System.out.println("c. Получение по идентификатору.");
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getEpic(4));
        System.out.println(taskManager.getSubtask(5));
        System.out.println(taskManager.getSubtask(6));
        System.out.println(taskManager.getSubtask(7));

        System.out.println("e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.");

        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);

        task2.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task2);

        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);

        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);

        taskManager.updateEpic(epic1);
        taskManager.updateEpic(epic2);

        taskManager.printAll();

        taskManager.deleteAll();

        System.out.println("d. Создание. Сам объект должен передаваться в качестве параметра.");
        Task newTask1 = taskManager.createTask(task1);
        taskManager.saveTask(newTask1);
        Task newTask2 = taskManager.createTask(task2);
        taskManager.saveTask(newTask2);

        Epic newEpic1 = taskManager.createEpic(epic1);
        taskManager.saveEpic(newEpic1);
        Epic newEpic2 = taskManager.createEpic(epic2);
        taskManager.saveEpic(newEpic2);

        Subtask newSubtask1 = taskManager.createSubtask(subtask1);
        taskManager.saveSubtask(newSubtask1);
        Subtask newSubtask2 = taskManager.createSubtask(subtask2);
        taskManager.saveSubtask(newSubtask2);
        Subtask newSubtask3 = taskManager.createSubtask(subtask3);
        taskManager.saveSubtask(newSubtask3);

        taskManager.printAll();

        System.out.println("a. Получение списка всех подзадач определённого эпика.");
        System.out.println(taskManager.subtaskList(1));

        System.out.println("f. Удаление по идентификатору.");
        taskManager.deleteTask(1);
        taskManager.deleteTask(2);
        taskManager.deleteEpic(3);

        taskManager.printAll();

        System.out.println("Выводим список задач в порядке приоритета");
        System.out.println(taskManager.getPrioritizedTasks());

    }
}
