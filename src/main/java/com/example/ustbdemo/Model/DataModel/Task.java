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

    @Column(name = "simuid")
    private Long simuid;

    @Column(name = "instrid")
    private Long instrid;

    @Column(name = "taskFilePath")
    private String taskFilePath;

    @Column(name = "testFilePath")
    private String testFilePath;

    @Column(name = "exampleFilePath")
    private String exampleFilePath;

    @Column(name = "simuPicPath1")
    private String simuPicPath1;

    @Column(name = "simuPicPath2")
    private String simuPicPath2;

    public Task(){}

    public Task(String tname, String tdis, Long ttype){
        this.tname = tname;
        this.tdis = tdis;
        this.ttype = ttype;
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

    public String getTaskFilePath() {
        return taskFilePath;
    }

    public void setTaskFilePath(String taskFilePath) {
        this.taskFilePath = taskFilePath;
    }

    public String getTestFilePath() {
        return testFilePath;
    }

    public void setTestFilePath(String testFilePath) {
        this.testFilePath = testFilePath;
    }

    public String getExampleFilePath() {
        return exampleFilePath;
    }

    public void setExampleFilePath(String exampleFilePath) {
        this.exampleFilePath = exampleFilePath;
    }

    public String getSimuPicPath1() {
        return simuPicPath1;
    }

    public void setSimuPicPath1(String simuPicPath1) {
        this.simuPicPath1 = simuPicPath1;
    }

    public String getSimuPicPath2() {
        return simuPicPath2;
    }

    public void setSimuPicPath2(String simuPicPath2) {
        this.simuPicPath2 = simuPicPath2;
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