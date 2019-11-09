package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Entity.Course;
import com.example.gitlabdemo.Entity.CourseAndStudent;
import com.example.gitlabdemo.Repository.CourseAndStudentRepository;
import com.example.gitlabdemo.Repository.CourseRepository;
import com.example.gitlabdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("courseService")
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseAndStudentRepository courseAndStudentRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository, CourseAndStudentRepository courseAndStudentRepository) {
        Assert.notNull(courseRepository, "courseRepository must not be null!");
        Assert.notNull(userRepository, "userRepository must not be null!");
        Assert.notNull(courseAndStudentRepository, "courseAndStudentRepository must not be null!");
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseAndStudentRepository = courseAndStudentRepository;
    }

    public Course findCourse(Long cid){
        return this.courseRepository.getOne(cid);
    }

    public Course saveCourse(Course course){
        this.courseRepository.save(course);
        return course;
    }

    public void deleteCourse(Course course){
        this.courseRepository.delete(course);
    }

    /**
     * 添加班级学生信息
     * @param courseAndStudent
     */
    public void courseAddStudent(CourseAndStudent courseAndStudent){
        this.courseAndStudentRepository.save(courseAndStudent);
    }
}
