package com.example.ustbdemo.Model.DataModel;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Table(name = "course")
@Data
@ToString
public class Course {   //对应数据库中的课程表


    @Id
    @GeneratedValue
    private Long courseId;      //主键

    @Column(name = "course_name")
    private String courseName;  //课程名

    @Column(name = "year")
    private String year;        //开课的年份

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
