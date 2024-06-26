package task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    // проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void instancesOfTheTaskAreEqualIfTheirIdIsEqual() {
        Task task1 = new Task("Спринт 5", "Выполнить ТЗ", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(15));
        Task task2 = new Task("Спринт 5", "Выполнить ТЗ", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(15));
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2, "Экземпляры класса Task не равны друг другу");
    }
}