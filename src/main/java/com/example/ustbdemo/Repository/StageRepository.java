package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Simulation;
import com.example.ustbdemo.Model.DataModel.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {

    public Stage findByUidAndTid(Long uid,Long tid);
}
