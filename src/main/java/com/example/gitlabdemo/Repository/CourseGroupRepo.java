package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.CourseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseGroupRepo extends JpaRepository<CourseGroup, Long> {
}
