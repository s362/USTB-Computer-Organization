package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assemble_code_score")        //记录汇编代码提交正确情况的数据库表
@Data
@ToString
public class Assemble_Code_Score {

    @Id
    @GeneratedValue
    private Long assembleCodeScoreId;

    @Column(name = "tid")
    private Long tid; // 题目id

    @Column(name = "uid")
    private Long uid; // 用户id

    @Column(name = "assemble_code_score")
    private Long assembleCodeScore; // 汇编代码得分

    @Column(name = "times")
    private Long times;  //学生提交次数


    @Column(name = "update_at")
    private Date updatedate;

    public Long getAssembleCodeScoreId() {
        return assembleCodeScoreId;
    }

    public void setAssembleCodeScoreId(Long assembleCodeScoreId) {
        this.assembleCodeScoreId = assembleCodeScoreId;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAssembleCodeScore() {
        return assembleCodeScore;
    }

    public void setAssembleCodeScore(Long assembleCodeScore) {
        this.assembleCodeScore = assembleCodeScore;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public void addTimes(){
        this.times+=1;
    }
}
