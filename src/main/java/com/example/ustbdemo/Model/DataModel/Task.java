package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
@Data
@ToString
public class Task {
    @Id
    @GeneratedValue
    private Long tid; // 题目id

    @Column(name = "ttype")
    private Long ttype; // 题目类型

    @Column(name = "tname", length = 100)
    private String tname;

    @Column(name = "tdis", length = 10000)
    private String tdis;

    public Task(){}

    public Task(String tname, String tdis, Long ttype){
        this.tname = tname;
        this.tdis = tdis;
        this.ttype = ttype;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getTtype() {
        return ttype;
    }

    public void setTtype(Long ttype) {
        this.ttype = ttype;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdis() {
        return tdis;
    }

    public void setTdis(String tdis) {
        this.tdis = tdis;
    }
}