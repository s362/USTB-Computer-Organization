package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Entity.Question;
import com.example.gitlabdemo.Entity.QuestionAndTask;
import com.example.gitlabdemo.Repository.CourseAndStudentRepo;
import com.example.gitlabdemo.Repository.QuestionAndTaskRepo;
import com.example.gitlabdemo.Repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("questionService")
public class QuestionService {
    private final QuestionRepo questionRepo;
    private final CourseAndStudentRepo courseAndStudentRepo;
    private final QuestionAndTaskRepo questionAndTaskRepo;

    @Autowired
    public QuestionService(QuestionRepo questionRepo, CourseAndStudentRepo courseAndStudentRepo, QuestionAndTaskRepo questionAndTaskRepo) {
        Assert.notNull(questionRepo, "taskRepository must not be null!");
        Assert.notNull(courseAndStudentRepo, "taskRepository must not be null!");
        Assert.notNull(questionRepo, "QuestionAndTaskRepo must not be null!");
        this.questionRepo = questionRepo;
        this.questionAndTaskRepo = questionAndTaskRepo;
        this.courseAndStudentRepo = courseAndStudentRepo;
    }


    // 保存作业
    public void saveQuestion(Question question)  {
        this.questionRepo.save(question);
        Example<Question> example = Example.of(question);
    }

    // 得到班级的所有作业列表
    public List<Question> getAllQuestion(Long cid){
        Question question = new Question();
        question.setCid(cid);
        Example<Question> example = Example.of(question);
        return questionRepo.findAll(example);
    }

    /**
     * 删除作业
     * @param qid
     */
    public void delete(Long qid){
        this.questionRepo.deleteById(qid);
    }

    /**
     * 设置作业中的题目
     * @param questionAndTask
     */
    public void setQuestionAndTask(QuestionAndTask questionAndTask){
        this.questionAndTaskRepo.save(questionAndTask);
    }
}
