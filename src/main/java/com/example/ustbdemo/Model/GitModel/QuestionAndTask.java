package com.example.ustbdemo.Model.GitModel;

import com.example.ustbdemo.Model.DataModel.Question;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class QuestionAndTask {
    Long qid;

    private String qname;

    private String qdis;

    private Date enddate;

    List<TaskScore> taskScores;

    public QuestionAndTask(){}

    public QuestionAndTask(Question question){
        this.qid = question.getQid();
        this.qname = question.getQname();
        this.qdis = question.getQdis();
        this.enddate = question.getEnddate();
        taskScores = new LinkedList<>();
    }

    public String getQdis() {
        return qdis;
    }

    public void setQdis(String qdis) {
        this.qdis = qdis;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public List<TaskScore> getTaskScores() {
        return taskScores;
    }

    public void setTaskScores(List<TaskScore> taskScores) {
        this.taskScores = taskScores;
    }
}
