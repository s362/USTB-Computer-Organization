package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {
}
