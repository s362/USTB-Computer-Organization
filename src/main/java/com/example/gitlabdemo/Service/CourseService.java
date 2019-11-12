package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Entity.*;
import com.example.gitlabdemo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service("courseService")
public class CourseService {
    private final CourseRepo courseRepo;
    private final StudentRepo studentRepo;
    private final CourseAndStudentRepo courseAndStudentRepo;
    private final CourseGroupRepo courseGroupRepo;
    private final CourseAndTeacherRepo courseAndTeacherRepo;
    @Autowired
    public CourseService(CourseRepo courseRepo, StudentRepo studentRepo, CourseAndStudentRepo courseAndStudentRepo,
                         CourseGroupRepo courseGroupRepo, CourseAndTeacherRepo courseAndTeacherRepo) {
        Assert.notNull(courseRepo, "courseRepo must not be null!");
        Assert.notNull(studentRepo, "studentRepo must not be null!");
        Assert.notNull(courseAndStudentRepo, "courseAndStudentRepo must not be null!");
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
        this.courseAndStudentRepo = courseAndStudentRepo;
        this.courseGroupRepo = courseGroupRepo;
        this.courseAndTeacherRepo = courseAndTeacherRepo;
    }

    /**
     * 通过课程id寻找课程
     * @param cid
     * @return
     */
    public Course findCourseByCid(Long cid){
        return this.courseRepo.getOne(cid);
    }

    /**
     * 保存课程
     * @param course
     */
    public void saveCourse(Course course){
        this.courseRepo.save(course);
    }

    /**
     * 删除课程
     * TODO 要把所有跟课程有关的都删掉
     * @param course
     */
    public void deleteCourse(Course course){
        this.courseRepo.delete(course);
    }

    /**
     * 删除课程分组
     * @param courseGroup
     */
    public void deleteCourseGroup(CourseGroup courseGroup){
        this.courseGroupRepo.delete(courseGroup);
    }

    /**
     * 添加课程分组
     * @param courseGroup
     */
    public void addCourseGroup(CourseGroup courseGroup){
        this.courseGroupRepo.save(courseGroup);
    }

    /**
     * 为课程分组添加学生
     * @param courseAndStudent
     */
    public void addStudentToCourseGroup(CourseAndStudent courseAndStudent){
        this.courseAndStudentRepo.save(courseAndStudent);
    }

    /**
     * 删除学生
     * @param courseAndStudent
     */
    public void deleteStudentToCourseGroup(CourseAndStudent courseAndStudent){
        this.courseAndStudentRepo.delete(courseAndStudent);
    }


    /**
     * 获取所有课堂
     */
    public List<Course> getAllCourses(Teacher teacher){
        CourseAndTeacher courseAndTeacher = new CourseAndTeacher();
        courseAndTeacher.setUid(teacher.getUid());
        Example<CourseAndTeacher> example = Example.of(courseAndTeacher);
        List<CourseAndTeacher> courseAndTeachers = this.courseAndTeacherRepo.findAll(example);
        List<Course> courses = new ArrayList<>();
        for (CourseAndTeacher _courseAndTeacher : courseAndTeachers){
            courses.add(this.courseRepo.findById(_courseAndTeacher.getCid()).get());
        }
        return courses;
    }

    public List<Course> getAllCourses(Student student){
        CourseAndStudent courseAndStudent = new CourseAndStudent();
        courseAndStudent.setSid(student.getUid());
        Example<CourseAndStudent> example = Example.of(courseAndStudent);
        List<CourseAndStudent> courseAndStudents = this.courseAndStudentRepo.findAll(example);
        List<Course> courses = new ArrayList<>();
        for (CourseAndStudent _courseAndStudent : courseAndStudents){
            courses.add(this.courseRepo.findById(_courseAndStudent.getCid()).get());
        }
        return courses;
    }
}
