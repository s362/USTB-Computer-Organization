package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.DataModel.Assemble_Choose;
import com.example.ustbdemo.Model.DataModel.Question;
import com.example.ustbdemo.Model.DataModel.Question_Task;
import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Model.GitModel.TaskFile;
import com.example.ustbdemo.Model.GitModel.TaskModel;
import com.example.ustbdemo.Model.UtilModel.ConfigJson;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Service.QuestionService;
import com.example.ustbdemo.Service.ScoreService;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Service.UserService;
import com.example.ustbdemo.Util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    GitProcess gitProcess;

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/getInstruct")
    public ResponseEntity<Result> getInstruct(){
        Result result = new Result();
        result.setObject(taskService.getAllInstruction());
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping(value = "/getSimulation")
    public ResponseEntity<Result> getSimulation(){
        Result result = new Result();
        result.setObject(taskService.getAllSimulation());
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping(value = "/createQuestion")
    public ResponseEntity<Result> createQuestion(@RequestBody JsonNode info){
        Question question = new Question();
        question.setQname(info.path("qname").asText());
        question.setCreatedate(new Date());
        question.setQdis(info.path("qdis").asText());
        question.setEnddate(DateUtil.getNowDate(info.path("enddate").asText()));
        try{
            questionService.saveQuestion(question);
            for(JsonNode jsonNode : info.path("tids")){
                System.out.println(jsonNode.asText());
                questionService.saveTaskQuestion(new Question_Task(question.getQid(), jsonNode.asLong()));
            }
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } catch (Exception e){
            return ResultUtil.getResult(new Result(e.toString(), false), HttpStatus.BAD_REQUEST);
        }
    }

//    创建verilog题目
    @PostMapping(value = "/createVerilogTask")
    public ResponseEntity<Result> createVerilogTask(ConfigJson configJson, String tname, String tdis, @RequestBody MultipartFile taskFile, MultipartFile testFile, MultipartFile exampleFile){
//        获取 gitProscess对象
        gitProcess = new GitProcess();
        String filetyes[] = {"taskFile", "testFile", "exampleFile"};
        Task task = new Task(tname, tdis, 0L);

        try{
            taskService.saveTask(task);
        } catch (Exception e){
            return ResultUtil.getResult(new Result("保存题目失败" + e.toString()), HttpStatus.BAD_REQUEST);
        }
        System.out.println(task.getTid());
        System.out.println("开始文件接收");
        String filePath;
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);
        MultipartFile multipartFiles[] = {taskFile, testFile, exampleFile};
        for(int i = 0; i < 3; i++){
            MultipartFile multipartFile = multipartFiles[i];
            try{
                filePath = FileUtil.fileUpload(multipartFile, filetyes[i], task);
                if(filePath == null) continue;

                List<TaskFile> taskFiles;
                switch (i){
                    case 0: taskFiles = taskModel.getTaskFiles();break;
                    case 1: taskFiles = taskModel.getTestFiels();break;
                    case 2: taskFiles = taskModel.getExampleFiles();break;
                    default: taskFiles = taskModel.getTaskFiles();break;
                }
                FileUtil.setTaskModelFiles(taskFiles, filePath);
                System.out.println(multipartFile.getOriginalFilename() + "   "+ filePath);
                if(multipartFile.getOriginalFilename().endsWith(".zip")){
                    FileUtil.deleteDirectory(filePath);
                }
            } catch (Exception e){
                taskService.deletTaskByTid(task.getTid());
                e.printStackTrace();
                return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
            }
        }

        if (configJson.isConfig()) {
            try{
                taskModel.setConfigJson(new TaskFile());
                taskModel.getConfigJson().setContent(configJson.toJson());
                System.out.println(taskModel.getConfigJson().getContent());
            } catch (Exception e){
                System.out.println(e.toString());
            }
            taskModel.getConfigJson().setTitle("config.json");
            System.out.println("config.json生成成功");
        } else{
            System.out.println("无config文件");
        }
        try{
            gitProcess.gitcreateTask(taskModel);
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("创建题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }


//    创建汇编题目
    @PostMapping(value = "/createAssembleTask")
    public ResponseEntity<Result> createAssembleTask(Task task, Long inputType, String taskCode, @RequestBody MultipartFile taskFile, String chooseTask){
//        获取 gitProscess对象
        gitProcess = new GitProcess();
        task.setTtype(1L);
//        保存题目
        try{
            taskService.saveTask(task);
        } catch (Exception e){
            return ResultUtil.getResult(new Result("保存题目失败" + e.toString()), HttpStatus.BAD_REQUEST);
        }
        System.out.println(task.getTid());
        String filePath;
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);
//        根据输入方式，选择不同的方式
        try{
            if (inputType == 0){
                taskModel.getTaskFiles().add(new TaskFile("code.asm", taskCode));
            } else{
                filePath = FileUtil.fileUpload(taskFile, "taskFile", task);
                FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath);
            }
            System.out.println("文件接收成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

//        创建git工程
        try{
            gitProcess.gitcreateTask(taskModel);
            System.out.println("创建git工程成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("创建题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

//        读取选择题
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode root = mapper.readTree(chooseTask);
            for(JsonNode chooseNode : root.path("chooseTask")){
                Assemble_Choose assemble_choose = new Assemble_Choose();
                String optionsStr = "";
                for (JsonNode optionNode : chooseNode.path("options")){
                    optionsStr += optionNode.asText() + "###";
                }
                String answerStr = "";
                for(JsonNode answerNode : chooseNode.path("answers")){
                    answerStr += answerNode.asText() + "###";
                }
                assemble_choose.setTid(task.getTid());
                assemble_choose.setOptions(optionsStr.substring(0, optionsStr.length()-3));
                assemble_choose.setAnswers(answerStr.substring(0, answerStr.length()-3));
                assemble_choose.setDiscri(chooseNode.path("discri").asText());
                taskService.saveAssembleChoose(assemble_choose);
            }
            System.out.println("创建选择题成功");

        } catch (Exception e){
            System.out.println("创建选择题失败");
            e.printStackTrace();
            taskService.deletTaskByTid(task.getTid());
        }
        System.out.println("创建题目成功");
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteTask")
    public ResponseEntity<Result> deleteTask(Long tid){
        taskService.deletTaskByTid(tid);
        FileUtil.deleteFileByTid(tid);
        try {
            gitProcess.deleteGroupByTid(tid);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteQuestion")
    public ResponseEntity<Result> deleteQuestion(Long qid){
        questionService.deleteQuestionById(qid);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }
}
