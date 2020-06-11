package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.TaskAssemble;
import com.example.ustbdemo.Model.DataModel.TaskChoose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskChooseRepository extends JpaRepository<TaskChoose, Long> {
}
