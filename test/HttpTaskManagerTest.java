import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import enums.Status;
import interfaces.TaskManager;
import managers.HttpTaskManager;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import ultility.LocalDateTimeAdapter;

import java.io.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>{

    private HttpTaskManager httpTaskManager;
    private Gson gson;
    private KVServer kvServer;




    @BeforeEach
    public void start() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();

        httpTaskManager = new HttpTaskManager("8078");
        super.taskManager = httpTaskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

    }

    @AfterEach
    public void stop(){
        kvServer.stop();

    }




    @Test
    public void saveTask() throws IOException, InterruptedException {
        Task task = new Task("test","test");
        httpTaskManager.createTask(task);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager1.getTaskById(1),httpTaskManager.getTaskById(1));
    }

    @Test
    public void saveSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("test","test",Status.NEW);
        SubTask subTask = new SubTask("test","test",Status.NEW,1);
        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubtask(subTask);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager1.getSubTaskById(2),httpTaskManager.getSubTaskById(2));
    }

    @Test
    public void saveEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("test","test",Status.NEW);
        httpTaskManager.createEpic(epic);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager1.getEpicById(1),httpTaskManager.getEpicById(1));
    }

    @Test
    public void saveHistory() throws IOException, InterruptedException {
        Task task = new Task("test","test");
        Task task1 = new Task("test","test");
        httpTaskManager.createTask(task);
        httpTaskManager.createTask(task1);
        httpTaskManager.getTaskById(2);
        httpTaskManager.getTaskById(1);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager1.getHistory(),httpTaskManager.getHistory());
    }

    @Test
    public void loadTasks() throws IOException, InterruptedException {
        Task task = new Task("test","test");
        httpTaskManager.createTask(task);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager.getTaskById(1),httpTaskManager1.getTaskById(1));
    }

    @Test
    public void loadSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("test","test",Status.NEW);
        SubTask subTask = new SubTask("Test","test",Status.NEW,1);
        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubtask(subTask);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager.getSubTaskById(2),httpTaskManager1.getSubTaskById(2));
    }

    @Test
    public void loadEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("test","test",Status.NEW);
        httpTaskManager.createEpic(epic);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager.getEpicById(1),httpTaskManager1.getEpicById(1));
    }

    @Test
    public void loadHistory() throws IOException, InterruptedException {
        Task task = new Task("test","test");
        Task task1 = new Task("test","test");
        httpTaskManager.createTask(task);
        httpTaskManager.createTask(task1);
        httpTaskManager.getTaskById(2);
        httpTaskManager.getTaskById(1);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        assertEquals(httpTaskManager.getHistory(),httpTaskManager1.getHistory());
    }



}
