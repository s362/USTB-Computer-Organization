package com.example.ustbdemo.Model.GitModel;

import com.example.ustbdemo.Model.DataModel.Score;
import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Service.TaskService;

import java.sql.Date;

public class TaskScore {
    private Long tid;
    private String tname;
    private String tdis;
    private Long tscore;

    public TaskScore(){};

    public TaskScore(Task task){
        this.tid = task.getTid();
        this.tname = task.getTname();
        this.tdis = task.getTdis();
    }

    public TaskScore(Task task, Score score){
        this.tid = task.getTid();
        this.tname = task.getTname();
        this.tdis = task.getTdis();
        this.tscore = score.getTscore();
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getTdis() {
        return tdis;
    }

    public void setTdis(String tdis) {
        this.tdis = tdis;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Long getTscore() {
        return tscore;
    }

    public void setTscore(Long tscore) {
        this.tscore = tscore;
    }
}
