package com.example.gitlabdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "question")
@Data
@ToString
public class Question {
    @Id
    @GeneratedValue
    private Long qid;

    @Column(name = "qname", length = 32)
    private String qname;

    @Column(name = "create_at")
    private Date createdate;

    @Column(name = "update_at")
    private Date updatedate;



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

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

}
