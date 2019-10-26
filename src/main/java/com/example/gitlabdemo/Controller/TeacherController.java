package com.example.gitlabdemo.Controller;

import com.example.gitlabdemo.Model.DataModel.Task;
import com.example.gitlabdemo.Model.GitModel.TaskFile;
import com.example.gitlabdemo.Model.GitModel.TaskModel;
import com.example.gitlabdemo.Model.Result;
import com.example.gitlabdemo.Service.QuestionService;
import com.example.gitlabdemo.Service.TaskService;
import com.example.gitlabdemo.Util.FileUtil;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.OSUtil;
import com.example.gitlabdemo.Util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
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

    /**
     * 提交压缩文件夹，创建题目
     * @param file .zip文件
     * @return
     */
    @PostMapping(value = "/createTask")
    public ResponseEntity<Result> createTask(@RequestBody MultipartFile file){
        gitProcess = new GitProcess();
//        必须为.zip文件
        try{
            System.out.println(file.getOriginalFilename());
            String _fileName = file.getOriginalFilename().split("\\.")[0];
            if(_fileName == "zip"){
                throw new Exception();
            }
        } catch (Exception e){
            return ResultUtil.getResult(new Result("请上传.zip包"), HttpStatus.BAD_REQUEST);
        }

        System.out.println("开始文件接收");
        String filePath;
//        开始文件接收
        try{
            filePath = FileUtil.fileUpload(file);
            System.out.println(filePath);
        } catch (Exception e){
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

//        开始解压，解压到 filepath中
        try {
            FileUtil.unZip(filePath);
            System.out.println("解压成功");
        } catch (Exception e){
            return ResultUtil.getResult(new Result("解压失败"), HttpStatus.BAD_REQUEST);
        }

        File f = new File(filePath);
        if (!f.exists()) {
            return ResultUtil.getResult(new Result("文件夹不存在"), HttpStatus.BAD_REQUEST);
        }
//        遍历该文件下的所有文件（其下每个文件夹都是一个题目）
        File fa[] = f.listFiles();
        try{
            for(int i = 0; i < fa.length; i++){
                File fs = fa[i];
                Task task = new Task();
                task.setTname(fs.getName());
                task.setCreatedate(new Date());
                task.setUpdatedate(new Date());
//                将题目信息存在数据库中，并得到题目id
                taskService.saveTask(task);
                Long tid = task.getTid();
                TaskModel taskModel = new TaskModel();
                taskModel.setTaskFiles(new LinkedList<TaskFile>());
                taskModel.setTask_id("t" + tid);
                taskModel.setTask_title(fs.getName());

                String task_path = fs.getParentFile().getParentFile().getPath() + (OSUtil.isLinux() ?  "/" + tid : "\\" + tid);
                fs.renameTo(new File(task_path));
                System.out.println(fs.getPath());

                FileUtil.createTaskModel(taskModel, fs.getPath());
//                在gitlab中创建相应的group与teacher工程
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
        f.delete();

        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }
}
