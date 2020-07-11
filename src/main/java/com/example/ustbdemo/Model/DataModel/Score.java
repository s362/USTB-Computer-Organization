package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "score")
@Data
@ToString
public class Score {
    @Id
    @GeneratedValue
    private Long sid;

    @Column(name = "uid")
    private Long uid; // 用户id

    @Column(name = "tid")
    private Long tid; // 题目id

    @Column(name = "tscore")
    private Long tscore; // 题目分数

    @Column(name = "update_at")
    private Date updatedate;


    public Score(){};

    public Score(Long uid, Long tid, Date updatedate){
        tscore = 0L;
        this.uid = uid;
        this.tid = tid;
        this.updatedate = updatedate;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getTscore() {
        return tscore;
    }

    public void setTscore(Long tscore) {
        this.tscore = tscore;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }
}
