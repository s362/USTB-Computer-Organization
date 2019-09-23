package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Model.DataModel.Question;
import com.example.gitlabdemo.Model.DataModel.Task;
import com.example.gitlabdemo.Repository.QuestionRepository;
import com.example.gitlabdemo.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("questionService")
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        Assert.notNull(questionRepository, "taskRepository must not be null!");
        this.questionRepository = questionRepository;
    }

    public void saveQuestion(Question question) throws Exception {
        this.questionRepository.save(question);
        Example<Question> example = Example.of(question);
       question = this.questionRepository.findAll(example).get(0);
    }

    public List<Question> getAllQuestion(){
        return questionRepository.findAll();
    }
    public void delete(Long qid){
        this.questionRepository.deleteById(qid);
    }
}
