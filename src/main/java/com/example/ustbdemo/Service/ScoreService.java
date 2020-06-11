package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.Score;
import com.example.ustbdemo.Repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
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
        return this.scoreRepository.findAll(example);
    }

    public void saveScore(Score score)  {
        try{
            score.setUpdatedate(new Date());
            this.scoreRepository.save(score);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public Score findScoreByUserandTid(Long uid, Long tid) {
        Score score = new Score();
        score.setUid(uid);
        score.setTid(tid);
        Example<Score> example = Example.of(score);
        try {
            Score result = this.scoreRepository.findOne(example).get();
            return result;
        } catch (Exception e){
            return null;
        }
    }

}
