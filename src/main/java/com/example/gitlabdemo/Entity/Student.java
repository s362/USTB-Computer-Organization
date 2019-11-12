package com.example.gitlabdemo.Entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student")
@Data
@ToString
public class Student {
    @Id
    @GeneratedValue
    private Long uid;

    @Column(name = "uusername", length = 32, unique = true, nullable = false)
    private String uusername;

    @Column(name = "upassword", length = 32, nullable = false)
    private String upassword;

    @Column(name = "create_at")
    private Date createdate;

    @Column(name = "nickname", length = 32, nullable = false)
    private String nickname;


    public Student(String uusername, String upassword){
        this.uusername = uusername;
        this.upassword = upassword;
    }

    public Student(String uusername, String upassword, String nickname){
        this.uusername = uusername;
        this.upassword = upassword;
        this.nickname = nickname;
    }

    public Student(){}

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
