package com.example.gitlabdemo.Controller;


import com.example.gitlabdemo.Model.Result;
import com.example.gitlabdemo.Model.DataModel.User;
import com.example.gitlabdemo.Service.UserService;
import com.example.gitlabdemo.Shiro.JwtUtil;
import com.example.gitlabdemo.Util.ResultUtil;
//import com.example.gitlabdemo.Util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/signin")
public class LoginController {
    @Autowired
    UserService userService;

//    登录验证,正确就返回jwt，错误返回报错信息
    @PostMapping(value = "/", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> login(@RequestBody User user){
        System.out.println(user);
        try{
            if (userService.getByUsernameAndPwd(user) != null){
                String token = JwtUtil.sign(user.getUusername(), user.getUpassword());
                if(token != null){
                    Result result = new Result();
                    result.setObject(token);
                    return ResultUtil.getResult(result, HttpStatus.OK);
                }
            }
            System.out.println("无此用户");
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        }
    }

//    添加用户
    @PostMapping("/adduser")
    public ResponseEntity<Result> addUser(String uusername, String upassword){
        User user = new User();

        user.setUusername(uusername);
        user.setUpassword(upassword);
        user.setCreatedate(new Date(new java.util.Date().getTime()));
        System.out.println(user);
        if(userService.addUser(user) == 0){
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } else{
            return ResultUtil.getResult(new Result("插入失败", false), HttpStatus.BAD_REQUEST);
        }
    }

//    错误返回
    @PostMapping("/401")
    public ResponseEntity<Result> error(){
        return ResultUtil.getResult(new Result("登录失败", false), HttpStatus.BAD_REQUEST);
    }
}
