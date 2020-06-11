package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Repository.*;
import com.example.ustbdemo.Util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;

@Service("taskService")
public class TaskService {
    private final TaskRepository taskRepository;
    private final ScoreRepository scoreRepository;
    private final TaskQuestionRepository taskQuestionRepository;
    private final SimulationRepository simulationRepository;
    private final InstructionRepository instructionRepository;
    private final TaskAssembleRepository taskAssembleRepository;
    private final TaskVerilogRepository taskVerilogRepository;
    private final TaskChooseRepository taskChooseRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ScoreRepository scoreRepository,
                       TaskQuestionRepository taskQuestionRepository, SimulationRepository simulationRepository,
                       InstructionRepository instructionRepository,
                       TaskAssembleRepository taskAssembleRepository,
                       TaskVerilogRepository taskVerilogRepository,
                       TaskChooseRepository taskChooseRepository){
        Assert.notNull(taskRepository, "taskRepository must not be null!");
        Assert.notNull(scoreRepository, "taskRepository must not be null!");
        Assert.notNull(taskQuestionRepository, "taskRepository must not be null!");
        Assert.notNull(instructionRepository, "taskRepository must not be null!");
        Assert.notNull(simulationRepository, "taskRepository must not be null!");
        Assert.notNull(taskAssembleRepository, "taskRepository must not be null!");
        Assert.notNull(taskVerilogRepository, "taskRepository must not be null!");
        Assert.notNull(taskChooseRepository, "taskRepository must not be null!");
        this.taskRepository = taskRepository;
        this.scoreRepository = scoreRepository;
        this.taskQuestionRepository = taskQuestionRepository;
        this.instructionRepository = instructionRepository;
        this.simulationRepository = simulationRepository;
        this.taskAssembleRepository = taskAssembleRepository;
        this.taskVerilogRepository = taskVerilogRepository;
        this.taskChooseRepository = taskChooseRepository;
    }

    public List<Simulation> getAllSimulation(){
        return this.simulationRepository.findAll();
    }

    public void addSimulation(Simulation simulation){
        this.simulationRepository.save(simulation);
    }

    public List<Instruction> getAllInstruction(){
        return this.instructionRepository.findAll();
    }

    public void addInstruction(Instruction instruction){
        this.instructionRepository.save(instruction);
    }

    public void saveTask(Task task) {
        this.taskRepository.save(task);
    }

    public void saveTaskChoose(TaskChoose taskChoose){
        this.taskChooseRepository.save(taskChoose);
    }

    public void saveTaskAssemble(TaskAssemble taskAssemble){
        this.taskAssembleRepository.save(taskAssemble);
    }

    public void saveTaskVerilog(TaskVerilog taskVerilog){
        this.taskVerilogRepository.save(taskVerilog);
    }


    public List<Task> getTaskbyQid(Long qid){
        Question_Task question_task = new Question_Task();
        question_task.setQid(qid);
        Example<Question_Task> example = Example.of(question_task);
        List<Question_Task> question_tasks = this.taskQuestionRepository.findAll(example);
        List<Task> tasks = new LinkedList<>();
        for(Question_Task questionTask : question_tasks){
            try{
                tasks.add(this.taskRepository.getOne(questionTask.getTid()));
            } catch (EntityNotFoundException e){
                System.out.println("不存在该题号");
                taskQuestionRepository.deleteById(questionTask.getQtid());
                continue;
            }
        }
        return tasks;
    }

//    根据题目id删除题目，并且删除所有学生该题目记录
    public void deletTaskByTid(Long tid){
        Score score = new Score();
        score.setTid(tid);

        Task task = this.taskRepository.getOne(tid);

//        删除所有选择题
        if (task.getTtype() == 1L){
            Assemble_Choose assemble_choose = new Assemble_Choose();
            assemble_choose.setTid(tid);
            Example<Assemble_Choose> exampleAssemble = Example.of(assemble_choose);
            try {
                List<Assemble_Choose> assemble_chooses = this.assembleChooseRepository.findAll(exampleAssemble);
                for(Assemble_Choose assemble_choose1 : assemble_chooses){
                    this.assembleChooseRepository.delete(assemble_choose1);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        this.taskRepository.deleteById(tid);
        FileUtil.deleteFileByTid(tid);
        Example<Score> example = Example.of(score);
        try {
            List<Score> scores = this.scoreRepository.findAll(example);
            for(Score score1:scores){
                this.scoreRepository.deleteById(score1.getSid());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
