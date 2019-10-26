package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Model.DataModel.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
