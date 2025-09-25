package tasks;
import com.google.gson.JsonObject;
import enums.Status;
import enums.TaskTypes;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private TaskTypes taskTypes;
    private int duration;
    private LocalDateTime startTime;
    private boolean collectData = false;




    public Task(String name, String description) { //Первичное создание
        this(name,description,Status.NEW);
    }

    public Task(String name,String description,Status status){ //Для теститования
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskTypes = TaskTypes.TASK;
    }

    public Task(String name, String description, Status status, int duration,LocalDateTime  startTime) { //Выгрузка
        this.name = name;
        this.description = description;
        this.status = status;
        collectData = true;
        this.duration = duration;
        this.startTime = startTime;
        this.taskTypes = TaskTypes.TASK;
    }

    public Task(String name, String description, Status status, Integer id) { //Выгрузка без даты
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.taskTypes = TaskTypes.TASK;
    }

    public Task(String name, String description, Status status, Integer id,int duration,LocalDateTime  startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        collectData = true; //Нужен для StringConverter
        this.duration = duration;
        this.startTime = startTime;
        this.taskTypes = TaskTypes.TASK;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIdentification() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public int getDuration(){
        return duration;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public TaskTypes getTaskTypes(){
        return taskTypes;
    }

    public boolean getCollectData(){
        return collectData;
    }



    public void setStatus(Status status) {
        this.status = status;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getEndTime(){
        if(startTime!=null) {
            return startTime.plusMinutes(duration);
        }
        return null;
    }

    public void setTaskTypes(TaskTypes taskTypes){
        this.taskTypes = taskTypes;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }




    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", identification=" + id +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime  + '\'' +
                ", endTime='" + getEndTime()  + '\'' +
                '}';
    }

    public JsonObject toJson() {
        JsonObject jsonTask = new JsonObject();
        JsonObject taskDetails = new JsonObject();
        taskDetails.addProperty("type",getTaskTypes().toString());
        taskDetails.addProperty("name", name);
        taskDetails.addProperty("status",status.toString());
        taskDetails.addProperty("description", description);
        taskDetails.addProperty("duration",duration);
        taskDetails.addProperty("startTime", String.valueOf(startTime));
        taskDetails.addProperty("endTime",String.valueOf(getEndTime()));
        jsonTask.add(String.valueOf(id), taskDetails);
        return jsonTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                duration == task.duration &&
                collectData == task.collectData &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                taskTypes == task.taskTypes &&
                Objects.equals(startTime, task.startTime);
    }

}
