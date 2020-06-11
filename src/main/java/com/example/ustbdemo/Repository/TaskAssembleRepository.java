package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Instruction;
import com.example.ustbdemo.Model.DataModel.TaskAssemble;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssembleRepository extends JpaRepository<TaskAssemble, Long> {
}
