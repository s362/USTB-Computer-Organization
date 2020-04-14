package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Model.DataModel.Question;
import com.example.gitlabdemo.Model.DataModel.Score;
import com.example.gitlabdemo.Repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Date;
import java.util.List;

@Service("scoreService")
public class ScoreService {
    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository){
        Assert.notNull(scoreRepository, "taskRepository must not be null!");
        this.scoreRepository = scoreRepository;
    }

    public List<Score> findScoreByUser(Long uid){
        Score score = new Score();
        score.setUid(uid);
        Example<Score> example = Example.of(score);
        try {
            Score temp;
            return this.scoreRepository.findAll(example);
        } catch (Exception e){
            return null;
        }
    }

    public int saveScore(Score score)  {
        try{
            score.setUpdatedate(new Date(new java.util.Date().getTime()));
            if (score.getTscore() == null){
                score.setTscore(0l);
            }
            this.scoreRepository.save(score);
            Example<Score> example = Example.of(score);
            score = this.scoreRepository.findAll(example).get(0);
            return 0;
        } catch (Exception e){
            System.out.println(e.toString());
            return -1;
        }
    }

    public Score findScoreByUserandTaskid(Score score) {
        Example<Score> example = Example.of(score);
        try {
            Score temp;
            temp = this.scoreRepository.findAll(example).get(0);
            return temp;
        } catch (Exception e){
            return null;
        }
    }
}
