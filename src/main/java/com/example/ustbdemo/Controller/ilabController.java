//package com.example.ustbdemo.Controller;
//
//import com.example.ustbdemo.Model.DataModel.*;
//import com.example.ustbdemo.Service.QuestionService;
//import com.example.ustbdemo.Service.ScoreService;
//import com.example.ustbdemo.Service.TaskService;
//import com.example.ustbdemo.Service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/ilab")
//public class ilabController {
//    @Autowired
//    ScoreService scoreService;
//
//    @Autowired
//    QuestionService questionService;
//
//    @Autowired
//    TaskService taskService;
//
//    @Autowired
//    UserService userService;
//
//
////    @PostMapping(value = "/")
////    public void ilabtest() throws Exception {
////
////        StudentController.ilab("test");
////    }
//
//
//    //获取题目对应的分数
//    private  Long getGradeOfTask(Long uid,Long tid){
//        Score score=scoreService.findScoreByUserandTid(uid,tid);
//        if (score==null) return 0l;
//        return score.getTscore();
//    }
//}
