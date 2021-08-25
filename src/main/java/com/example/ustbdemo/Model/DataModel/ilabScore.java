package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "ilabScore")
@Data
@ToString
public class ilabScore implements Comparable<ilabScore>{

    @Id
    @GeneratedValue
    private Long uid;

    @Column(name = "step",length = 255)
    private String step;

    @Column(name = "creattime")
    private String creatTime;

    @Column(name = "endtime")
    private String endTime;

    @Column(name = "username",length = 255)
    private String username;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(ilabScore o) {
       return Integer.parseInt(this.step) -  Integer.parseInt(o.step);
    }
}
