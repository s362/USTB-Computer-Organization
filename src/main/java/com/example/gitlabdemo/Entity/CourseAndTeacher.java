package com.example.gitlabdemo.Entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "courseandteacher")
@Data
@ToString
public class CourseAndTeacher {
    @Id
    @GeneratedValue
    private Long csid;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "cid")
    private Long cid;

    @Column(name = "utype")
    private Long utype;

    @Column(name = "join_time")
    private Date joinTime;

    public CourseAndTeacher(){

    }

    public CourseAndTeacher(Long cid, Long uid){
        this.cid = cid;
        this.uid = uid;
    }

    public Long getUtype() {
        return utype;
    }

    public void setUtype(Long utype) {
        this.utype = utype;
    }

    public Long getCsid() {
        return csid;
    }

    public void setCsid(Long csid) {
        this.csid = csid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }


}
