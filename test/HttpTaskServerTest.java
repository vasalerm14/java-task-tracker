import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import enums.Status;
import interfaces.TaskManager;
import managers.InMemoryTaskManager;
import managers.HttpTaskManager;
import managers.HttpTaskServer;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import ultility.LocalDateTimeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private InMemoryTaskManager inMemoryTaskManager;
    private Gson gson;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    void start() throws IOException {

        File file = new File("backUp.txt");
        inMemoryTaskManager = new InMemoryTaskManager();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        httpTaskServer = new HttpTaskServer(inMemoryTaskManager);
        httpTaskServer.startServer();
    }

    @AfterEach
    void stop() {
        httpTaskServer.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task task = new Task("test", "test");
        inMemoryTaskManager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {}.getType());
        assertEquals(inMemoryTaskManager.getTasks(), tasks, "/tasks/task/ работает не корректно");
    }

    @Test
    void getSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {}.getType());
        assertEquals(inMemoryTaskManager.getSubtasks(), subTasks, "/tasks/subtask/ работает не корректно");
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicNew = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {}.getType());
        assertEquals(inMemoryTaskManager.getEpics(), epicNew, "/tasks/epic/ работает не корректно");
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        Task task = new Task("test", "test");
        inMemoryTaskManager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(inMemoryTaskManager.getTaskById(1)), response.body(), "/tasks/task/ работает не корректно");
    }

    @Test
    void getSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        SubTask subTask = new SubTask("Test", "test", Status.NEW, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(inMemoryTaskManager.getSubTaskById(2)), response.body(), "/tasks/subtask?id= работает не корректно");
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(inMemoryTaskManager.getEpicById(1)), response.body(), "/tasks/epic работает не корректно");
    }

    @Test
    void getEpicSubtasksIds() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        SubTask subTask = new SubTask("test", "test", Status.NEW, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(inMemoryTaskManager.getEpicById(1).getSubTaskId()), response.body(), "/tasks/subtasks/epic?id= работает не корректно");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        SubTask subTask = new SubTask("test", "test", Status.NEW, 1);
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subTask);
        inMemoryTaskManager.getSubTaskById(2);
        inMemoryTaskManager.getEpicById(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(inMemoryTaskManager.getHistory()), response.body(), "/tasks/history работает не корректно");
    }


    @Test
    void createTask() throws IOException, InterruptedException {
        Task task = new Task("test", "test");
        task.setId(1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        URI GETurl = URI.create("http://localhost:8080/tasks/task?id=1");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest Getrequest = HttpRequest.newBuilder().uri(GETurl).GET().build();
        HttpResponse<String> Getresponse = client.send(Getrequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(task), Getresponse.body(), "POST /tasks/task работает не корректно");
    }

    @Test
    void createSutask() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("test", "test", Status.NEW, 1);
        inMemoryTaskManager.createSubtask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        URI GETurl = URI.create("http://localhost:8080/tasks/subtask?id=2");
        String json = gson.toJson(inMemoryTaskManager.getSubTaskById(2));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest Getrequest = HttpRequest.newBuilder().uri(GETurl).GET().build();
        HttpResponse<String> Getresponse = client.send(Getrequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(inMemoryTaskManager.getSubTaskById(2)), Getresponse.body(), "POST /tasks/subtask работает не корректно");
    }

    @Test
    void createEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        URI GETurl = URI.create("http://localhost:8080/tasks/epic?id=1");
        String json = gson.toJson(inMemoryTaskManager.getSubTaskById(2));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest Getrequest = HttpRequest.newBuilder().uri(GETurl).GET().build();
        HttpResponse<String> Getresponse = client.send(Getrequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(inMemoryTaskManager.getEpicById(1)), Getresponse.body(), "POST /tasks/epic работает не корректно");
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {

        Task task = new Task("test", "test");
        inMemoryTaskManager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        URI GETurl = URI.create("http://localhost:8080/tasks/task?id=1");
        String json = gson.toJson(inMemoryTaskManager.getTaskById(1));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        //Создание на сервере
        HttpRequest POSTrequest = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(POSTrequest, HttpResponse.BodyHandlers.ofString());
        //Удаление задачи на сервере
        HttpRequest DELETErequest = HttpRequest.newBuilder().uri(GETurl).DELETE().build();
        HttpResponse<String> DELETEresponse = client.send(DELETErequest, HttpResponse.BodyHandlers.ofString());
        //Получение задачи с сервера
        HttpRequest GETrequest = HttpRequest.newBuilder().uri(GETurl).GET().build();
        HttpResponse<String> GETresponse = client.send(GETrequest, HttpResponse.BodyHandlers.ofString());

        assertEquals("", GETresponse.body(), "DELETE /tasks/task?id= работает не корректно");


    }

    @Test
    void deleteSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("test", "test", Status.NEW, 1);
        inMemoryTaskManager.createSubtask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        URI GETurl = URI.create("http://localhost:8080/tasks/subtask?id=2");
        String json = gson.toJson(inMemoryTaskManager.getSubTaskById(2));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        //Создание на сервере
        HttpRequest POSTrequest = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(POSTrequest, HttpResponse.BodyHandlers.ofString());
        //Удаление задачи на сервере
        HttpRequest DELETErequest = HttpRequest.newBuilder().uri(GETurl).DELETE().build();
        HttpResponse<String> DELETEresponse = client.send(DELETErequest, HttpResponse.BodyHandlers.ofString());
        //Получение задачи с сервера
        HttpRequest GETrequest = HttpRequest.newBuilder().uri(GETurl).GET().build();
        HttpResponse<String> GETresponse = client.send(GETrequest, HttpResponse.BodyHandlers.ofString());

        assertEquals("null", GETresponse.body(), "DELETE /tasks/subtask?id= работает не корректно");

    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        URI GETurl = URI.create("http://localhost:8080/tasks/epic?id=1");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        //Создание на сервере
        HttpRequest POSTrequest = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(POSTrequest, HttpResponse.BodyHandlers.ofString());
        //Удаление задачи на сервере
        HttpRequest DELETErequest = HttpRequest.newBuilder().uri(GETurl).DELETE().build();
        HttpResponse<String> DELETEresponse = client.send(DELETErequest, HttpResponse.BodyHandlers.ofString());
        //Получение задачи с сервера
        HttpRequest GETrequest = HttpRequest.newBuilder().uri(GETurl).GET().build();
        HttpResponse<String> GETresponse = client.send(GETrequest, HttpResponse.BodyHandlers.ofString());
        assertEquals("null", GETresponse.body(), "DELETE /tasks/epic?id= работает не корректно");
    }

    @Test
    void deleteTasks() throws IOException, InterruptedException {
        Task task = new Task("test", "test");
        inMemoryTaskManager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(inMemoryTaskManager.getTaskById(1));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        //Создание на сервере
        HttpRequest POSTrequest = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(POSTrequest, HttpResponse.BodyHandlers.ofString());
        //Удаление задачи на сервере
        HttpRequest DELETErequest = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> DELETEresponse = client.send(DELETErequest, HttpResponse.BodyHandlers.ofString());
        //Получение задачи с сервера
        HttpRequest GETrequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> GETresponse = client.send(GETrequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(List.of()), GETresponse.body(), "DELETE /tasks/task работает не корректно");

    }

    @Test
    void deleteSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("test", "test", Status.NEW, 1);
        inMemoryTaskManager.createSubtask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(inMemoryTaskManager.getSubTaskById(2));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        //Создание на сервере
        HttpRequest POSTrequest = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(POSTrequest, HttpResponse.BodyHandlers.ofString());
        //Удаление задачи на сервере
        HttpRequest DELETErequest = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> DELETEresponse = client.send(DELETErequest, HttpResponse.BodyHandlers.ofString());
        //Получение задачи с сервера
        HttpRequest GETrequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> GETresponse = client.send(GETrequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(List.of()), GETresponse.body(), "DELETE /tasks/subtask работает не корректно");

    }

    @Test
    void deleteEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test", Status.NEW);
        inMemoryTaskManager.createEpic(epic);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(inMemoryTaskManager.getEpicById(1));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        //Создание на сервере
        HttpRequest POSTrequest = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(POSTrequest, HttpResponse.BodyHandlers.ofString());
        //Удаление задачи на сервере
        HttpRequest DELETErequest = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> DELETEresponse = client.send(DELETErequest, HttpResponse.BodyHandlers.ofString());
        //Получение задачи с сервера
        HttpRequest GETrequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> GETresponse = client.send(GETrequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(gson.toJson(List.of()), GETresponse.body(), "DELETE /tasks/epic работает не корректно");

    }


}
