package com.example.gitlabdemo.Controller;


import com.example.gitlabdemo.Entity.Teacher;
import com.example.gitlabdemo.Model.LoginUser;
import com.example.gitlabdemo.Util.Result;
import com.example.gitlabdemo.Entity.Student;
import com.example.gitlabdemo.Service.UserService;
import com.example.gitlabdemo.Shiro.JwtUtil;
import com.example.gitlabdemo.Util.ResultUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/signin")
public class LoginController {
    @Autowired
    UserService userService;

    /**
     * 登录验证
     * @param loginUser 用户名和密码
     * @return
     */
    @PostMapping(value = "/", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> login(@RequestBody LoginUser loginUser){
        System.out.println(loginUser);
        try{
//            如果用户名和密码正确，则返回token
            if (userService.getByUsernameAndPwd(loginUser)){
                String token;
                if(loginUser.getUtype() != 0){
                    loginUser.setUtype(userService.findTeacherByName(loginUser.getUusername()).getUtype());
                }
                token = JwtUtil.sign(loginUser);

                if(token != null){
                    Result result = new Result();
                    Map m1 = new HashMap();
                    m1.put("jwt", token);
                    m1.put("utype", loginUser.getUtype());
                    result.setObject(m1);
                    return ResultUtil.getResult(result, HttpStatus.OK);
                }
            }
//            System.out.println("无此用户");
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
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
