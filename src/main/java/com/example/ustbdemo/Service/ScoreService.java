package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.Assemble_Choose_Score;
import com.example.ustbdemo.Model.DataModel.Score;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Repository.AssembleChooseScoreRepository;
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
    private final AssembleChooseScoreRepository assembleChooseScoreRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository, AssembleChooseScoreRepository assembleChooseScoreRepository){
        Assert.notNull(scoreRepository, "taskRepository must not be null!");
        Assert.notNull(assembleChooseScoreRepository, "taskRepository must not be null!");
        this.scoreRepository = scoreRepository;
        this.assembleChooseScoreRepository = assembleChooseScoreRepository;
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

    public void saveAssembleChooseScore(Assemble_Choose_Score assemble_choose_score)  {
        try{
            assemble_choose_score.setUpdatedate(new Date());
            this.assembleChooseScoreRepository.save(assemble_choose_score);
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

    public Assemble_Choose_Score findAssembleChooseScoreByUidandTid(Long uid, Long tcid){
        Assemble_Choose_Score assemble_choose_score = new Assemble_Choose_Score();
        assemble_choose_score.setUid(uid);
        assemble_choose_score.setTcid(tcid);
        Example<Assemble_Choose_Score> example = Example.of(assemble_choose_score);
        try {
            Assemble_Choose_Score result = this.assembleChooseScoreRepository.findOne(example).get();
            return result;
        } catch (Exception e){
            return null;
        }
    }

}
