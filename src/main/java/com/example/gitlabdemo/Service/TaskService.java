package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Entity.QuestionAndTask;
import com.example.gitlabdemo.Entity.Task;
import com.example.gitlabdemo.Repository.QuestionAndTaskRepository;
import com.example.gitlabdemo.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

@Service("taskService")
public class TaskService {
    private final TaskRepository taskRepository;
    private final QuestionAndTaskRepository questionAndTaskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository,  QuestionAndTaskRepository questionAndTaskRepository){
        Assert.notNull(taskRepository, "taskRepository must not be null!");
        Assert.notNull(questionAndTaskRepository, "taskRepository must not be null!");
        this.taskRepository = taskRepository;
        this.questionAndTaskRepository = questionAndTaskRepository;
    }

    /**
     * 保存题目
     * @param task task对象
     * @throws Exception
     */
    public void saveTask(Task task) throws Exception{
        this.taskRepository.save(task);
        Example<Task> example = Example.of(task);
    }

    /**
     * 根据题目id返回题目对象
     * @param tid
     * @return
     */
    public Task getTaskbyTid(Long tid){
        Task task = taskRepository.getOne(tid);
        return task;
    }

    /**
     * 得到某次作业下的所有题目
     * @param qid 作业id
     * @return
     */
    public List<Task> getTaskbyQid(Long qid){
        QuestionAndTask questionAndTask = new QuestionAndTask();
        questionAndTask.setQid(qid);
        List<QuestionAndTask> questionAndTasks;
        Example<QuestionAndTask> exampleQT = Example.of(questionAndTask);
        questionAndTasks = this.questionAndTaskRepository.findAll(exampleQT);

        List<Task> tasks = new LinkedList<>();
        for(QuestionAndTask _questAndTask : questionAndTasks){
            Task task = new Task();
            task.setTid(_questAndTask.getTid());
            Example<Task> example = Example.of(task);
            tasks.add(this.taskRepository.findOne(example).get());
        }
        return tasks;
    }
}
