package managers;

import interfaces.HistoryManager;
import interfaces.TaskManager;

import java.io.File;
import java.io.IOException;

public class Managers {
    private static File file = new File("backUp.txt");

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTask() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked(){ return  new FileBackedTasksManager(file);}

    public static HttpTaskManager getDefaultHttpTaskManager() throws IOException, InterruptedException { return new HttpTaskManager("8078");}





}
