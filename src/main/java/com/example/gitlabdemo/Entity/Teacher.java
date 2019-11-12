package com.example.gitlabdemo.Entity;

import com.example.gitlabdemo.Model.LoginUser;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "teacher")
@Data
@ToString
public class Teacher {
    @Id
    @GeneratedValue
    private Long uid;

    @Column(name = "uusername", length = 32, unique = true, nullable = false)
    private String uusername;

    @Column(name = "upassword", length = 32, nullable = false)
    private String upassword;

    @Column(name = "create_at")
    private Date createdate;

    @Column(name = "realname", length = 32, nullable = false)
    private String realname;

    @Column(name = "utype")
    private Long utype;


    public Teacher(String uusername, String upassword){
        this.uusername = uusername;
        this.upassword = upassword;
    }

    public Teacher(String uusername, String upassword, String realname){
        this.uusername = uusername;
        this.upassword = upassword;
        this.realname = realname;
    }

    public Teacher(){}

    public Long getUtype() {
        return utype;
    }

    public void setUtype(Long utype) {
        this.utype = utype;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUusername() {
        return uusername;
    }

    public void setUusername(String uusername) {
        this.uusername = uusername;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

}
