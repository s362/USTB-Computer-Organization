package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Question_Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskQuestionRepository extends JpaRepository<Question_Task, Long> {
}
