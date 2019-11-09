package com.example.gitlabdemo.Entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "questionandtask")
@Data
@ToString
public class QuestionAndTask {
    @Id
    @GeneratedValue
    private Long qtid;

    @Column(name = "qid")
    private Long qid;

    @Column(name = "tid")
    private Long tid;

    public QuestionAndTask(){

    }

    public QuestionAndTask(Long qid, Long tid){
        this.qid = qid;
        this.tid = tid;
    }

    public Long getQtid() {
        return qtid;
    }

    public void setQtid(Long qtid) {
        this.qtid = qtid;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }
}
