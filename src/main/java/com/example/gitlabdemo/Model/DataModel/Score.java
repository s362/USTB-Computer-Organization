package com.example.gitlabdemo.Model.DataModel;

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
    private Long uid;

    @Column(name = "task_id")
    private Long  tid;

    @Column(name = "tscore")
    private Long tscore;

    @Column(name = "update_at")
    private Date updatedate;

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
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
}
