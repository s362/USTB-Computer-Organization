package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Service.UserService;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    UserService userService;
    public static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @PostMapping(value = "/", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> login(@RequestBody User user){
        logger.info(user.toString());
        User testUser=userService.findByUserName(user.getUsername());
        logger.info(testUser.toString());
        try{
            if (userService.getByUsernameAndPwd(testUser.getUsername(), testUser.getPasswd()) != null){
//                签名，生成jwt
                String token = JwtUtil.sign(testUser.getUsername());
                if(token != null){
                    Result result = new Result();
                    result.setObject(token);
                    return ResultUtil.getResult(result, HttpStatus.OK);
                }
            }
            logger.info("无此用户");
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            logger.info(e.toString());
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        }
    }

}
