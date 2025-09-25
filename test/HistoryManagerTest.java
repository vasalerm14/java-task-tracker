import enums.Status;
import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {

    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    void clearHistory() {
        assertEquals(0, inMemoryHistoryManager.getHistory().size(), "История не пустая");
    }

    @Test
    void checkHistoryDuplication() {
        Task task = new Task("TEST", "TEST", Status.NEW);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task);
        assertEquals(1, inMemoryHistoryManager.getHistory().size(), "Размер истории не совпадает с тестовым");
    }

    @Test
    void removeFromStartHistory() {

        Task task = new Task("START", "TEST", Status.NEW);
        Task task1 = new Task("MIDDLE", "TEST", Status.NEW);
        Task task2 = new Task("END", "TEST", Status.NEW);
        task.setId(1); // На удаление
        task1.setId(2);
        task2.setId(3);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.remove(1);
        List<Task> testHistory = new ArrayList<>(List.of(task1, task2));
        assertEquals(testHistory, inMemoryHistoryManager.getHistory(), "История не совпадает");
    }

    @Test
    void removeFromMiddleHistory() {

        Task task = new Task("START", "TEST", Status.NEW);
        Task task1 = new Task("MIDDLE", "TEST", Status.NEW);
        Task task2 = new Task("END", "TEST", Status.NEW);
        task.setId(1);
        task1.setId(2);// На удаление
        task2.setId(3);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.remove(2);
        List<Task> testHistory = new ArrayList<>(List.of(task, task2));
        assertEquals(testHistory, inMemoryHistoryManager.getHistory(), "История не совпадает");
    }

    @Test
    void removeFromEndHistory() {

        Task task = new Task("START", "TEST", Status.NEW);
        Task task1 = new Task("MIDDLE", "TEST", Status.NEW);
        Task task2 = new Task("END", "TEST", Status.NEW);
        task.setId(1);
        task1.setId(2);
        task2.setId(3);// На удаление
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.remove(3);
        List<Task> testHistory = new ArrayList<>(List.of(task, task1));
        assertEquals(testHistory, inMemoryHistoryManager.getHistory(), "История не совпадает");
    }



}