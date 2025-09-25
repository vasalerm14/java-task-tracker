import enums.Status;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    void epicWithOutSubTask() {
        Epic epic = new Epic("TEST", "TEST", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        final int epicTasks = epic.getSubTaskId().size();
        assertEquals(epicTasks, inMemoryTaskManager.getEpicById(1).getSubTaskId().size(), "SubTask не пустые");
    }

    @Test
    void epicWithSubTasksNewStatus() {
        Epic epic = new Epic("TEST", "TEST", Status.NEW);
        SubTask subTask = new SubTask("TEST", "TEST", Status.NEW, 1);
        SubTask subTask1 = new SubTask("TEST", "TEST", Status.NEW, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.getEpics();
        inMemoryTaskManager.createSubtask(subTask);
        inMemoryTaskManager.createSubtask(subTask1);
        assertEquals(Status.NEW, inMemoryTaskManager.getEpicById(1).getStatus(), "Статусы не совпадают");
    }

    @Test
    void epicWithSubTasksDoneStatus() {
        Epic epic = new Epic("TEST", "TEST", Status.NEW);
        SubTask subTask = new SubTask("TEST", "TEST", Status.NEW, 1); // 2
        SubTask subTask1 = new SubTask("TEST", "TEST", Status.NEW, 1); // 3
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subTask);
        inMemoryTaskManager.createSubtask(subTask1);
        SubTask subTaskForUpdate = inMemoryTaskManager.getSubTaskById(2);
        SubTask subTaskForUpdate1 = inMemoryTaskManager.getSubTaskById(3);
        subTaskForUpdate.setStatus(Status.DONE);
        subTaskForUpdate1.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(subTaskForUpdate);
        inMemoryTaskManager.updateSubTask(subTaskForUpdate1);
        assertEquals(Status.DONE, inMemoryTaskManager.getEpicById(1).getStatus(), "Статусы не совпадают");
    }

    @Test
    void epicWithSubTaskDoneAndNewStatus() {
        Epic epic = new Epic("TEST", "TEST", Status.NEW);
        SubTask subTask = new SubTask("TEST", "TEST", Status.NEW, 1); // 2
        SubTask subTask1 = new SubTask("TEST", "TEST", Status.NEW, 1); // 3
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subTask);
        inMemoryTaskManager.createSubtask(subTask1);
        SubTask subTaskForUpdate = inMemoryTaskManager.getSubTaskById(2);
        subTaskForUpdate.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(subTaskForUpdate);
        assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpicById(1).getStatus(), "Статусы не совпадают");
    }


    @Test
    void epicWithSubTasksInProgressStatus() {
        Epic epic = new Epic("TEST", "TEST", Status.NEW);
        SubTask subTask = new SubTask("TEST", "TEST", Status.NEW, 1); // 2
        SubTask subTask1 = new SubTask("TEST", "TEST", Status.IN_PROGRESS, 1); // 3
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subTask);
        inMemoryTaskManager.createSubtask(subTask1);
        assertEquals(Status.IN_PROGRESS, inMemoryTaskManager.getEpicById(1).getStatus(), "Статусы не совпадают");
    }
}