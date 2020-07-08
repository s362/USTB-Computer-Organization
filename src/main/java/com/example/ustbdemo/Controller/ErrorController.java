package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ErrorController  {
    public static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping(path = "/401")
    public ResponseEntity<Result> unauthorized() {
        logger.info("返回400");
        return ResultUtil.getResult(new Result("登录失败", false), HttpStatus.BAD_REQUEST);
    }
}
