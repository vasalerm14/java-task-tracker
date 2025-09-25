import enums.Status;
import managers.FileBackedTasksManager;
import managers.HttpTaskManager;
import managers.HttpTaskServer;
import managers.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

       //Привет :)
        //Я не до конца понял как нужно сделать, но вроде сделал и работает, спасибо !



        new KVServer().start();
        HttpTaskManager httpTaskManager = new HttpTaskManager("8078");
        Task task = new Task("test","test");
        httpTaskManager.createTask(task);
        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
        System.out.println(httpTaskManager1.getTaskById(1));
//        HttpTaskManager httpTaskManager = new HttpTaskManager("8078");
//        Task task = new Task("Test","test");
//        Task task1 = new Task("test","test");
//        httpTaskManager.createTask(task);
//        httpTaskManager.createTask(task1);
//        Epic epic = new Epic("test","test", Status.NEW);
//        SubTask subTask = new SubTask("test","test",Status.NEW,3);
//        SubTask newSubTask = new SubTask("test","test",Status.DONE,4,3);
//        httpTaskManager.createEpic(epic);
//        httpTaskManager.createSubtask(subTask);
//        httpTaskManager.updateSubTask(newSubTask);
//        httpTaskManager.getTaskById(2);
//        httpTaskManager.getSubTaskById(4);
//        httpTaskManager.load();
//
//        HttpTaskManager httpTaskManager1 = httpTaskManager.load();
//        System.out.println(httpTaskManager1.getTasks());




    }
}
