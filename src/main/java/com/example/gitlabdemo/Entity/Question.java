package com.example.gitlabdemo.Entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "question")
@Data
@ToString
public class Question {
    @Id
    @GeneratedValue
    private Long qid;

//    题目名称
    @Column(name = "qname", length = 32, unique = true)
    private String qname;

    @Column(name = "qcontent", length = 32)
    private String qcontent;

    @Column(name = "begin_at")
    private Date begindate;

//    截止时间
    @Column(name = "end_at")
    private Date enddate;

//    布置给的班级id
    @Column(name = "cid")
    private Long cid;

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public String getQcontent() {
        return qcontent;
    }

    public void setQcontent(String qcontent) {
        this.qcontent = qcontent;
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
}
