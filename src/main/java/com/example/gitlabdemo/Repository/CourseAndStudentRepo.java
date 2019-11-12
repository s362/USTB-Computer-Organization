package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.CourseAndStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAndStudentRepo extends JpaRepository<CourseAndStudent, Long> {
}