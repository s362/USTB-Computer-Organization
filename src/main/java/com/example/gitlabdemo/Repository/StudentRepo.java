package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long> {

}
