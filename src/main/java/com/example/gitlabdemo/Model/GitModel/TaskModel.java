/**
 * 题目信息
 */
package com.example.gitlabdemo.Model.GitModel;

import lombok.ToString;

import java.util.List;

@ToString
public class TaskModel {
    // gitlab下的项目名
    String task_id;
    // 题目内容（读取content.txt中的内容）
    String task_content;
    // 题目标题
    String task_title;
    // 题目下的所有文件
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

}
