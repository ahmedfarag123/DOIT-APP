package com.example.doit1;


public class TaskModel {
    private String taskId;
    private String taskName;
    private String worker1;
    private String worker2;
    private String worker3;
    private int timeFrom;
    private int timeTo;
    private String imageUrl;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String voiceUrl;

    public TaskModel() {

    }

    public TaskModel(String taskId, String taskName, String worker1, String worker2, String worker3, int timeFrom, int timeTo, String imageUrl, String imageUrl1, String imageUrl2, String imageUrl3, String voiceUrl) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.worker1 = worker1;
        this.worker2 = worker2;
        this.worker3 = worker3;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.imageUrl = imageUrl;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.voiceUrl = voiceUrl;
    }


    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getWorker1() { return worker1; }
    public void setWorker1(String worker1) { this.worker1 = worker1; }
    public String getWorker2() { return worker2; }
    public void setWorker2(String worker2) { this.worker2 = worker2; }
    public String getWorker3() { return worker3; }
    public void setWorker3(String worker3) { this.worker3 = worker3; }
    public int getTimeFrom() { return timeFrom; }
    public void setTimeFrom(int timeFrom) { this.timeFrom = timeFrom; }
    public int getTimeTo() { return timeTo; }
    public void setTimeTo(int timeTo) { this.timeTo = timeTo; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getImageUrl1() { return imageUrl1; }
    public void setImageUrl1(String imageUrl1) { this.imageUrl1 = imageUrl1; }
    public String getImageUrl2() { return imageUrl2; }
    public void setImageUrl2(String imageUrl2) { this.imageUrl2 = imageUrl2; }
    public String getImageUrl3() { return imageUrl3; }
    public void setImageUrl3(String imageUrl3) { this.imageUrl3 = imageUrl3; }
    public String getVoiceUrl() { return voiceUrl; }
    public void setVoiceUrl(String voiceUrl) { this.voiceUrl = voiceUrl; }

    public String getAssignedEmployees() {
        return worker1 + ", " + worker2 + ", " + worker3;
    }

}



