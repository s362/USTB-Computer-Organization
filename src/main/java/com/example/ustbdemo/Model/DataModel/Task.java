package com.example.ustbdemo.Model.DataModel;

import com.example.ustbdemo.Util.FileUtil;
import com.example.ustbdemo.Util.OSUtil;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
@Data
@ToString
public class Task {             //题库对应的数据库表
    @Id
    @GeneratedValue
    private Long tid; // 题目id

    @Column(name = "ttype")
    private Long ttype; // 题目类型   0L——Verilog   1L——汇编

    @Column(name = "tname", length = 100)
    private String tname; // 题目名称

    @Column(name = "tdis", length = 10000)
    private String tdis; // 题目描述

    @Column(name = "simuid1")
    private Long simuid1; // 仿真器 2 id

    @Column(name = "simuid2")
    private Long simuid2; // 仿真器1 id

    @Column(name = "instrid")
    private Long instrid; // 指令说明书id

    @Column(name = "taskFilePath")
    private String taskFilePath; // 正确工程存放的位置 ，这个暂时也没用

    @Column(name = "exampleFilePath")
    private String exampleFilePath; // 样例工程存放的位置 ,这个目前没用

    @Column(name = "simuPicPath1")
    private String simuPicPath1; // 第一个仿真图存放的位置

    @Column(name = "simuPicPath2")
    private String simuPicPath2; // 第二个仿真图存放的位置

    @Column(name = "course_id")
    private Long courseId;          //该题目对应的课程id

    @Column(name = "is_public")
    private Integer isPublic;           //该题目是否公开到公共题库中

    public static final String EXAMPLE_TaskFile = OSUtil.isLinux() ?
            FileUtil.STATIC_PATH_LINUX + "exampleTaskFile.zip" : FileUtil.STATIC_PATH_WIN + "exampleTaskFile.zip";


    public Task(){}

    public Task(String tname, String tdis, Long ttype){
        this.tname = tname;
        this.tdis = tdis;
        this.ttype = ttype;
    }

    public String getExampleFilePath() {
        return exampleFilePath;
    }

    public void setExampleFilePath(String exampleFilePath) {
        this.exampleFilePath = exampleFilePath;
    }

    public Long getSimuid1() {
        return simuid1;
    }

    public void setSimuid1(Long simuid1) {
        this.simuid1 = simuid1;
    }

    public Long getSimuid2() {
        return simuid2;
    }

    public void setSimuid2(Long simuid2) {
        this.simuid2 = simuid2;
    }

    public Long getInstrid() {
        return instrid;
    }

    public void setInstrid(Long instrid) {
        this.instrid = instrid;
    }

    public String getTaskFilePath() {
        return taskFilePath;
    }

    public void setTaskFilePath(String taskFilePath) {
        this.taskFilePath = taskFilePath;
    }

    public String getSimuPicPath1() {
        return simuPicPath1;
    }

    public void setSimuPicPath1(String simuPicPath1) {
        this.simuPicPath1 = simuPicPath1;
    }

    public String getSimuPicPath2() {
        return simuPicPath2;
    }

    public void setSimuPicPath2(String simuPicPath2) {
        this.simuPicPath2 = simuPicPath2;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getTtype() {
        return ttype;
    }

    public void setTtype(Long ttype) {
        this.ttype = ttype;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdis() {
        return tdis;
    }

    public void setTdis(String tdis) {
        this.tdis = tdis;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }
}