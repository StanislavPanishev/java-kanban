package task;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class SubtaskTest {


    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void instancesOfTheSubtaskAreEqualIfTheirIdIsEqual() {
        Subtask subtask1 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, 1);
        subtask1.setId(1);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2, "Экземпляры класса Subtask не равны друг другу");
    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком;
    @Test
    public void subtaskCannotBeMadeOwnEpic() {
        TaskManager taskManager = new InMemoryTaskManager();
        Subtask subtask1 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW, 1);
        subtask1.setId(1);
        Subtask subtask2 = taskManager.createSubtask(subtask1);
        Assertions.assertEquals(1, subtask2.getEpicID(), "Subtask можно сделать своим же эпиком");
    }

    @Test
    void getEpicID() {
    }

    @Test
    void setEpicID() {
    }
}