package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.CourseAndTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAndTeacherRepo extends JpaRepository<CourseAndTeacher, Long> {
}