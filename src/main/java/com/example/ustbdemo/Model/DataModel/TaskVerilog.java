package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "task")
@Data
@ToString
public class TaskVerilog {
    @Id
    @GeneratedValue
    private Long tvid; // 题目id

    @Column(name = "tid")
    private Long tid;

    public TaskVerilog(){}

    public Long getTvid() {
        return tvid;
    }

    public void setTvid(Long tvid) {
        this.tvid = tvid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }
}