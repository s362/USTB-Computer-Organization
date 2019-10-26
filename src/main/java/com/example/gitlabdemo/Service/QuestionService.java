package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Model.DataModel.Question;
import com.example.gitlabdemo.Repository.CourseAndStudentRepository;
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
    private final CourseAndStudentRepository courseAndStudentRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, CourseAndStudentRepository courseAndStudentRepository) {
        Assert.notNull(questionRepository, "taskRepository must not be null!");
        Assert.notNull(courseAndStudentRepository, "taskRepository must not be null!");
        this.questionRepository = questionRepository;
        this.courseAndStudentRepository = courseAndStudentRepository;
    }

//    保存作业
    public void saveQuestion(Question question) throws Exception {
        this.questionRepository.save(question);
        Example<Question> example = Example.of(question);
       question = this.questionRepository.findAll(example).get(0);
    }

//    得到班级的所有作业列表
    public List<Question> getAllQuestion(Long cid){
        Question question = new Question();
        question.setCid(cid);
        Example<Question> example = Example.of(question);
        return questionRepository.findAll(example);
    }

    public void delete(Long qid){
        this.questionRepository.deleteById(qid);
    }
}
