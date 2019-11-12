package com.example.gitlabdemo.Controller;

import com.example.gitlabdemo.Entity.Course;
import com.example.gitlabdemo.Entity.CourseGroup;
import com.example.gitlabdemo.Entity.Student;
import com.example.gitlabdemo.Entity.Teacher;
import com.example.gitlabdemo.Service.CourseService;
import com.example.gitlabdemo.Service.UserService;
import com.example.gitlabdemo.Shiro.JwtUtil;
import com.example.gitlabdemo.Util.Result;
import com.example.gitlabdemo.Util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CoursesController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/courses/{cid}", method = RequestMethod.GET)
//    public ResponseEntity<Result> getCourse(@PathVariable Long cid, HttpServletRequest httpServletRequest){
//
//    }

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public ResponseEntity<Result> getCourses(HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        Long utype = JwtUtil.getUserType(httpServletRequest.getHeader("utype"));
        if(utype > 1){
            Teacher teacher = this.userService.findTeacherByName(user_id);
            return ResultUtil.getResult(new Result(this.courseService.getAllCourses(teacher)), HttpStatus.OK);
        } else {
            Student student = this.userService.findStudentByName(user_id);
            return ResultUtil.getResult(new Result(this.courseService.getAllCourses(student)), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/courses/{cid}", method = RequestMethod.DELETE)
    public ResponseEntity<Result> deleteCourse(@PathVariable Long cid, HttpServletRequest httpServletRequest){
        Course course = new Course();
        course.setCid(cid);
        this.courseService.deleteCourse(course);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @RequestMapping(value = "/courses", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Result> updateCourse(HttpServletRequest httpServletRequest, @RequestBody Course course){
        this.courseService.saveCourse(course);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @RequestMapping(value = "/courses", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Result> addCourse(@RequestBody String cname, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        if(!user_id.equals("admin")){
            return  ResultUtil.getResult(new Result("no authority", false), HttpStatus.BAD_REQUEST);
        }
        Course course = new Course();
        course.setCname(cname);
        try{
            courseService.saveCourse(course);
            // admin 是每一个课程的老师
            userService.addTeacherToCourse(userService.findTeacherByName(user_id), course.getCid());
            return  ResultUtil.getResult(new Result(), HttpStatus.OK);
        } catch (Exception e){
            return  ResultUtil.getResult(new Result("course has already exist", false), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 添加课程分组
     * @param cid
     * @param cgname
     * @return
     */
    @PostMapping(value = "/coursegroups")
    public ResponseEntity<Result> addCourseGroup(Long cid, String cgname){
        CourseGroup courseGroup = new CourseGroup(cid, cgname);
        try{
            this.courseService.addCourseGroup(courseGroup);
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } catch (Exception e){
            return ResultUtil.getResult(new Result("添加失败", false), HttpStatus.OK);
        }
    }

    /**
     * 删除课程分组
     * @param cgid
     * @return
     */
    @DeleteMapping(value = "/coursegroups")
    public ResponseEntity<Result> deleteCourseGroup(Long cgid){
        CourseGroup courseGroup = new CourseGroup();
        courseGroup.setCgid(cgid);
        try{
            this.courseService.deleteCourseGroup(courseGroup);
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } catch (Exception e){
            return ResultUtil.getResult(new Result("添加失败", false), HttpStatus.OK);
        }
    }

}
