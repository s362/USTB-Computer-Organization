package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.QuestionAndTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAndTaskRepository extends JpaRepository<QuestionAndTask, Long> {
}
