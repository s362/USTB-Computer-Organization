package com.example.gitlabdemo.Config;

import com.example.gitlabdemo.Model.Result;
import com.example.gitlabdemo.Util.ResultUtil;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

/**
 * @author: fxbin
 * @datetime: 2018/7/5 18:29
 * @description:
 */
@RestControllerAdvice
public class MyExceptionAdvice {

    /**
     * 捕捉所有Shiro异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ShiroException.class)
    public ResponseEntity<Result> handle401(ShiroException e) {
        return ResultUtil.getResult(new Result("121"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result> handle401(AuthenticationException e) {
        System.out.println(e.toString());
        return ResultUtil.getResult(new Result("121"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Result> handle401(UnauthenticatedException e) {
        return ResultUtil.getResult(new Result("121"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handle401(Exception e) {
        System.out.println(2020);
        return ResultUtil.getResult(new Result("121"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 捕捉UnauthorizedException自定义异常
     * @return
     */
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(CustomUnauthorizedException.class)
//    public ResponseEntity<Result> handle401(CustomUnauthorizedException e) {
//        return ResultUtil.getResult(new Result("121"), HttpStatus.BAD_REQUEST);
//    }
}
