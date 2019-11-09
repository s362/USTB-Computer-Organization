package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
