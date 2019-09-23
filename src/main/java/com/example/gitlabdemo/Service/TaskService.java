package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Model.DataModel.Task;
import com.example.gitlabdemo.Model.DataModel.User;
import com.example.gitlabdemo.Repository.TaskRepository;
import com.example.gitlabdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("taskService")
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository){
        Assert.notNull(taskRepository, "taskRepository must not be null!");
        this.taskRepository = taskRepository;
    }

    public void saveTask(Task task) throws Exception{
        this.taskRepository.save(task);
        Example<Task> example = Example.of(task);
        task = this.taskRepository.findAll(example).get(0);
    }

    public List<Task> getTaskbyQid(Long qid){
        Task task = new Task();
        task.setQid(qid);

        Example<Task> example = Example.of(task);
        return this.taskRepository.findAll(example);
    }
}
