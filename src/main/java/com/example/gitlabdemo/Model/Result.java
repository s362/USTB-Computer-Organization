package com.example.gitlabdemo.Model;

public class Result {
    private boolean success = true;
    private String message ;
    private Object object;
    private Object note;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public Result(Object object) {
        this.object = object;
    }

    public Result() {
    }

    public Result(String message) {

        this.message = message;
        this.success = false;
    }

    public Result(String message, boolean success) {

        this.message = message;
        this.success = success;
    }
}
