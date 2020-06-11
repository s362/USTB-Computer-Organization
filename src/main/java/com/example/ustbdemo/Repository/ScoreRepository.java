package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
