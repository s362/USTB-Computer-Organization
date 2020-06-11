package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.stream.events.Namespace;

@Entity
@Table(name = "taskchoose")
@Data
@ToString
public class TaskChoose {
    @Id
    @GeneratedValue
    private Long tcid; // 题目id

    @Column(name = "tid")
    private Long tid;

    @Column(name = "taid")
    private Long taid;

    @Column(name = "options", length = 10000)
    private String options;

    @Column(name = "answers", length = 255)
    private String answers;

    public TaskChoose(){};

    public TaskChoose(Long tid, String options, String answers){
        this.tid = tid;
        this.options = options;
        this.answers = answers;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getTaid() {
        return taid;
    }

    public void setTaid(Long taid) {
        this.taid = taid;
    }

    public Long getTcid() {
        return tcid;
    }

    public void setTcid(Long tcid) {
        this.tcid = tcid;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}