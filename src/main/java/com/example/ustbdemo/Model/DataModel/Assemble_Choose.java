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
    private Long tcid; // 题目id

    @Column(name = "tid")
    private Long tid;

    @Column(name = "discri")
    private String  discri;

    @Column(name = "options", length = 10000)
    private String options;

    @Column(name = "answers", length = 255)
    private String answers;

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
}