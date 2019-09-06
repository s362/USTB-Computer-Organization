package com.example.gitlabdemo.Model;

public class User {
    private String task_id;
    private String user_id;

    public User(String user_id){
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }



    @Override
    public String toString() {
        return  this.task_id + "  " + this.user_id;
    }
}
