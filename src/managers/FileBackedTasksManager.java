package managers;

import enums.TaskTypes;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import ultility.StringConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File fileName;

    public FileBackedTasksManager(File fileName) {
        this.fileName = fileName;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(SubTask subtask) {
        super.createSubtask(subtask);
        save();

    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubTask() {
        super.clearSubTask();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearTaskById(int id) {
        super.clearTaskById(id);
        save();
    }

    @Override
    public void clearSubTaskById(int id) {
        super.clearSubTaskById(id);
        save();
    }

    @Override
    public void clearEpicById(int id) {
        super.clearEpicById(id);
        save();
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    protected void save() {
        Path path = Paths.get(fileName.toString());
        String toSave = "id,type,name,status,description,epic,duration,startTime,endTime" + '\n';
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toSave);

        for (Task task : super.getTasks()) {
            stringBuilder.append(StringConverter.toString(task)).append('\n');
        }
        for (Task task : super.getEpics()) {
            stringBuilder.append(StringConverter.toString(task)).append('\n');

        }
        for (SubTask subTask : super.getSubtasks()) {

            stringBuilder.append(StringConverter.toString(subTask)).append('\n');
        }
        stringBuilder.append('\n');

        stringBuilder.append(StringConverter.historyToString(history));
        ;
        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write(stringBuilder.toString());
        } catch (IOException e) {
            throw new FileProcessingException("Ошибка записи в файл");
        }

    }

    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try {
            String fileHistory = Files.readString(file.toPath());


            List<String> lines = new ArrayList<>(Arrays.asList(fileHistory.split("\n")));

            lines.remove(0);


            for (String line : lines) {

                if (line.isEmpty()) {
                    break;
                }
                Task task = StringConverter.fromString(line);


                if (task.getTaskTypes() == TaskTypes.EPIC) {
                    fileBackedTasksManager.epicHashMap.put(task.getIdentification(), (Epic) task);
                } else if (task.getTaskTypes() == TaskTypes.SUBTASK) {
                    fileBackedTasksManager.subTaskHashMap.put(task.getIdentification(), (SubTask) task);
                    fileBackedTasksManager.addSubTaskToEpic(((SubTask) task).getEpicId(), task.getIdentification());
                } else {
                    fileBackedTasksManager.taskHashMap.put(task.getIdentification(), task);

                }
            }
            if (lines.size() > 2) {
                for (Integer taskId : StringConverter.historyFromString(lines.get(lines.size() - 1))) {

                    if (fileBackedTasksManager.taskHashMap.containsKey(taskId)) {
                        fileBackedTasksManager.history.add(fileBackedTasksManager.taskHashMap.get(taskId));
                    } else if (fileBackedTasksManager.subTaskHashMap.containsKey(taskId)) {
                        fileBackedTasksManager.history.add(fileBackedTasksManager.subTaskHashMap.get(taskId));
                    } else {
                        fileBackedTasksManager.history.add(fileBackedTasksManager.epicHashMap.get(taskId));
                    }
                }
            }


            return fileBackedTasksManager;

        } catch (IOException e) {
            throw new FileProcessingException("Не удалось прочитать файл");
        }
    }


}
