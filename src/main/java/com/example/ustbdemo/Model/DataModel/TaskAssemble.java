package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "taskassemble")
@Data
@ToString
public class TaskAssemble {
    @Id
    @GeneratedValue
    private Long taid; // 题目id

    @Column(name = "tid")
    private Long tid;

    @Column(name = "simuid")
    private Long simuid;

    @Column(name = "instrid")
    private Long instrid;

    public TaskAssemble(){}

    public Long getTaid() {
        return taid;
    }

    public void setTaid(Long taid) {
        this.taid = taid;
    }



    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getSimuid() {
        return simuid;
    }

    public void setSimuid(Long simuid) {
        this.simuid = simuid;
    }

    public Long getInstrid() {
        return instrid;
    }

    public void setInstrid(Long instrid) {
        this.instrid = instrid;
    }
}