package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "question_task")
@Data
@ToString
public class Question_Task {
    @Id
    @GeneratedValue
    private Long qtid;

//    一个question对应多个task
    @Column(name = "tid", nullable = false)
    private Long tid; // 题目id

    @Column(name = "qid", nullable = false)
    private Long qid; // 作业id

    public Question_Task(){}

    public Question_Task(Long qid, Long tid){
        this.tid = tid;
        this.qid = qid;
    }

    public Long getQtid() {
        return qtid;
    }

    public void setQtid(Long qtid) {
        this.qtid = qtid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }
}
