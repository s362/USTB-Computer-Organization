package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Entity.*;
import com.example.gitlabdemo.Model.LoginUser;
import com.example.gitlabdemo.Repository.CourseAndStudentRepo;
import com.example.gitlabdemo.Repository.CourseAndTeacherRepo;
import com.example.gitlabdemo.Repository.StudentRepo;
import com.example.gitlabdemo.Repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.NoSuchElementException;

@Service("userService")
public class UserService {
    private final StudentRepo studentRepo;
    private final CourseAndStudentRepo courseAndStudentRepo;
    private final TeacherRepo teacherRepo;
    private final CourseAndTeacherRepo courseAndTeacherRepo;

    @Autowired
    public UserService(StudentRepo studentRepo, CourseAndStudentRepo courseAndStudentRepo,
                       TeacherRepo teacherRepo, CourseAndTeacherRepo courseAndTeacherRepo){
        Assert.notNull(studentRepo, "studentRepo must not be null!");
        this.studentRepo = studentRepo;
        this.courseAndStudentRepo = courseAndStudentRepo;
        this.teacherRepo = teacherRepo;
        this.courseAndTeacherRepo = courseAndTeacherRepo;
    }

    /**
     * 根据用户名和密码获取用户
     * @param loginUser
     * @return
     */
    public Boolean getByUsernameAndPwd(LoginUser loginUser){
        try {
            if(loginUser.getUtype() == 0){
                Student student = new Student(loginUser.getUusername(), loginUser.getUpassword());
                Example<Student> example = Example.of(student);
                this.studentRepo.findOne(example).get();
            } else{
                Teacher teacher = new Teacher(loginUser.getUusername(), loginUser.getUpassword());
                Example<Teacher> example = Example.of(teacher);
                this.teacherRepo.findOne(example).get();
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 根据用户名和密码获取用户
     * @param teacher
     * @return
     */
    public Teacher getByTeachernameAndPwd(Teacher teacher){
        Example<Teacher> example = Example.of(teacher);
        return this.teacherRepo.findOne(example).get();
    }

    /**
     * 根据用户名寻找用户，用于登录验证
     * @param userName
     * @return
     */
    public LoginUser findByUserName(String userName, Long utpye){
        try{
            if (utpye == 0){
                Student student = new Student();
                student.setUusername(userName);
                Example<Student> example = Example.of(student);
                student = this.studentRepo.findOne(example).get();
                return new LoginUser(student.getUusername(), student.getUpassword());
            } else {
                Teacher teacher = new Teacher();
                teacher.setUusername(userName);
                Example<Teacher> example = Example.of(teacher);
                teacher = this.teacherRepo.findOne(example).get();
                return new LoginUser(teacher.getUusername(), teacher.getUpassword());
            }
        } catch (NoSuchElementException e){
            return null;
        }
    }

    /**
     * 根据用户名寻找老师
     * @param userName
     * @return
     */
    public Student findStudentByName(String userName){
        Student student = new Student();
        student.setUusername(userName);
        Example<Student> example = Example.of(student);
        try{
            return this.studentRepo.findOne(example).get();
        } catch (NoSuchElementException e){
            return null;
        }
    }

    /**
     * 根据用户名寻找老师
     * @param userName
     * @return
     */
    public Teacher findTeacherByName(String userName){
        Teacher teacher = new Teacher();
        teacher.setUusername(userName);
        Example<Teacher> example = Example.of(teacher);
        try{
            return this.teacherRepo.findOne(example).get();
        } catch (NoSuchElementException e){
            return null;
        }
    }


    /**
     * 增加学生
     * @param student
     * @return
     */
    public void addStudent(Student student){
        student.setCreatedate(new Date());
        this.studentRepo.save(student);
    }



    /**
     * 增加老师
     * @param teacher
     * @return
     */
    public void addTeacher(Teacher teacher){
        teacher.setCreatedate(new Date());
        this.teacherRepo.save(teacher);
    }


    /**
     * 添加学生到某课程中
     * @param student
     * @param cgid
     */
    public void addStudentToCourse(Student student, Long cgid){
        CourseAndStudent courseAndStudent = new CourseAndStudent();
        courseAndStudent.setCgid(cgid);
        courseAndStudent.setSid(student.getUid());
        Example<CourseAndStudent> example = Example.of(courseAndStudent);
        try{
            courseAndStudentRepo.findOne(example).get();

        } catch (NoSuchElementException e){
            courseAndStudent.setJoinTime(new Date());
            courseAndStudentRepo.save(courseAndStudent);
        }
    }

    /**
     * 添加老师到某课程中,附带重复检查
     * @param teacher
     * @param cid
     */
    public void addTeacherToCourse(Teacher teacher, Long cid){
        CourseAndTeacher courseAndTeacher = new CourseAndTeacher();
        courseAndTeacher.setCid(cid);
        courseAndTeacher.setUid(teacher.getUid());
        Example<CourseAndTeacher> example = Example.of(courseAndTeacher);
        try{
            courseAndTeacherRepo.findOne(example).get();

        } catch (NoSuchElementException e){
            courseAndTeacher.setJoinTime(new Date());
            courseAndTeacherRepo.save(courseAndTeacher);
        }
    }
}
