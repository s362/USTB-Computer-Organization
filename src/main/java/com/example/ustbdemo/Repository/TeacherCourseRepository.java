package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Teacher_Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherCourseRepository extends JpaRepository<Teacher_Course,Long> {
}
