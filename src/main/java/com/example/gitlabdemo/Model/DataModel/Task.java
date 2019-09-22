package com.example.gitlabdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "task")
@Data
@ToString
public class Task {
    @Id
    @GeneratedValue
    private Long tid;

    @Column(name = "qname", length = 32)
    private String tname;

    @Column(name = "create_at")
    private Date createdate;

    @Column(name = "update_at")
    private Date updatedate;

    @Column(name = "qid")
    private Long qid;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
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