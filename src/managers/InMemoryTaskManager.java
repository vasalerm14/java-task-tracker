package managers;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import enums.Status;

import java.time.LocalDateTime;
import java.util.*;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id;
    protected HashMap<Integer, Task> taskHashMap = new HashMap<>(); //HashMap хранящий объекты класса Task
    protected HashMap<Integer, Epic> epicHashMap = new HashMap<>(); //HashMap хранящий объекты класса Epic
    protected HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>(); //HashMap хранящий объекты класса SubTask

    protected HistoryManager history = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.id = 0;
    }

    @Override
    public void createTask(Task task) {
        validation(task);
        task.setId(++id);
        taskHashMap.put(id, task);

    }

    @Override
    public void createSubtask(SubTask subTask) throws FileProcessingException {
        validation(subTask);
        subTask.setId(++id);
        subTaskHashMap.put(id, subTask);
        addSubTaskToEpic(subTask.getEpicId(), id);
        if (subTask.getDuration() > 0 && subTask.getStartTime() != null) {
            Epic epic = updateEpicTime(epicHashMap.get(subTask.getEpicId()));
            epicHashMap.put(epic.getIdentification(), epic);
        }
    }

    @Override
    public void createEpic(Epic epic) throws FileProcessingException {
        epic.setId(++id);
        epicHashMap.put(id, updateEpicTime(epic));
    }

    @Override
    public Collection<Task> getTasks() {
        return List.copyOf(taskHashMap.values());
    }

    @Override
    public Collection<Epic> getEpics() {
        return List.copyOf(epicHashMap.values());
    }

    @Override
    public Collection<SubTask> getSubtasks() {
        return List.copyOf(subTaskHashMap.values());
    }

    @Override
    public Task getTaskById(int id) throws FileProcessingException {
        history.add(taskHashMap.get(id));
        return taskHashMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        history.add(epicHashMap.get(id));
        return epicHashMap.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        history.add(subTaskHashMap.get(id));
        return subTaskHashMap.get(id);
    }

    @Override
    public List<SubTask> showSubTaskByEpic(int epicId) {
        Epic epic = epicHashMap.get(epicId);
        List<SubTask> subTasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTasks.add(getSubTaskById(subTaskId));
        }

        return subTasks;
    }

    @Override
    public void clearTasks() {
        for (Integer id : taskHashMap.keySet()) {
            history.remove(id);
        }
        taskHashMap.clear();
    }

    @Override
    public void clearEpic() {
        for (Integer id : epicHashMap.keySet()) {
            for (Integer subtaskId : epicHashMap.get(id).getSubTaskId()) {
                history.remove(subtaskId);
            }
            history.remove(id);
        }
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void clearSubTask() {
        for (SubTask subtask : subTaskHashMap.values()) {
            removeSubTaskFromEpic(subtask.getEpicId(), subtask.getIdentification());
            updateEpicStatus(subtask.getEpicId());
            Epic epic = epicHashMap.get(subtask.getEpicId());
            epic.setEndTime(null);
            epic.setStartTime(null);
            epic.setDuration(0);
            epicHashMap.put(subtask.getEpicId(), updateEpicTime(epic));
            history.remove(subtask.getIdentification());
        }
        subTaskHashMap.clear();

    }

    @Override
    public void clearTaskById(int id) {
        taskHashMap.remove(id);
        history.remove(id);
    }

    @Override
    public void clearEpicById(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer subtaskId : epic.getSubTaskId()) {
            subTaskHashMap.remove(subtaskId);
            history.remove(subtaskId);
        }
        epicHashMap.remove(id);
        history.remove(id);
    }

    @Override
    public void clearSubTaskById(int id) {
        SubTask subtask = subTaskHashMap.get(id);
        removeSubTaskFromEpic(subtask.getEpicId(), subtask.getIdentification());
        updateEpicStatus(subtask.getEpicId());
        Epic epic = epicHashMap.get(subtask.getEpicId());
        epic.setStartTime(null);
        epic.setEndTime(null);
        epic.setDuration(0);
        epicHashMap.put(subtask.getEpicId(), epic);
        subTaskHashMap.remove(id);
        history.remove(id);
    }

    private void removeSubTaskFromEpic(Integer epicId, Integer subTaskId) {
        Epic epic = epicHashMap.get(epicId);
        epic.removeSubTask(subTaskId);
        epicHashMap.put(epicId, epic);
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epicHashMap.get(epicId);
        int newScore = 0;
        int doneScore = 0;
        for (Integer subTaskId : epic.getSubTaskId()) {
            if (subTaskHashMap.get(subTaskId).getStatus() == (Status.DONE)) {
                doneScore++;
            } else if (subTaskHashMap.get(subTaskId).getStatus() == (Status.NEW)) {
                newScore++;
            }
        }
        if (epic.getSubTaskId().isEmpty() || newScore == epic.getSubTaskId().size()) {
            epic.setStatus(Status.NEW);
        } else if (doneScore == epic.getSubTaskId().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        epicHashMap.put(epicId, epic);
    }


    private Epic updateEpicTime(Epic epic) {
        LocalDateTime earliestStartTime = null;
        LocalDateTime latestEndTime = null;
        int totalDuration = 0;
        for (Integer subtaskId : epic.getSubTaskId()) {
            LocalDateTime subTaskStartTime = getSubTaskById(subtaskId).getStartTime();
            LocalDateTime subTaskEndTime = getSubTaskById(subtaskId).getEndTime();
            if (earliestStartTime == null || subTaskStartTime.isBefore(earliestStartTime)) {
                earliestStartTime = subTaskStartTime;
            }
            if (latestEndTime == null || subTaskEndTime.isAfter(latestEndTime)) {
                latestEndTime = subTaskEndTime;
            }
            totalDuration += getSubTaskById(subtaskId).getDuration();
        }
        epic.setStartTime(earliestStartTime);
        epic.setEndTime(latestEndTime);
        epic.setDuration(totalDuration);
        return epic;
    }

    protected void addSubTaskToEpic(int epicId, int subTaskId) {
        Epic epic = epicHashMap.get(epicId);
        epic.updateSubTaskId(subTaskId);
        updateEpicStatus(epicId);
    }

    @Override
    public void updateTasks(Task task) {
        validation(task);
        task.setId(taskHashMap.get(task.getIdentification()).getIdentification());
        taskHashMap.put(task.getIdentification(), task);
    }


    @Override
    public void updateSubTask(SubTask subTask) {
        validation(subTask);
        subTask.setId(subTaskHashMap.get(subTask.getIdentification()).getIdentification());
        subTaskHashMap.put(subTask.getIdentification(), subTask);

        updateEpicStatus(subTask.getEpicId());

    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(LocalDateTime::compareTo)));

        prioritizedTasks.addAll(taskHashMap.values());
        prioritizedTasks.addAll(subTaskHashMap.values());

        return new ArrayList<>(prioritizedTasks);
    }


    private void validation(Task newTask) {
        if (newTask.getStartTime() == null) {
            return;
        }

        for (Task task : getPrioritizedTasks()) {
            if ((newTask.getStartTime().isBefore(task.getEndTime()) || newTask.getStartTime().isEqual(task.getEndTime()))
                    && (newTask.getEndTime().isAfter(task.getStartTime()) || newTask.getEndTime().isEqual(task.getStartTime()))) {
                throw new FileProcessingException("Найдено пересечение с существующей задачей");
            }
        }
    }


}
