package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
