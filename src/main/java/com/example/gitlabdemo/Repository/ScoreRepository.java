package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Model.DataModel.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
