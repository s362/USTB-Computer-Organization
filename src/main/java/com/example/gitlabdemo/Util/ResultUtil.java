package com.example.gitlabdemo.Util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultUtil {
    static public ResponseEntity<Result> getResult(Result result, HttpStatus httpStatus){
        return new ResponseEntity<>(result, httpStatus);
    }
}
