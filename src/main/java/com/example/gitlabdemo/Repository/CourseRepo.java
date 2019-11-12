package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, Long> {
}
