package com.example.gitlabdemo.Util;

import com.example.gitlabdemo.Model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultUtil {
    static ResponseEntity<Result> getResult(Result result, HttpStatus httpStatus){
        return new ResponseEntity<>(result, httpStatus);
    }
}
