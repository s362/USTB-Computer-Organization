package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.stream.events.Namespace;

@Entity
@Table(name = "assemble_choose")
@Data
@ToString
public class Assemble_Choose {
    @Id
    @GeneratedValue
    private Long tcid; // 选择题id

    @Column(name = "tid")
    private Long tid; // 题目id，因为一个题目有多个选择题，这里是一对多关系

    @Column(name = "tpart")
    private Integer tpart;  //该选择题属于题目的哪个部分，1 2 3分别代表三个部分汇编代码后、理想流水线原理单步仿真后、关键技术原理仿真后

    @Column(name = "discri")
    private String  discri; // 选择题描述

    @Column(name = "options", length = 10000)
    private String options; // 选项

    @Column(name = "answers", length = 255)
    private String answers; // 答案

    public Assemble_Choose(){};

    public Assemble_Choose(String discri, String options, String answers){
        this.discri = discri;
        this.options = options;
        this.answers = answers;
    }

    public String getDiscri() {
        return discri;
    }

    public void setDiscri(String discri) {
        this.discri = discri;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
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

    public Integer getTpart() {
        return tpart;
    }

    public void setTpart(Integer tpart) {
        this.tpart = tpart;
    }
}