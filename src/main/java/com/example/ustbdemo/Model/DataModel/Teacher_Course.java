package com.example.ustbdemo.Model.DataModel;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "teacher_course")
@Data
@ToString
public class Teacher_Course {       //记录老师和课程之间的绑定关系的数据库表

    @Id
    @GeneratedValue
    private Long teacherCourseId;   //主键

    @Column(name = "teacher_id")    //老师id
    private Long teacherId;

    @Column(name = "course_id")     //课程id
    private Long courseId;

    public Long getTeacherCourseId() {
        return teacherCourseId;
    }

    public void setTeacherCourseId(Long teacherCourseId) {
        this.teacherCourseId = teacherCourseId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
