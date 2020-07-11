package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.bind.annotation.PutMapping;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long uid;

    @Column(name = "utype")
    private Long utype; // 用户类型

    @Column(name = "username", length = 255, unique = true)
    private String username; // 用户名

    @Column(name = "upasswd", length = 255)
    private String upasswd; // 密码

    @Column(name = "udis", length = 255)
    private String udis;

    public User(){}

    public User(String username, String upasswd, Long utype){
        this.username = username;
        this.upasswd = upasswd;
        this.utype = utype;
    }

    public String getUpasswd() {
        return upasswd;
    }

    public void setUpasswd(String upasswd) {
        this.upasswd = upasswd;
    }

    public Long getUtype() {
        return utype;
    }

    public void setUtype(Long utype) {
        this.utype = utype;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUdis() {
        return udis;
    }

    public void setUdis(String udis) {
        this.udis = udis;
    }

    public String getPasswd() {
        return upasswd;
    }

    public void setPasswd(String passwd) {
        this.upasswd = passwd;
    }
}
