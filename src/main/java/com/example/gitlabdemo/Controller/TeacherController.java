package com.example.gitlabdemo.Controller;


import com.example.gitlabdemo.Model.DataModel.Question;
import com.example.gitlabdemo.Model.DataModel.Task;
import com.example.gitlabdemo.Model.GitModel.TaskFile;
import com.example.gitlabdemo.Model.GitModel.TaskModel;
import com.example.gitlabdemo.Model.Result;
import com.example.gitlabdemo.Service.QuestionService;
import com.example.gitlabdemo.Service.TaskService;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.OSUtil;
import com.example.gitlabdemo.Util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.gitlabdemo.Util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    GitProcess gitProcess;

    @Autowired
    private TaskService taskService;

    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/createTask")
    public ResponseEntity<Result> createTask(@RequestBody MultipartFile file){
        gitProcess = new GitProcess();
        Question question = new Question();
        question.setCreatedate(new Date(new java.util.Date().getTime()));
        question.setUpdatedate(new Date(new java.util.Date().getTime()));
        try{
            question.setQname(file.getOriginalFilename().split("\\.")[0]);
        } catch (Exception e){
            return ResultUtil.getResult(new Result("请上传.zip包"), HttpStatus.BAD_REQUEST);
        }

        String question_id;
        try{
            questionService.saveQuestion(question);
            question_id = question.getQid().toString();

            System.out.println(question_id);
        } catch (Exception e){
            return ResultUtil.getResult(new Result(e.toString()), HttpStatus.BAD_REQUEST);
        }
        System.out.println("开始文件接收");
        String filePath;
        try{
            filePath = FileUtil.fileUpload(file, question_id);
            System.out.println(filePath);
        } catch (Exception e){
            questionService.delete(question.getQid());
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

        try {
            FileUtil.unZip(filePath);
            System.out.println("解压成功");
        } catch (Exception e){
            questionService.delete(question.getQid());
            return ResultUtil.getResult(new Result("解压失败"), HttpStatus.BAD_REQUEST);
        }

        File f = new File(filePath);
        if (!f.exists()) {
            return ResultUtil.getResult(new Result("文件夹不存在"), HttpStatus.BAD_REQUEST);
        }
        File fa[] = f.listFiles();
        try{
            for(int i = 0; i < fa.length; i++){
                File fs = fa[i];
                Task task = new Task();
                task.setTname(fs.getName());
                task.setQid(question.getQid());
                task.setCreatedate(new Date(new java.util.Date().getTime()));
                task.setUpdatedate(new Date(new java.util.Date().getTime()));
                taskService.saveTask(task);
                String task_id = "t" + task.getTid().toString();
                TaskModel taskModel = new TaskModel();
                taskModel.setTaskFiles(new LinkedList<TaskFile>());
                taskModel.setTask_id(task_id);
                taskModel.setTask_title(fs.getName());

                String task_path = OSUtil.isLinux() ? filePath + "/" + task_id : filePath + "\\" + task_id;
                fs.renameTo(new File(task_path));
                System.out.println(fs.getPath());

                FileUtil.createTaskModel(taskModel, task_path);
                try{
                    gitProcess.gitcreateTask(taskModel);
                }catch (Exception e){
                    e.printStackTrace();
                    return ResultUtil.getResult(new Result(e.toString()), HttpStatus.BAD_REQUEST);
                }

                System.out.println(taskModel.getTask_title() + "  创建成功");

            }
        } catch (Exception e){

            System.out.println(e.toString());
            e.printStackTrace();
            return ResultUtil.getResult(new Result(e.toString()), HttpStatus.BAD_REQUEST);
        }

        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }
}
