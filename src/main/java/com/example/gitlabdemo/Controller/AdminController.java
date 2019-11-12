package com.example.gitlabdemo.Controller;

import com.example.gitlabdemo.Entity.Course;
import com.example.gitlabdemo.Entity.CourseAndTeacher;
import com.example.gitlabdemo.Entity.Teacher;
import com.example.gitlabdemo.Model.LoginUser;
import com.example.gitlabdemo.Service.CourseService;
import com.example.gitlabdemo.Service.QuestionService;
import com.example.gitlabdemo.Service.TaskService;
import com.example.gitlabdemo.Service.UserService;
import com.example.gitlabdemo.Shiro.JwtUtil;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.Result;
import com.example.gitlabdemo.Util.ResultUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    GitProcess gitProcess;
    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CourseService courseService;




    /**
     * 创建老师
     * @param
     * @return
     */
    @PostMapping(value = "/addTeacher", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> addTeacher(HttpServletRequest httpServletRequest, @RequestBody Teacher teacher){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        if(!user_id.equals("admin")){
            return  ResultUtil.getResult(new Result("no authority", false), HttpStatus.BAD_REQUEST);
        }
        System.out.println(teacher);
        // 设置权限为3
        // TODO 这里权限表要存入数据库中
        teacher.setUtype(3l);
        userService.addTeacher(teacher);
        return  ResultUtil.getResult(new Result(), HttpStatus.OK);
    }


    /**
     * 给老师分配课程
     * @param
     * @return
     */
    @PostMapping(value = "/addTeacherToCourse", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> addTeacherToCourse(HttpServletRequest httpServletRequest, @RequestBody JsonNode info){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(info);
        final ObjectMapper mapper = new ObjectMapper();
        try{
//            System.out.println(info.findValue("cids").toString());
            List<Long> _cids = mapper.readValue(info.findValue("cids").toString(), new TypeReference<List<Long>>(){});
            Teacher teacher = userService.findTeacherByName(user_id);
            for(Long cid : _cids){
                userService.addTeacherToCourse(teacher, cid);
            }
        } catch (Exception e){
            System.out.println("false");
            return  ResultUtil.getResult(new Result(e.toString(), false), HttpStatus.BAD_REQUEST);
        }
        return  ResultUtil.getResult(new Result(), HttpStatus.OK);
    }


    /**
     * 创建管理员,测试使用
     * @param
     * @return
     */
    @PostMapping(value = "/addAdmin", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> addAdmin(@RequestBody Teacher teacher){
        System.out.println(teacher);
        userService.addTeacher(teacher);
        return  ResultUtil.getResult(new Result(), HttpStatus.OK);
    }


}
