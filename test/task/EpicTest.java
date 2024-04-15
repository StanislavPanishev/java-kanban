package task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void instancesOfTheEpicAreEqualIfTheirIdIsEqual() {
        Epic epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        Epic epic2 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1, epic2, "Экземпляры класса Epic не равны друг другу");
    }

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    @Test
    public void anEpicCannotBeAddedToItselfAsASubtask() {
        List<Integer> subtasks = new ArrayList<>();
        Epic epic1 = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        epic1.setSubtasks(subtasks);
        Assertions.assertEquals(0, epic1.getSubtasks().size(), "Id Epic занесен в список SubTasksId");
    }

}