package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.Question;
import com.example.ustbdemo.Model.DataModel.Question_Task;
import com.example.ustbdemo.Model.DataModel.Score;
import com.example.ustbdemo.Repository.QuestionRepository;
import com.example.ustbdemo.Repository.TaskQuestionRepository;
import com.example.ustbdemo.Repository.TaskRepository;
import com.sun.org.apache.bcel.internal.generic.LLOAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

@Service("questionService")
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TaskQuestionRepository taskQuestionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, TaskQuestionRepository taskQuestionRepository) {
        Assert.notNull(questionRepository, "taskRepository must not be null!");
        Assert.notNull(taskQuestionRepository, "taskRepository must not be null!");
        this.questionRepository = questionRepository;
        this.taskQuestionRepository = taskQuestionRepository;
    }

    public void saveTaskQuestion(Question_Task question_task){
        this.taskQuestionRepository.save(question_task);
    }

    public void saveQuestion(Question question) {
        this.questionRepository.save(question);
    }

    public List<Question> getAllQuestion(){
        return questionRepository.findAll();
    }

    public void deleteQuestionById(Long qid){
        Question_Task question_task = new Question_Task();
        question_task.setQid(qid);
        Example<Question_Task> example = Example.of(question_task);
        try {
            List<Question_Task> question_tasks= this.taskQuestionRepository.findAll(example);
            for(Question_Task question_task1 : question_tasks){
                this.taskQuestionRepository.deleteById(question_task1.getQtid());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        this.questionRepository.deleteById(qid);
    }

    public Question getQuestionByQid(Long qid){
        try{
            return this.questionRepository.findById(qid).get();
        } catch (Exception e){
            return  null;
        }
    }

//    public List<Question_Task> getQuestionTasksByQid(){
//
//    }

    public void deleteQuestionTasksByQid(Long qid){
        Question_Task question_task = new Question_Task();
        question_task.setQid(qid);
        Example<Question_Task> question_taskExample = Example.of(question_task);
        List<Question_Task> question_tasks = taskQuestionRepository.findAll(question_taskExample);
        for(Question_Task question_task1 : question_tasks){
            this.taskQuestionRepository.delete(question_task1);
        }
    }
}
