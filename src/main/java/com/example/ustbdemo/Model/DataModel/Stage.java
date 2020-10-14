package com.example.ustbdemo.Model.DataModel;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * 暂存表，用于存储学生做汇编实验题时的进度，避免突然丢失
 * tid: 题目id
 * step：目前进入到第几阶段（1,2,3,4,5,6,7）        1:编写汇编代码，
 *                                                 2：理想流水线 流水线视角仿真，
 *                                                 3：理想流水线，指令视角仿真，
 *                                                 4：冲关答题，
 *                                                 5：实际流水线 流水线视角仿真，
 *                                                 6：理想流水线 指令视角仿真，
 *                                                 7：冲关答题
 * code：学生写的代码
 * simulatorStep:流水线的目前的周期数 ，即保存时流水线仿真运行到了哪个周期，整数
 * tcid：当前选择题到了哪题
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

    @Column(name = "simulator_step")
    private Long simulatorStep;    //模拟器的步数

//    @Column(name = "simulator2_step")
//    private Long simulator2Step;    //模拟器2的步数

    @Column(name = "code")
    private String code;            //汇编代码

    @Column(name = "step")
    private Long step;          //当前进行的步骤

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSimulatorStep() {
        return simulatorStep;
    }

    public void setSimulatorStep(Long simulatorStep) {
        this.simulatorStep = simulatorStep;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
    }
}
