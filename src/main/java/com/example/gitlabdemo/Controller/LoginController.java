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

import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/signin")
public class LoginController {
    @Autowired
    UserService userService;

    /**
     * 登录验证
     * @param user 用户名和密码
     * @return
     */
    @PostMapping(value = "/", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> login(@RequestBody User user){
        System.out.println(user);
        try{
//            如果用户名和密码正确，则返回token
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

    /**
     * 添加用户
     * @param uusername 用户名
     * @param upassword 密码
     * @return
     */
    @PostMapping("/adduser")
    public ResponseEntity<Result> addUser(String uusername, String upassword){
        User user = new User();

        user.setUusername(uusername);
        user.setUpassword(upassword);
        user.setCreatedate(new Date());
        System.out.println(user);
        if(userService.addUser(user) == 0){
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } else{
            return ResultUtil.getResult(new Result("插入失败", false), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 登录错误返回报错信息
     * @return
     */
    @PostMapping("/401")
    public ResponseEntity<Result> error(){
        return ResultUtil.getResult(new Result("登录失败", false), HttpStatus.BAD_REQUEST);
    }
}
