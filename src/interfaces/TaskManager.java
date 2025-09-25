package interfaces;

import managers.FileProcessingException;
import tasks.*;

import java.util.Collection;
import java.util.List;

public interface TaskManager {
    void createTask(Task task) throws FileProcessingException;

    void createSubtask(SubTask subTask) throws FileProcessingException;

    void createEpic(Epic epic) throws FileProcessingException;

    Collection<Task> getTasks();

    Collection<Epic> getEpics();

    Collection<SubTask> getSubtasks();

    Task getTaskById(int id) throws FileProcessingException;

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    List<SubTask> showSubTaskByEpic(int epicId);

    void clearTasks();

    void clearEpic();

    void clearSubTask();

    void clearTaskById(int id);

    void clearEpicById(int id);

    void clearSubTaskById(int id);

    void updateTasks(Task task);

    void updateSubTask(SubTask subTask);

    List<Task> getHistory();
    List<Task> getPrioritizedTasks();
}
