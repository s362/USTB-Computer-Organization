package com.example.ustbdemo.Model.UtilModel;

import com.example.ustbdemo.Model.DataModel.Score;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import java.util.List;

public class ilabResult {
    private String username ;
    private String title = "流水线CPU冲突解决方法虚拟仿真实验";
    private long status ;
    private long score = 0l;
    private long startTime = System.currentTimeMillis();
    private long endTime ;
    private long timeUsed ;
    private long appid;
    private String originId;
    private List<steps> steps;

    public ilabResult(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.timeUsed = ( endTime-startTime)/1000;
    }

    public long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(long timeUsed) {
        this.timeUsed = timeUsed;
    }

    public long getAppid() {
        return appid;
    }

    public void setAppid(long appid) {
        this.appid = appid;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public List<com.example.ustbdemo.Model.UtilModel.steps> getSteps() {
        return steps;
    }

    public void setSteps(List<com.example.ustbdemo.Model.UtilModel.steps> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "ilabResult{" +
                "username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", score=" + score +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeUsed=" + timeUsed +
                ", appid=" + appid +
                ", originId='" + originId + '\'' +
                ", steps=" + steps +
                '}';
    }


}
