package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Model.DataModel.CourseAndStudent;
import com.example.gitlabdemo.Model.DataModel.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAndStudentRepository extends JpaRepository<CourseAndStudent, Long> {
}