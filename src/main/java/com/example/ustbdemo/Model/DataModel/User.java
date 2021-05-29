package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.bind.annotation.PutMapping;

import javax.persistence.*;
import java.util.Date;

import static org.apache.http.client.utils.DateUtils.parseDate;

@Entity
@Table(name = "user")
@Data
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long uid;

    @Column(name = "utype")
    private Long utype; // 用户类型 0-管理员、1-老师、2-学生

    @Column(name = "username", length = 255, unique = true)
    private String username; // 用户名

    @Column(name = "upasswd", length = 255)
    private String upasswd; // 密码

    @Column(name = "udis", length = 255)
    private String udis;

    @Column(name = "update_at")
    private Date update_at; // 修改密码时间

    @Column(name = "lock_at")
    private Date lock_at ; // 账户被锁时间

    @Column(name = "lock_times")
    private Long lock_times; // 密码错误次数



    public User(){}

    public User(String username, String upasswd, Long utype){
        this.username = username;
        this.upasswd = upasswd;
        this.utype = utype;
    }

    public User(String username, String upasswd, String udis, Long lock_times) {
        this.username = username;
        this.upasswd = upasswd;
        this.udis = udis;
        this.lock_times = lock_times;
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

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public Date getLock_at() {
        return lock_at;
    }

    public void setLock_at(Date lock_at) {
        this.lock_at = lock_at;
    }

    public Long getLock_times() {
        return lock_times;
    }

    public void setLock_times(Long lock_times) {
        this.lock_times = lock_times;
    }
}
