package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {

}