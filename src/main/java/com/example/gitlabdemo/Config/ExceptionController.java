package com.example.gitlabdemo.Config;

import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController {
    // 捕捉shiro的异常
    @ExceptionHandler(value = ShiroException.class)
    public ResponseBean handle401(ShiroException e) {
        System.out.println("lalal");
        return new ResponseBean(401, e.getMessage(), null);
    }

    @ExceptionHandler(value = org.apache.shiro.authc.AuthenticationException.class)
    public ResponseBean handle401_P(org.apache.shiro.authc.AuthenticationException e) {
        System.out.println("lalal");
        return new ResponseBean(401, e.getMessage(), null);
    }
//
//    // 捕捉UnauthorizedException
    @ExceptionHandler(Throwable.class)
    public ResponseBean handle401() {
        return new ResponseBean(401, "Unauthorized", null);
    }
//
    // 捕捉UnauthorizedException
    @ExceptionHandler(value = org.apache.shiro.authc.AuthenticationException.class)
    public ResponseBean handleAu() {
        return new ResponseBean(401, "Unauthorized", null);
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
        System.out.println(ex.toString());
        ex.printStackTrace();
        return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);
    }

    private HttpStatus getStatus(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}