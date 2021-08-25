package com.example.ustbdemo.Controller;


import com.example.ustbdemo.Model.DataModel.Course;
import com.example.ustbdemo.Model.DataModel.Teacher_Course;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Service.ManageService;
import com.example.ustbdemo.Service.UserService;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    public static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ManageService manageService;

//    @PostMapping(value = "/getTeacherCourse")
//    public ResponseEntity<Result> getTeacherCourse(HttpServletRequest httpServletRequest){
//        if (!AuthorityLimitUtil.isAdmin(httpServletRequest.getHeader("Authorization"))){
//            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
//        }
//        String username= JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
//        User user=userService.findByUserName(username);
//        List<Course> courseList=manageService.getTeacherCourseList(user.getUid());
//        Result result=new Result();
//        result.setObject(courseList);
//        result.setSuccess(true);
//        return ResultUtil.getResult(result,HttpStatus.OK);
//    }

    //获取的所有的课程信息
    @PostMapping(value = "/getCourses")
    public ResponseEntity<Result> getCourses(HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        List<Course> courseList=manageService.getCourses();
        Result result=new Result();
        result.setObject(courseList);
        result.setSuccess(true);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }


    //获取所有的老师的信息，包括密码
    @PostMapping(value = "/getTeachers")
    public ResponseEntity<Result> getTeachers(HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        List<User> userList=userService.getTeachers();
        Result result=new Result();
        result.setObject(userList);
        result.setSuccess(true);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

    //管理员新建一门新的课程
    @PostMapping(value = "addCourse")
    public ResponseEntity<Result> addCourse(Course course,HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        if (manageService.addCourse(course)){
            return ResultUtil.getResult(new Result(),HttpStatus.OK);
        }else return ResultUtil.getResult(new Result("添加失败"),HttpStatus.BAD_REQUEST);
    }

    //管理员删除一门已有的课程
    @PostMapping(value = "deleteCourse")
    public ResponseEntity<Result> deleteCourse(Long courseId,HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        if (manageService.deleteCourseByCourseId(courseId)){
            return ResultUtil.getResult(new Result(),HttpStatus.OK);
        }else return ResultUtil.getResult(new Result("删除失败"),HttpStatus.BAD_REQUEST);
    }


    //查看某门课程的授课老师
    @PostMapping(value = "getTeacherOfCourse")
    public ResponseEntity<Result> getTeacherOfCourse(Long courseId,HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        List<User> userList=manageService.getTeachersByCourseId(courseId);
        Result result=new Result();
        result.setSuccess(true);
        result.setObject(userList);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

    //为某个课程绑定老师
    @PostMapping(value = "addTeacherOfCourse")
    public ResponseEntity<Result> addTeacherOfCourse(Teacher_Course teacherCourse, HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        if (manageService.addTeacherOfCourse(teacherCourse)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("绑定失败"),HttpStatus.BAD_REQUEST);
    }

    //为某个老师解除绑定
    @PostMapping(value = "deleteTeacherOfCourse")
    public ResponseEntity<Result> deleteTeacherOfCourse(Teacher_Course teacherCourse,HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        if (manageService.deleteTeacherOfCourse(teacherCourse)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("解除失败"),HttpStatus.BAD_REQUEST);
    }

    //新增老师
    @PostMapping(value = "addTeacher")
    public ResponseEntity<Result> addTeacher(User teacher,HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        User user=userService.findByUserName(teacher.getUsername());
        if (user!=null){
            logger.info("用户名已存在");
            return ResultUtil.getResult(new Result("用户名已存在"),HttpStatus.BAD_REQUEST);
        }
        if (userService.addTeacher(teacher)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("新增老师失败"),HttpStatus.BAD_REQUEST);
    }

    //删除老师
    @PostMapping(value = "deleteTeacher")
    public ResponseEntity<Result> deleteTeacher(Long teacherId,HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        if (userService.deleteTeacherByTeacherId(teacherId)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("删除老师失败"),HttpStatus.BAD_REQUEST);
    }

    //新增学生
    @PostMapping(value = "addStudent")
    public ResponseEntity<Result> addStudent(User student, HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        User user=userService.findByUserName(student.getUsername());
        if (user!=null){
            logger.info("用户名已存在");
            return ResultUtil.getResult(new Result("用户名已存在"),HttpStatus.BAD_REQUEST);
        }
        if (userService.addStudent(student)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("新增用户失败"),HttpStatus.BAD_REQUEST);
    }

    //删除学生
    @PostMapping(value = "deleteStudent")
    public ResponseEntity<Result> deleteStudent(Long StudentId,HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        if (userService.deleteStudentId(StudentId)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("删除学生失败"),HttpStatus.BAD_REQUEST);
    }

    //获取所有的学生的信息，包括密码
    @PostMapping(value = "/getStudent")
    public ResponseEntity<Result> getStudent(HttpServletRequest httpServletRequest){
        if (!IsAdmin(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        List<User> userList=userService.getStudent();
        Result result=new Result();
        result.setObject(userList);
        result.setSuccess(true);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

//    //修改所有的学生的信息，密码加密
//    @PostMapping(value = "/changeStudentPassword")
//    public ResponseEntity<Result> changeStudentPassword(HttpServletRequest httpServletRequest){
//        if (!IsAdmin(httpServletRequest)){
//            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
//        }
//
//        if (userService.changeStudentPassword()) return ResultUtil.getResult(new Result(),HttpStatus.OK);
//        else return ResultUtil.getResult(new Result("修改密码失败"),HttpStatus.BAD_REQUEST);
//    }


    private boolean IsAdmin(HttpServletRequest httpServletRequest){      //判断该用户是否拥有管理员权限
        String token=httpServletRequest.getHeader("Authorization");
        String username= JwtUtil.getUsername(token);
        User user=userService.findByUserName(username);
        if (user==null) return true;
        return user.getUtype() == 0;
    }

    private boolean isAdminOrTeacher(HttpServletRequest httpServletRequest){      //判断该用户是否拥有管理员或者老师权限
        String token=httpServletRequest.getHeader("Authorization");
        String username= JwtUtil.getUsername(token);
        User user=userService.findByUserName(username);
        if (user==null) return false;
        return user.getUtype() == 1||user.getUtype()==0;
    }
}
