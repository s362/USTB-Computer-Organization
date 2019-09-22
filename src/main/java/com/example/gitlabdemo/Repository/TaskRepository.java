package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Model.DataModel.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
