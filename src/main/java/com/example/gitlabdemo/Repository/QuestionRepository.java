package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
