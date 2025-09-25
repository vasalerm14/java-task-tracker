import enums.Status;
import interfaces.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;


import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;



    @Test
    void checkCreateTask() {
        Task task = new Task("Test", "Test", Status.NEW);
        taskManager.createTask(task);
        assertEquals(1, taskManager.getTasks().size(), "Таск не создается");
    }


    @Test
    void checkCreateSubTaskWithEpic() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        taskManager.createSubtask(subTask);
        assertEquals(1, taskManager.getSubtasks().size(), "SubTask с объявленным эпиком создается не корректно");
    }

    @Test
    void checkCreateEpic() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getEpics().size(), "Epic создается не корректно");
    }

    @Test
    void checkGetTasks() {

        Task task = new Task("Test", "Test", Status.NEW);
        Task task2 = new Task("Test", "Test", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        assertEquals(2, taskManager.getTasks().size(), "Функция getTasks работает не корректно");
    }

    @Test
    void checkGetEpics() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        Epic epic1 = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        assertEquals(2, taskManager.getEpics().size(), "Функция getEpics работает не корректно");
    }

    @Test
    void checkGetSubTasks() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Test", "test", Status.NEW, 1);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        assertEquals(2, taskManager.getSubtasks().size(), "Функция getSubTasks работает не корректно");
    }

    @Test
    void checkGetTaskById() {

        Task task = new Task("Test", "Test", Status.NEW);
        Task task2 = new Task("Test", "Test", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        assertEquals(task2, taskManager.getTaskById(2), "Функция getTasksById работает не корректно");

    }

    @Test
    void checkGetSubTaskById() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Test", "test", Status.NEW, 1);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskById(3), "Функция getSubTaskById работает не корректно");

    }

    @Test
    void checkGetEpicById() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        Epic epic1 = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(2), "Функция getEpicById работает не корректно");
    }

    @Test
    void checkClearTasks() {

        Task task = new Task("Test", "Test", Status.NEW);
        Task task2 = new Task("Test", "Test", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.clearTasks();
        assertEquals(0, taskManager.getTasks().size(), "Функция clearTasks работает не корректно");
    }

    @Test
    void checkClearEpic() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        Epic epic1 = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        taskManager.clearEpic();
        assertEquals(0, taskManager.getEpics().size(), "Функция clearEpic работает не корректно");
    }

    @Test
    void checkClearSubTask() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Test", "test", Status.NEW, 1);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        taskManager.clearSubTask();
        assertEquals(0, taskManager.getSubtasks().size(), "Функция clearSubTask работает не корректно");
    }

    @Test
    void checkClearTaskById() {

        Task task = new Task("Test", "Test", Status.NEW);
        Task task2 = new Task("Test", "Test", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.clearTaskById(2);
        assertFalse(taskManager.getTasks().contains(task2), "Функция clearTaskById работает не корректно");
    }

    @Test
    void checkClearSubTaskById() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Test", "test", Status.NEW, 1);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        taskManager.clearSubTaskById(3);
        assertFalse(taskManager.getSubtasks().contains(subTask1), "Функция clearSubTaskById работает не корректно");
    }

    @Test
    void checkClearEpicById() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Test", "test", Status.NEW, 1);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        taskManager.clearEpicById(1);
        assertFalse(taskManager.getEpics().contains(epic), "Функция clearEpicById работает не корректно");
    }

    @Test
    void checkUpdateTask() {

        Task task = new Task("Test", "Test", Status.NEW);
        taskManager.createTask(task);
        Task updateTask = new Task("Test", "Test", Status.DONE, 1);
        taskManager.updateTasks(updateTask);
        assertEquals(updateTask, taskManager.getTaskById(1), "Функция updateTask работает не корректно");
    }

    @Test
    void checkUpdateSubTask() {

        Epic epic = new Epic("Test", "test", Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        taskManager.createSubtask(subTask);
        SubTask updateSubTask = new SubTask("Test2", "test", Status.DONE, 2, 1);
        taskManager.updateSubTask(updateSubTask);
        assertEquals(updateSubTask, taskManager.getSubTaskById(2), "Функция updateSubTask работает не корректно");
    }

    @Test
    void checkGetHistory() {

        Task task = new Task("Test", "Test", Status.NEW);
        Task task2 = new Task("Test", "Test", Status.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.clearTaskById(1);
        assertEquals(1, taskManager.getHistory().size(), "Функция getHistory работае не корректно");
        assertEquals(task2, taskManager.getHistory().get(0), "Функция getHistory работае не корректно");
    }


}
