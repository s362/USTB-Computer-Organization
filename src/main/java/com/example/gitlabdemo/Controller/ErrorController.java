package com.example.gitlabdemo.Controller;

import com.example.gitlabdemo.Util.Result;
import com.example.gitlabdemo.Util.ResultUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ErrorController  {
    @RequestMapping(path = "/401")
    public ResponseEntity<Result> unauthorized() {
        System.out.println("返回400");
        return ResultUtil.getResult(new Result("登录失败", false), HttpStatus.BAD_REQUEST);
    }
}
