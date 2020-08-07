package com.example.ustbdemo.Model.DataModel;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * 暂存表，用于存储学生做汇编实验题时的进度，避免突然丢失
 */

@Entity
@Table(name = "stage")
@Data
@ToString
public class Stage {

    @Id
    @GeneratedValue
    private Long stageId;       //主键

    @Column(name = "uid")
    private Long uid;           //用户id

    @Column(name = "tid")
    private Long tid;           //题目id

    @Column(name = "tcid")
    private Long tcid;          //选择题id

    @Column(name = "simulator1_step")
    private Long simulator1Step;    //模拟器1的步数

    @Column(name = "simulator2_step")
    private Long simulator2Step;    //模拟器2的步数

    @Column(name = "code")
    private String code;            //汇编代码

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
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

    public Long getTcid() {
        return tcid;
    }

    public void setTcid(Long tcid) {
        this.tcid = tcid;
    }

    public Long getSimulator1Step() {
        return simulator1Step;
    }

    public void setSimulator1Step(Long simulator1Step) {
        this.simulator1Step = simulator1Step;
    }

    public Long getSimulator2Step() {
        return simulator2Step;
    }

    public void setSimulator2Step(Long simulator2Step) {
        this.simulator2Step = simulator2Step;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
