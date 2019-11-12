package com.example.gitlabdemo.Entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "coursegroup")
@Data
@ToString
public class CourseGroup {
    @Id
    @GeneratedValue
    private Long cgid;

    @Column(name = "cgname")
    private String cgname;

    @Column(name = "cid")
    private Long cid;

    public CourseGroup(){}

    public CourseGroup(Long cid, String cgname){
        this.cid = cid;
        this.cgname = cgname;
    }

    public Long getCgid() {
        return cgid;
    }

    public void setCgid(Long cgid) {
        this.cgid = cgid;
    }

    public String getCgname() {
        return cgname;
    }

    public void setCgname(String cgname) {
        this.cgname = cgname;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
}
