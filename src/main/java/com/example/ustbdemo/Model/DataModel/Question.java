package com.example.ustbdemo.Model.DataModel;

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

    @Column(name = "qname", length = 100)
    private String qname;

    @Column(name = "qdis", length = 10000)
    private String qdis;

    @Column(name = "create_at")
    private Date createdate;

    @Column(name = "end_at")
    private Date enddate;



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

    public String getQdis() {
        return qdis;
    }

    public void setQdis(String qdis) {
        this.qdis = qdis;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
}
