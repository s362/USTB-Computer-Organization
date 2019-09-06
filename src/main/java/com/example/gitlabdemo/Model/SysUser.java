package com.example.gitlabdemo.Model;

import lombok.Data;
import lombok.ToString;

@Data
public class SysUser {

    private String id;
    private String username;
    private String password;

    public SysUser(String username,String password){
        this.username = username;
        this.password = password;
    }

}