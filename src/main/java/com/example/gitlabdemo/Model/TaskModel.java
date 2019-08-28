package com.example.gitlabdemo.Model;

import java.util.List;

public class TaskModel {
    String task_id;
    String task_content;
    String task_title;
    List<TaskFile> taskFiles;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public List<TaskFile> getTaskFiles() {
        return taskFiles;
    }

    public void setTaskFiles(List<TaskFile> taskFiles) {
        this.taskFiles = taskFiles;
    }

    @Override
    public String toString() {
        return task_id + "\n" + task_title + "\n" + task_content + "\n" + taskFiles.toString();
    }
}
