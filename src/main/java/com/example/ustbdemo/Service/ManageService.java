package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.Course;
import com.example.ustbdemo.Model.DataModel.Teacher_Course;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Repository.CourseRepository;
import com.example.ustbdemo.Repository.TeacherCourseRepository;
import com.example.ustbdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service("manageService")
public class ManageService {

    private final CourseRepository courseRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ManageService(CourseRepository courseRepository, TeacherCourseRepository teacherCourseRepository, UserRepository userRepository) {
        Assert.notNull(courseRepository, "courseRepository must not be null!");
        Assert.notNull(teacherCourseRepository, "teacherCourseRepository must not be null!");
        Assert.notNull(userRepository, "userRepository must not be null!");
        this.courseRepository = courseRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.userRepository = userRepository;
    }

    /**
     * 根据老师id，返回该老师执教的所有课程信息
     * @param teacherId 老师id
     * @return  课程列表
     */
    public List<Course> getTeacherCourseList(Long teacherId){
        Teacher_Course teacherCourse=new Teacher_Course();
        teacherCourse.setTeacherId(teacherId);
        Example<Teacher_Course> teacherCourseExample=Example.of(teacherCourse);
        List<Teacher_Course> teacherCourseList=this.teacherCourseRepository.findAll(teacherCourseExample);
        List<Course> courseList=new ArrayList<>();
        for (Teacher_Course item :teacherCourseList) {
            try {
                Course course=this.courseRepository.findById(item.getCourseId()).get();
                courseList.add(course);
            }catch (Exception e){
                System.out.println("不存在该课程");
                this.teacherCourseRepository.deleteById(item.getTeacherCourseId());
            }
        }
        return courseList;
    }

    /**
     * 获取所有的课程信息
     * @return 课程的列表
     */
    public List<Course> getCourses(){
        try {
            return this.courseRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加一门新的课程
     * @param course 课程信息
     * @return 添加成功与否
     */
    public boolean addCourse(Course course){
        try {
            this.courseRepository.save(course);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据课程id查询该课程的授课老师信息
     * @param courseId 课程id
     * @return  老师的信息列表
     */
    public List<User> getTeachersByCourseId(Long courseId){
        Teacher_Course teacher_course=new Teacher_Course();
        teacher_course.setCourseId(courseId);
        //查询出所有和该课程的有对应关系的老师的id
        Example<Teacher_Course> teacherCourseExample=Example.of(teacher_course);
        try {
            List<Teacher_Course> teacherCourseList=this.teacherCourseRepository.findAll(teacherCourseExample);
            List<User> userList=new ArrayList<>();
            for (Teacher_Course item:teacherCourseList){
                try {
                    //根据老师id逐个查出老师个人信息
                    User user=this.userRepository.findById(item.getTeacherId()).get();
                    userList.add(user);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return userList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据课程id删除课程
     * @param courseId 课程id
     * @return 删除是否成功
     */
    public boolean deleteCourseByCourseId(Long courseId){
        try {
            this.courseRepository.deleteById(courseId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加老师到课程里
     * @param teacherCourse 老师与课程的对应关系对象
     * @return 添加是否成功
     */
    public boolean addTeacherOfCourse(Teacher_Course teacherCourse){
        try {
            Example<Teacher_Course> teacherCourseExample=Example.of(teacherCourse);

            //先查询是否已经有该关系存在，避免重复插入
            Optional<Teacher_Course> teacherCourseOptional=this.teacherCourseRepository.findOne(teacherCourseExample);
            if (teacherCourseOptional.isPresent()) {
                System.out.println("该老师已经是该课程的授课老师");
                return true;
            }
            this.teacherCourseRepository.save(teacherCourse);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解除老师和课程之间的绑定关系
     * @param teacherCourse 老师与课程之间的关系对象
     * @return 解除是否成功
     */
    public boolean deleteTeacherOfCourse(Teacher_Course teacherCourse){
        try {
            Example<Teacher_Course> teacherCourseExample=Example.of(teacherCourse);

            Optional<Teacher_Course> teacherCourseOptional=this.teacherCourseRepository.findOne(teacherCourseExample);
            if (!teacherCourseOptional.isPresent()){
                System.out.println("该老师与该课程之间无绑定关系");
                return true;
            }
            this.teacherCourseRepository.delete(teacherCourseOptional.get());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
