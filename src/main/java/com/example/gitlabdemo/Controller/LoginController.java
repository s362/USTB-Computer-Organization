package com.example.gitlabdemo.Controller;


import com.example.gitlabdemo.Model.Result;
import com.example.gitlabdemo.Model.SysUser;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.JudgeUtil;
import com.example.gitlabdemo.Util.TokenUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @PostMapping("/student")
    public Result login(SysUser sysUser, HttpServletResponse response){
        Map users = new HashMap();
        users.put("41624112", "41624112");
        users.put("41624113", "41624113");

        System.out.println(sysUser);
        try{

            if (users.get(sysUser.getUsername()).equals(sysUser.getPassword())){
                String token = TokenUtil.sign(sysUser);
                if(token != null){
                    response.setHeader("Authorization", token);
                    response.setStatus(org.apache.http.HttpStatus.SC_OK);
                    Result result = new Result();
                    result.setObject(token);
                    return result;
                }
            }
            response.setStatus((org.apache.http.HttpStatus.SC_BAD_REQUEST));
            return new Result("密码错误");
        } catch (Exception e){
            System.out.println(e.toString());
            response.setStatus((org.apache.http.HttpStatus.SC_BAD_REQUEST));
            return new Result("密码错误");
        }
    }
}
