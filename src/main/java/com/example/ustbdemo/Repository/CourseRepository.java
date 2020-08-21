package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
