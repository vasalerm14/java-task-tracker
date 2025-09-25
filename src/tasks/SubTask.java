package tasks;
import com.google.gson.JsonObject;
import enums.Status;
import enums.TaskTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    private int epicId;


    public SubTask(String name, String discription, Status status, int epicId) {

        super(name, discription,status);
        this.setTaskTypes(TaskTypes.SUBTASK);
        this.epicId =  epicId;
    }

    public SubTask(String name, String discription, Status status, int id, int epicId) {
        super(name, discription, status, id);
        this.setTaskTypes(TaskTypes.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(String name, String discription, Status status, int epicId,int duration,LocalDateTime startTime) {
        super(name, discription,status,duration,startTime);
        this.setTaskTypes(TaskTypes.SUBTASK);
        this.epicId =  epicId;
    }





    public Integer getEpicId(){
        return epicId;
    }

    @Override
    public String toString() {

        return "SubTask{" + "name='" + getName() + "', discription='" + getDescription() + "', identification='"
                + getIdentification() + "', status='" + getStatus()
                + "', epicId=" + epicId + "', duration='" + getDuration() + "', startTime='" + getStartTime() + "', endTime='" + getEndTime() + '}';
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonTask = new JsonObject();
        JsonObject taskDetails = new JsonObject();
        taskDetails.addProperty("type",getTaskTypes().toString());
        taskDetails.addProperty("name", getName());
        taskDetails.addProperty("status",getStatus().toString());
        taskDetails.addProperty("description", getDescription());
        taskDetails.addProperty("epicId", getEpicId());
        taskDetails.addProperty("duration",getDuration());
        taskDetails.addProperty("startTime", String.valueOf(getStartTime()));
        taskDetails.addProperty("endTime",String.valueOf(getEndTime()));
        jsonTask.add(String.valueOf(getIdentification()), taskDetails);
        return jsonTask;
    }


}
