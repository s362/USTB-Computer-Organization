package com.example.ustbdemo.Model.GitModel;

import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@ToString
public class TaskModel {
    String task_id;
    List<TaskFile> taskFiles;
    List<TaskFile> exampleFiles;
    TaskFile configJson;

    public TaskModel(){}

    public TaskModel(String task_id){
        this.task_id = task_id;
        taskFiles = new LinkedList<>();
        exampleFiles = new LinkedList<>();
    }

    public TaskFile getConfigJson() {
        return configJson;
    }

    public void setConfigJson(TaskFile configJson) {
        this.configJson = configJson;
    }

    public List<TaskFile> getExampleFiles() {
        return exampleFiles;
    }

    public void setExampleFiles(List<TaskFile> exampleFiles) {
        this.exampleFiles = exampleFiles;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public List<TaskFile> getTaskFiles() {
        return taskFiles;
    }

    public void setTaskFiles(List<TaskFile> taskFiles) {
        this.taskFiles = taskFiles;
    }

}
