package com.example.ustbdemo.Model.DataModel;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verilog_run_times")        //记录verilog代码提交情况的数据库表
@Data
@ToString
public class VerilogRunTimes {

    @Id
    @GeneratedValue
    private Long timesId;       //主键

    @Column(name = "tid")
    private Long tid; // 题目id

    @Column(name = "uid")
    private Long uid; // 用户id

    @Column(name = "times")
    private Long times;  //学生提交次数

    @Column(name = "update_at")
    private Date updatedate;

    @Column(name = "result_svg")  //存放提交之后的波形图片，使用svg字符串的格式存储
    private String resultSvg;

    public Long getTimesId() {
        return timesId;
    }

    public void setTimesId(Long timesId) {
        this.timesId = timesId;
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

    public String getResultSvg() {
        return resultSvg;
    }

    public void setResultSvg(String resultSvg) {
        this.resultSvg = resultSvg;
    }
}
