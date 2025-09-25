package managers;

import com.google.gson.*;
import enums.TaskTypes;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import ultility.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private String PORT;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskManager(String PORT) throws IOException, InterruptedException {
        super(null);
        this.kvTaskClient = new KVTaskClient(PORT);
        this.PORT = PORT;
    }

    @Override
    protected void save() {
        kvTaskClient.put("task", convertTasksToJson());
        kvTaskClient.put("epic", convertEpicToJson());
        kvTaskClient.put("subtask", convertSubTasksToJson());
        kvTaskClient.put("history", convertHistoryToJson());
    }

    public HttpTaskManager load() throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(PORT);
        String jsonTask = kvTaskClient.load("task");
        String jsonSubTask = kvTaskClient.load("subtask");
        String jsonEpic = kvTaskClient.load("epic");
        String jsonHistory = kvTaskClient.load("history");


        //Из json в Task
        JsonObject[] jsonArrayTask = gson.fromJson(jsonTask, JsonObject[].class);
        for (JsonObject jsonObject : jsonArrayTask) {
            for (String key : jsonObject.keySet()) {
                if (key != null) {
                    JsonObject taskObject = jsonObject.getAsJsonObject(key);
                    Task task = gson.fromJson(taskObject, Task.class);
                    task.setId(Integer.parseInt(key));
                    task.setTaskTypes(TaskTypes.TASK);
                    httpTaskManager.taskHashMap.put(Integer.parseInt(key), task);
                }
            }
        }

        //Из json в Epic
        JsonObject[] jsonArrayEpic = gson.fromJson(jsonEpic, JsonObject[].class);
        for (JsonObject jsonObject : jsonArrayEpic) {
            for (String key : jsonObject.keySet()) {
                if (key != null) {
                    JsonObject taskObject = jsonObject.getAsJsonObject(key);
                    Epic epic = gson.fromJson(taskObject, Epic.class);
                    epic.setTaskTypes(TaskTypes.EPIC);
                    epic.setId(Integer.parseInt(key));
                    httpTaskManager.epicHashMap.put(Integer.parseInt(key), epic);
                }
            }
        }

        //Из json в SubTask
        JsonObject[] jsonArraySubTask = gson.fromJson(jsonSubTask, JsonObject[].class);
        for (JsonObject jsonObject : jsonArraySubTask) {
            for (String key : jsonObject.keySet()) {
                if (key != null) {
                    JsonObject taskObject = jsonObject.getAsJsonObject(key);
                    SubTask subTask = gson.fromJson(taskObject, SubTask.class);
                    subTask.setId(Integer.parseInt(key));
                    subTask.setTaskTypes(TaskTypes.SUBTASK);
                    httpTaskManager.subTaskHashMap.put(Integer.parseInt(key), subTask);
                    Epic epic = httpTaskManager.epicHashMap.get(subTask.getEpicId());
                    epic.updateSubTaskId(Integer.parseInt(key));
                    httpTaskManager.epicHashMap.put(epic.getIdentification(), epic);
                }
            }
        }

        //Из json в History
        jsonHistory = jsonHistory.replace("[", "");
        jsonHistory = jsonHistory.replace("]", "");
        for (String num : jsonHistory.split(",")) {
            if (num.isEmpty()) {
                break;
            }
            Integer taskId = Integer.parseInt(num);
            if (httpTaskManager.taskHashMap.containsKey(taskId)) {
                httpTaskManager.history.add(httpTaskManager.taskHashMap.get(taskId));
            } else if (httpTaskManager.subTaskHashMap.containsKey(taskId)) {
                httpTaskManager.history.add(httpTaskManager.subTaskHashMap.get(taskId));
            } else {
                httpTaskManager.history.add(httpTaskManager.epicHashMap.get(taskId));
            }
        }
        return httpTaskManager;
    }

    private String convertTasksToJson() {
        JsonArray tasksJsonArray = new JsonArray();

        for (Task task : getTasks()) {
            tasksJsonArray.add(task.toJson());
        }

        return tasksJsonArray.toString();
    }

    private String convertSubTasksToJson() {
        JsonArray subTaskJsonArray = new JsonArray();
        for (SubTask subTask : getSubtasks()) {
            subTaskJsonArray.add(subTask.toJson());
        }
        return subTaskJsonArray.toString();
    }

    private String convertEpicToJson() {
        JsonArray epicJsonArray = new JsonArray();
        for (Epic epic : getEpics()) {
            epicJsonArray.add(epic.toJson());
        }
        return epicJsonArray.toString();
    }

    private String convertHistoryToJson() {
        JsonArray historyArray = new JsonArray();
        for (Task task : getHistory()) {
            historyArray.add(task.getIdentification());
        }
        return historyArray.toString();

    }


}
