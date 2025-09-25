package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import enums.Status;
import enums.TaskTypes;

public class Epic extends Task {
    private List<Integer> subtasksIds;
    private LocalDateTime endTime;


    public Epic(String name, String discription, Status status) {
        super(name, discription, status);
        this.subtasksIds = new ArrayList<>();
        this.setTaskTypes(TaskTypes.EPIC);
    }

    public Epic(String name, String discription, Status status, Integer subTaskId) {
        super(name, discription, status,subTaskId);
        this.subtasksIds = new ArrayList<>();
        this.setTaskTypes(TaskTypes.EPIC);
    }




    public void setSubtasksIds(List<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public List<Integer> getSubTaskId() {
        return this.subtasksIds;
    }

    public void removeSubTask(Integer taskId) {
        this.subtasksIds.remove(taskId);
    }

    public void updateSubTaskId(int subTaskId) {
        if(this.subtasksIds==null){
            this.subtasksIds = new ArrayList<>();
            this.subtasksIds.add(subTaskId);
        } else if (!this.subtasksIds.contains(subTaskId)) {
            this.subtasksIds.add(subTaskId);
        }
    }

    public void clearSubTaskId(){
        this.subtasksIds.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime(){
        return this.endTime;
    }

    @Override
    public String toString() {
        String subTask = "";
        if (subtasksIds != null) {
            for (Integer subTaskId : subtasksIds) {
                subTask = subTask + " " + Integer.toString(subTaskId);
            }
        } else {
            subTask = "";
        }
        return "Epic{" + "name='" + getName() + "', discription='" + getDescription() + "', identification='"
                + getIdentification() + "', status='" + getStatus()
                + "', subtaskId='[" + subTask + "]' " + "', duration='" + getDuration() + "', startTime='" + getStartTime() + "', endTime='" + getEndTime() + '}';
    }

    @Override
    public JsonObject toJson(){
        JsonObject jsonTask = new JsonObject();
        JsonObject taskDetails = new JsonObject();
        taskDetails.addProperty("type",getTaskTypes().toString());
        taskDetails.addProperty("name", getName());
        taskDetails.addProperty("status",getStatus().toString());
        taskDetails.addProperty("description", getDescription());
        taskDetails.addProperty("duration",getDuration());
        taskDetails.addProperty("startTime", String.valueOf(getStartTime()));
        taskDetails.addProperty("endTime",String.valueOf(getEndTime()));
        jsonTask.add(String.valueOf(getIdentification()), taskDetails);
        return jsonTask;

    }



}
