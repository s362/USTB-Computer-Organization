package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("scoreService")
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final AssembleChooseScoreRepository assembleChooseScoreRepository;
    private final AssembleCodeScoreRepository assembleCodeScoreRepository;
    private final AssembleChooseRepository assembleChooseRepository;
    private final VerilogRunTimesRepository verilogRunTimesRepository;
    private final StageRepository stageRepository;
    @Autowired
    public ScoreService(ScoreRepository scoreRepository, AssembleChooseScoreRepository assembleChooseScoreRepository, AssembleCodeScoreRepository assembleCodeScoreRepository, AssembleChooseRepository assembleChooseRepository, VerilogRunTimesRepository verilogRunTimesRepository, StageRepository stageRepository){
        Assert.notNull(scoreRepository, "scoreRepository must not be null!");
        Assert.notNull(assembleChooseScoreRepository, "assembleChooseScoreRepository must not be null!");
        Assert.notNull(assembleCodeScoreRepository, "assembleCodeScoreRepository must not be null!");
        Assert.notNull(assembleChooseRepository,"assembleChooseRepository must not be null!");
        Assert.notNull(verilogRunTimesRepository,"verilogRunTimesRepository must not be null!");
        Assert.notNull(stageRepository,"stageRepository must not be null!");
        this.scoreRepository = scoreRepository;
        this.assembleChooseScoreRepository = assembleChooseScoreRepository;
        this.assembleCodeScoreRepository = assembleCodeScoreRepository;
        this.assembleChooseRepository = assembleChooseRepository;
        this.verilogRunTimesRepository = verilogRunTimesRepository;
        this.stageRepository = stageRepository;
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
            List<Score> scoreList = this.scoreRepository.findAll(example);
            if(!scoreList.isEmpty()){
                Score result = scoreList.get(0);
                for (Score item:scoreList){
                    if(result.getTscore()<item.getTscore()){
                        result = item;
                    }
                }
                for (Score item:scoreList){
                    if(result.getSid()!=item.getSid()){
                        this.scoreRepository.delete(item);
                    }
                }
                return result;
            }else{
                return null;
            }

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


    //根据用户id和题目id获取汇编仿真代码的提交分数，包括提交次数
    public Assemble_Code_Score findAssembleCodeScoreByUidAndTid(Long uid,Long tid){
        Assemble_Code_Score assembleCodeScore=new Assemble_Code_Score();
        assembleCodeScore.setTid(tid);
        assembleCodeScore.setUid(uid);
        Example<Assemble_Code_Score> example=Example.of(assembleCodeScore);
        try {
            return this.assembleCodeScoreRepository.findOne(example).get();
        }catch (Exception e){
            return null;
        }
    }


    //保存汇编代码的得分和提交次数情况
    public void saveAssembleCodeScore(Assemble_Code_Score assembleCodeScore){
        try {
            this.assembleCodeScoreRepository.save(assembleCodeScore);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //将某个题目的成绩完全删除，便于演示整个打分逻辑和过程
    public boolean deleteScore(Long uid,Long tid,Long tType){
        Score score=new Score();
        score.setUid(uid);
        score.setTid(tid);
        Example<Score> scoreExample=Example.of(score);
        try {
            Optional<Score> scoreOptional = this.scoreRepository.findOne(scoreExample);
            if (!scoreOptional.isPresent()) {
                System.out.println("uid="+uid+"tid="+tid+": 此题没有成绩记录");
                return true;
            }
            //verilog编程题  直接删除score库中的成绩即可
            this.scoreRepository.delete(scoreOptional.get());

            if (tType == 1L) { //汇编仿真题 需要在删除score库的同时删除Assemble_Choose_Score和Assemble_Code_Score的成绩

                //汇编代码部分成绩删除
                Assemble_Code_Score assembleCodeScore=new Assemble_Code_Score();
                assembleCodeScore.setUid(uid);
                assembleCodeScore.setTid(tid);
                Example<Assemble_Code_Score> assembleCodeScoreExample=Example.of(assembleCodeScore);

                Optional<Assemble_Code_Score> assembleCodeScoreOptional=this.assembleCodeScoreRepository.findOne(assembleCodeScoreExample);
                if (assembleCodeScoreOptional.isPresent()){
                    this.assembleCodeScoreRepository.delete(assembleCodeScoreOptional.get());
                }

                //选择题部分成绩删除
                //查找选择题

                Assemble_Choose assembleChoose=new Assemble_Choose();
                assembleChoose.setTid(tid);
                Example<Assemble_Choose> assembleChooseExample=Example.of(assembleChoose);
                List<Assemble_Choose> assembleChooseList=this.assembleChooseRepository.findAll(assembleChooseExample);
                //删除每一个选择题对应的成绩
                for (Assemble_Choose item:assembleChooseList){
                    Assemble_Choose_Score assembleChooseScore=new Assemble_Choose_Score();
                    assembleChooseScore.setUid(uid);
                    assembleChooseScore.setTcid(item.getTcid());
                    Example<Assemble_Choose_Score> assembleChooseScoreExample=Example.of(assembleChooseScore);

                    Optional<Assemble_Choose_Score> assembleChooseScoreOptional=this.assembleChooseScoreRepository.findOne(assembleChooseScoreExample);
                    if (assembleChooseScoreOptional.isPresent()){
                        this.assembleChooseScoreRepository.delete(assembleChooseScoreOptional.get());
                    }
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public VerilogRunTimes findVerilogRunTimesByTidAndUid(Long tid,Long uid){
        VerilogRunTimes verilogRunTimes=new VerilogRunTimes();
        verilogRunTimes.setTid(tid);
        verilogRunTimes.setUid(uid);
        Example<VerilogRunTimes> verilogRunTimesExample=Example.of(verilogRunTimes);
        try {
            return this.verilogRunTimesRepository.findOne(verilogRunTimesExample).get();
        }catch (Exception e){
            return null;
        }
    }

    //将提交次数+1
    public boolean addVerilogRunTimes(VerilogRunTimes verilogRunTimes){
        verilogRunTimes.setTimes(verilogRunTimes.getTimes()+1);
        verilogRunTimes.setUpdatedate(new Date());
        try {
            this.verilogRunTimesRepository.save(verilogRunTimes);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean saveVerilogRunTimes(VerilogRunTimes verilogRunTimes){
        verilogRunTimes.setUpdatedate(new Date());
        try {
            this.verilogRunTimesRepository.save(verilogRunTimes);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 删除汇编仿真题的暂存信息或者是verilog编程题的提交次数信息
     * @param uid 用户id
     * @param task 题目信息
     * @return true/false
     */
    public boolean deleteStageAndVerilogRunTimes(Long uid,Task task){
        try {
            if (task.getTtype()==0L) {//删除提交次数
                VerilogRunTimes verilogRunTimes=new VerilogRunTimes();
                verilogRunTimes.setTid(task.getTid());
                verilogRunTimes.setUid(uid);
                Example<VerilogRunTimes> verilogRunTimesExample=Example.of(verilogRunTimes);
                Optional<VerilogRunTimes> verilogRunTimesOptional=this.verilogRunTimesRepository.findOne(verilogRunTimesExample);
                if (verilogRunTimesOptional.isPresent()) {
                    this.verilogRunTimesRepository.delete(verilogRunTimesOptional.get());
                    return true;
                }
            }else { //删除暂存信息
                Stage stage=new Stage();
                stage.setUid(uid);
                stage.setTid(task.getTid());
                Example<Stage> stageExample=Example.of(stage);
                Optional<Stage> stageOptional=this.stageRepository.findOne(stageExample);
                if (stageOptional.isPresent()) {
                    this.stageRepository.delete(stageOptional.get());
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }


}
