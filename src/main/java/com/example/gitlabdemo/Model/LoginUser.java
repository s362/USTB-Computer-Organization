package com.example.gitlabdemo.Model;

public class LoginUser {
    private String uusername;

    private String upassword;

    private Long utype;

    public LoginUser(){}

    public LoginUser(String  uusername, String upassword){
        this.uusername = uusername;
        this.upassword = upassword;
    }

    public Long getUtype() {
        return utype;
    }

    public void setUtype(Long utype) {
        this.utype = utype;
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
