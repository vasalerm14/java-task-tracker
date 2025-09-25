package ultility;

import enums.Status;
import enums.TaskTypes;
import interfaces.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringConverter {
    public static Task fromString(String values) {



        if (values == null) {
            return null;
        }
        ArrayList<String> arrValues = new ArrayList<>(Arrays.asList(values.split(",")));
        if(TaskTypes.valueOf(arrValues.get(1))==TaskTypes.SUBTASK){
            SubTask subTask = new SubTask(arrValues.get(2),arrValues.get(4),Status.valueOf(arrValues.get(3)),Integer.parseInt(arrValues.get(5)));
            subTask.setId(Integer.parseInt(arrValues.get(0)));
            System.out.println();
            subTask.setTaskTypes(TaskTypes.valueOf(arrValues.get(1)));

            if(arrValues.size()==9){
                subTask.setDuration(Integer.parseInt(arrValues.get(6)));
                subTask.setStartTime(LocalDateTime.parse(arrValues.get(7)));
            }
            return subTask;
        } else if (TaskTypes.valueOf(arrValues.get(1))==TaskTypes.EPIC) {
            Epic epic = new Epic(arrValues.get(2), arrValues.get(4), Status.valueOf(arrValues.get(3)));
            epic.setId(Integer.parseInt(arrValues.get(0)));
            epic.setTaskTypes(TaskTypes.valueOf(arrValues.get(1)));
            if(arrValues.size()==8){
                epic.setDuration(Integer.parseInt(arrValues.get(5)));
                epic.setStartTime(LocalDateTime.parse(arrValues.get(6)));
                epic.setEndTime(LocalDateTime.parse(arrValues.get(7)));
            }

            return epic;
        }
        Task task = new Task(arrValues.get(2), arrValues.get(4), Status.valueOf(arrValues.get(3)));
        task.setId(Integer.parseInt(arrValues.get(0)));
        task.setTaskTypes(TaskTypes.valueOf(arrValues.get(1)));
        if(arrValues.size()==8){
            task.setDuration(Integer.parseInt(arrValues.get(5)));
            task.setStartTime(LocalDateTime.parse(arrValues.get(6)));
        }
        return task;
    }

    public static String toString(Task task) {


        if(task.getCollectData() && task.getEndTime()!=null){

            if(task.getTaskTypes()==TaskTypes.SUBTASK){

                SubTask subTask = (SubTask) task;
                return subTask.getIdentification() + "," + subTask.getTaskTypes() + "," + subTask.getName() + "," +
                        subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicId()
                        + "," + subTask.getDuration() + "," + subTask.getStartTime() + "," + subTask.getEndTime();
            } else{
                return task.getIdentification() + "," + task.getTaskTypes() + "," + task.getName() + "," + task.getStatus() + ","
                        + task.getDescription() + ","  + task.getDuration() + "," + task.getStartTime() + "," + task.getEndTime();
            }

        } else if (task.getTaskTypes()==TaskTypes.SUBTASK) {
            SubTask subTask = (SubTask) task;
            return subTask.getIdentification() + "," + subTask.getTaskTypes() + "," + subTask.getName() + "," + subTask.getStatus() + ","
                    + subTask.getDescription() + "," + subTask.getEpicId();

        } else {

            return task.getIdentification() + "," + task.getTaskTypes() + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription() + ",";
        }

    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Task> history = manager.getHistory();
        int t = 0;
        while (t < history.size()) {
            stringBuilder.append(history.get(t).getIdentification());
            t++;
            if (t != history.size())
                stringBuilder.append(',');
        }
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String values){

        List<Integer> history = new ArrayList<>();
        for(String value : values.split(",")){
            history.add(Integer.parseInt(value));
        }
        return history;
    }

}
