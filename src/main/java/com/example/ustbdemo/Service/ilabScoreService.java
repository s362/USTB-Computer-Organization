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

import static com.example.ustbdemo.Util.Base64Convert.baseConvertStr;

@Service("ilabscoreService")
public class ilabScoreService {
    private final ilabScoreRepository ilabScoreRepository;
    private final ScoreRepository scoreRepository;
    private final AssembleChooseScoreRepository assembleChooseScoreRepository;
    private final AssembleCodeScoreRepository assembleCodeScoreRepository;
    private final AssembleChooseRepository assembleChooseRepository;
    private final VerilogRunTimesRepository verilogRunTimesRepository;
    private final StageRepository stageRepository;
    @Autowired
    public ilabScoreService(com.example.ustbdemo.Repository.ilabScoreRepository ilabScoreRepository, ScoreRepository scoreRepository, AssembleChooseScoreRepository assembleChooseScoreRepository, AssembleCodeScoreRepository assembleCodeScoreRepository, AssembleChooseRepository assembleChooseRepository, VerilogRunTimesRepository verilogRunTimesRepository, StageRepository stageRepository){
        Assert.notNull(ilabScoreRepository, "ilabScoreRepository must not be null!");
        Assert.notNull(scoreRepository, "scoreRepository must not be null!");
        Assert.notNull(assembleChooseScoreRepository, "assembleChooseScoreRepository must not be null!");
        Assert.notNull(assembleCodeScoreRepository, "assembleCodeScoreRepository must not be null!");
        Assert.notNull(assembleChooseRepository,"assembleChooseRepository must not be null!");
        Assert.notNull(verilogRunTimesRepository,"verilogRunTimesRepository must not be null!");
        Assert.notNull(stageRepository,"stageRepository must not be null!");
        this.ilabScoreRepository = ilabScoreRepository;
        this.scoreRepository = scoreRepository;
        this.assembleChooseScoreRepository = assembleChooseScoreRepository;
        this.assembleCodeScoreRepository = assembleCodeScoreRepository;
        this.assembleChooseRepository = assembleChooseRepository;
        this.verilogRunTimesRepository = verilogRunTimesRepository;
        this.stageRepository = stageRepository;
    }

    public boolean DeleteByName(String username){
        ilabScore ilabscore = new ilabScore();
        ilabscore.setUsername(username);
        Example<ilabScore> example = Example.of(ilabscore);
        try{
            List<ilabScore> result = this.ilabScoreRepository.findAll(example);
            for(ilabScore i:result){
                ilabScoreRepository.delete(i);
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }
    public void saveIalbStep(ilabScore ilabscore)  {
        Example<ilabScore> example = Example.of(ilabscore);
        try{
            ilabscore.setCreatTime(String.valueOf(System.currentTimeMillis()));
            this.ilabScoreRepository.save(ilabscore);

        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public boolean saveIalbEndtime(String username,long step){
        ilabScore ilabscore = new ilabScore();
        ilabscore.setUsername(username);
        ilabscore.setStep(String.valueOf(step));
        Example<ilabScore> example = Example.of(ilabscore);
        try{
            ilabScore result = this.ilabScoreRepository.findOne(example).get();
            this.ilabScoreRepository.deleteById(result.getUid());
            result.setEndTime(String.valueOf(System.currentTimeMillis()- 1000L));
            this.ilabScoreRepository.save(result);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public List<ilabScore> getIlabScoreByUsername(String username){
        ilabScore ilabscore = new ilabScore();
        ilabscore.setUsername(username);
        Example<ilabScore> example = Example.of(ilabscore);
        try{
            List<ilabScore> result = this.ilabScoreRepository.findAll(example);
            if(!result.isEmpty()){
                for(ilabScore tmp : result){
                    getIlabScoreByUsernameStep(tmp.getUsername(),Long.parseLong(tmp.getStep()));
                }
            }
            return this.ilabScoreRepository.findAll(example);
        } catch (Exception e){
            return null;
        }
    }

    public ilabScore getIlabScoreByUsernameStep(String username,long step){
        ilabScore ilabscore = new ilabScore();
        ilabscore.setUsername(username);
        ilabscore.setStep(String.valueOf(step));
        Example<ilabScore> example = Example.of(ilabscore);
        try{
//            ilabScore result = this.ilabScoreRepository.findOne(example).get();

            List<ilabScore> ilabList = this.ilabScoreRepository.findAll(example);
            if(!ilabList.isEmpty()){
                ilabScore result = ilabList.get(0);
                for (ilabScore item:ilabList){
                    if(Long.parseLong(result.getCreatTime())>Long.parseLong(item.getCreatTime())){
                        result = item;
                    }
                }
                for (ilabScore item:ilabList){
                    if(Long.parseLong(result.getCreatTime())!=Long.parseLong(item.getCreatTime())){
                        this.ilabScoreRepository.delete(item);
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


    public ilabScore getNowIlabScore(String username){
        ilabScore ilabscore = new ilabScore();
        ilabscore.setUsername(username);
        Example<ilabScore> example = Example.of(ilabscore);
        try{
            List<ilabScore> ilabList = this.ilabScoreRepository.findAll(example);
            if(!ilabList.isEmpty()){
                ilabScore result = ilabList.get(0);
                for (ilabScore item:ilabList){
                    if(Long.parseLong(result.getStep())<Long.parseLong(item.getStep())){
                        result = item;
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

}
