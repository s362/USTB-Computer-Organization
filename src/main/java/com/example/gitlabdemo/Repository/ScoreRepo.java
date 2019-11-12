package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepo extends JpaRepository<Score, Long> {
}
