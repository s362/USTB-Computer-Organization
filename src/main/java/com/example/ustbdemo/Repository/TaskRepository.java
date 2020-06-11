package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
