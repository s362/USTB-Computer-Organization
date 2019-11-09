package com.example.gitlabdemo.Entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "course")
@Data
@ToString
public class Course {
    @Id
    @GeneratedValue
    private Long cid;

    @Column(name = "cname", length = 32, unique = true)
    private String cname;

    @Column(name = "create_at")
    private Date createdate;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}
