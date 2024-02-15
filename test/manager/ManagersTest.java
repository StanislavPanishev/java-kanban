package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ManagersTest {

    //убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    public void returnsInitializedAndReadyInstancesOfManagersGetDefault() {
        Assertions.assertNotNull(Managers.getDefault(), "InMemoryTaskManager is null");
    }

    @Test
    public void returnsInitializedAndReadyInstancesOfManagersGetDefaultHistory() {
        Assertions.assertNotNull(Managers.getDefaultHistory(), "InMemoryHistoryManager is null");
    }
}