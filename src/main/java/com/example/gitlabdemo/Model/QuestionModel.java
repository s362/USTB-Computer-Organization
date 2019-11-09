package com.example.gitlabdemo.Model;

import java.util.Date;
import java.util.List;

public class QuestionModel {
    Long qid;

    private String qname;

    private Date beginDate;

    private Date endDate;

    List<TaskScore> taskScores;

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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<TaskScore> getTaskScores() {
        return taskScores;
    }

    public void setTaskScores(List<TaskScore> taskScores) {
        this.taskScores = taskScores;
    }
}
