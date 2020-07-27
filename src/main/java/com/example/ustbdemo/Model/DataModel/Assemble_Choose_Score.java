package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assemble_choose_score")
@Data
@ToString
public class Assemble_Choose_Score {
    @Id
    @GeneratedValue
    private Long acsid;

    @Column(name = "tcid")
    private Long tcid; // 选择题id

    @Column(name = "uid")
    private Long uid; // 用户id

    @Column(name = "acscore")
    private Long acscore; // 选择题分数

    @Column(name = "times")
    private Long times;  //学生提交次数


    @Column(name = "update_at")
    private Date updatedate;

    public Assemble_Choose_Score(){};

    public Assemble_Choose_Score(Long uid, Long tcid, Date updatedate){
        this.uid = uid;
        this.tcid = tcid;
        this.acscore = 0L;
        this.updatedate = updatedate;
        this.times=0L;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Long getTcid() {
        return tcid;
    }

    public void setTcid(Long tcid) {
        this.tcid = tcid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAcsid() {
        return acsid;
    }

    public void setAcsid(Long acsid) {
        this.acsid = acsid;
    }

    public Long getAcscore() {
        return acscore;
    }

    public void setAcscore(Long acscore) {
        this.acscore = acscore;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public void addTimes(){this.times=this.times+1;}
}





