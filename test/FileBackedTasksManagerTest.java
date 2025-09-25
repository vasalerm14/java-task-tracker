import enums.Status;
import managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @BeforeEach
    void start(){
        File fileToRead = new File("backUp.txt");
        super.taskManager = new FileBackedTasksManager(fileToRead);
    }

    @Test
    void saveNullHistory() throws FileNotFoundException {
        File file = new File("test.txt");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.getTaskById(1);//Вызов функции для срабатывания сохранения
        int lines = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(file.toString()))){
            while (reader.readLine()!=null){
                lines++;
            }
        } catch (IOException e) {;

        }
        assertEquals(2,lines,"Пустой файл сохранился не корректно");
    }

    @Test
    void clearTasks() throws FileNotFoundException {
        File file = new File("test.txt");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Task task = new Task("TEST","TEST", Status.NEW);
        fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.clearTasks();
        int lines = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(file.toString()))){
            while (reader.readLine()!=null){
                lines++;
            }
        } catch (IOException e) {;

        }
        assertEquals(2,lines,"Пустой файл сохранился не корректно");
    }

    @Test
    void saveEpicWithOutSubTasks() throws FileNotFoundException {
        File file = new File("test.txt");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Epic epic = new Epic("TEST","TEST", Status.NEW);
        fileBackedTasksManager.createEpic(epic);
        int lines = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(file.toString()))){
            while (reader.readLine()!=null){
                lines++;
            }
        } catch (IOException e) {;

        }
        assertEquals(3,lines,"Файл сохранился не корректно");
    }

    @Test
    void backUpFromFile(){
        File file = new File("test.txt");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Epic epic = new Epic("TEST","TEST", Status.NEW);
        SubTask subTask = new SubTask("Test","Test",Status.DONE,1);
        fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.createSubtask(subTask);
        fileBackedTasksManager.getEpicById(1);

        FileBackedTasksManager newFileBackedTasksManager = fileBackedTasksManager.loadFromFile(file);

        assertEquals(fileBackedTasksManager.getTasks(),newFileBackedTasksManager.getTasks(),"Классы не совпадают по Таск");
        assertEquals(fileBackedTasksManager.getSubtasks(),newFileBackedTasksManager.getSubtasks(),"Классы не совпадают по SubTask");
        assertEquals(fileBackedTasksManager.getEpics(),newFileBackedTasksManager.getEpics(),"Классы не совпадают по Epic");
        assertEquals(fileBackedTasksManager.getHistory(),newFileBackedTasksManager.getHistory(),"Файлы не совпадают по истории");
    }
}