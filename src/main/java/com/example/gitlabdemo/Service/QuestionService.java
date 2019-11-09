package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Entity.Question;
import com.example.gitlabdemo.Entity.QuestionAndTask;
import com.example.gitlabdemo.Repository.CourseAndStudentRepository;
import com.example.gitlabdemo.Repository.QuestionAndTaskRepository;
import com.example.gitlabdemo.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("questionService")
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CourseAndStudentRepository courseAndStudentRepository;
    private final QuestionAndTaskRepository questionAndTaskRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, CourseAndStudentRepository courseAndStudentRepository, QuestionAndTaskRepository questionAndTaskRepository) {
        Assert.notNull(questionRepository, "taskRepository must not be null!");
        Assert.notNull(courseAndStudentRepository, "taskRepository must not be null!");
        Assert.notNull(questionRepository, "QuestionAndTaskRepository must not be null!");
        this.questionRepository = questionRepository;
        this.questionAndTaskRepository = questionAndTaskRepository;
        this.courseAndStudentRepository = courseAndStudentRepository;
    }


    // 保存作业
    public void saveQuestion(Question question)  {
        this.questionRepository.save(question);
        Example<Question> example = Example.of(question);
    }

    // 得到班级的所有作业列表
    public List<Question> getAllQuestion(Long cid){
        Question question = new Question();
        question.setCid(cid);
        Example<Question> example = Example.of(question);
        return questionRepository.findAll(example);
    }

    /**
     * 删除作业
     * @param qid
     */
    public void delete(Long qid){
        this.questionRepository.deleteById(qid);
    }

    /**
     * 设置作业中的题目
     * @param questionAndTask
     */
    public void setQuestionAndTask(QuestionAndTask questionAndTask){
        this.questionAndTaskRepository.save(questionAndTask);
    }
}
