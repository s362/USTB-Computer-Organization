package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.CourseAndStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAndStudentRepository extends JpaRepository<CourseAndStudent, Long> {
}