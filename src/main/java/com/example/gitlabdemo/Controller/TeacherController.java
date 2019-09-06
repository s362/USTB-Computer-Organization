package com.example.gitlabdemo.Controller;


import com.example.gitlabdemo.Model.Result;
import com.example.gitlabdemo.Model.TaskModel;
import com.example.gitlabdemo.Util.GitProcess;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    GitProcess gitProcess;

    @PostMapping(value = "/createTask", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> createTask(String task_id, @RequestBody TaskModel taskModel){
        gitProcess = new GitProcess(task_id);
        System.out.println(taskModel);
        if(gitProcess.gitcreateTask(taskModel)){
            return getResult(new Result(), HttpStatus.OK);
        }
        else {
            return getResult(new Result("false"), HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/createTask")
//    private ResponseEntity<Result> createTask(TaskModel taskModel){
//        gitProcess = new GitProcess();
//        System.out.println(taskModel);
//        if(gitProcess.gitcreateTask(taskModel)){
//            return getResult(new Result(), HttpStatus.OK);
//        }
//        else {
//            return getResult(new Result("false"), HttpStatus.BAD_REQUEST);
//        }
//    }


    ResponseEntity<Result> getResult(Result result, HttpStatus httpStatus){
        return new ResponseEntity<>(result, httpStatus);
    }
}
