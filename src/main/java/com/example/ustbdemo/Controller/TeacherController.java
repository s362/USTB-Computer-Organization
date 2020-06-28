package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Model.GitModel.QuestionAndTask;
import com.example.ustbdemo.Model.GitModel.TaskFile;
import com.example.ustbdemo.Model.GitModel.TaskModel;
import com.example.ustbdemo.Model.UtilModel.ChooseModel;
import com.example.ustbdemo.Model.UtilModel.ConfigJson;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Service.QuestionService;
import com.example.ustbdemo.Service.ScoreService;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Service.UserService;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.ls.LSInput;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

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
        List<Instruction> instructions = taskService.getAllInstruction();
        for(Instruction instruction : instructions){
            instruction.setInstrFilePath(PathUtil.toUrlPath(instruction.getInstrFilePath()));
        }
        result.setObject(instructions);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping(value = "/getSimulation")
    public ResponseEntity<Result> getSimulation(){
        Result result = new Result();
        result.setObject(taskService.getAllSimulation());
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<Result> getQuestions(HttpServletRequest httpServletRequest){
        List<Question> questions = questionService.getAllQuestion();
        Result result = new Result();
        result.setObject(questions);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getExampleTaskFile")
    public ResponseEntity<Result> getExampleTaskFile(HttpServletRequest httpServletRequest){
        Result result = new Result();
        result.setObject(PathUtil.toUrlPath(Task.EXAMPLE_TaskFile));
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getTasks")
    public ResponseEntity<Result> getTasks(HttpServletRequest httpServletRequest){
        List<JsonNode> jsonNodes = new LinkedList<>();
        List<Task> tasks = taskService.getAllTasks();
        for(Task task:tasks){
            Map taskMap = new HashMap();
            taskMap.put("tid", task.getTid());
            taskMap.put("tname", task.getTname());
            taskMap.put("tdis", task.getTdis());
            taskMap.put("ttype", task.getTtype());
            if(task.getTtype() == 0L){
                taskMap.put("taskFilePath", PathUtil.toUrlPath(task.getTaskFilePath()));
            } else {
                taskMap.put("taskFilePath", PathUtil.toUrlPath(task.getTaskFilePath()));
                taskMap.put("exampleFilePath", PathUtil.toUrlPath(task.getExampleFilePath()));
                taskMap.put("simuPicPath1", PathUtil.toUrlPath(task.getSimuPicPath1()));
                taskMap.put("simuPicPath2", PathUtil.toUrlPath(task.getSimuPicPath2()));
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode taskNode = mapper.readTree(objectMapper.writeValueAsString(taskMap));
                jsonNodes.add(taskNode);
            } catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        Result result = new Result();
        result.setObject(jsonNodes);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }


    @PostMapping("/getChooseByTid")
    public ResponseEntity<Result> getChooseByTid(Long tid){
        List<Assemble_Choose> assemble_chooses = taskService.getAssebleChoosesByTid(tid);
        Result result = new Result(assemble_chooses);
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
                questionService.saveTaskQuestion(new Question_Task(question.getQid(), jsonNode.asLong()));
            }
            System.out.println("创建题目成功");
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } catch (Exception e){
            System.out.println("创建题目失败");
            return ResultUtil.getResult(new Result(e.toString(), false), HttpStatus.BAD_REQUEST);
        }
    }

    //    创建题目
    @PostMapping(value = "/createVerilogTask")
    public ResponseEntity<Result> createVerilogTask(String tname, @RequestBody MultipartFile taskFile){
//        获取 gitProscess对象
        gitProcess = new GitProcess();
        Task task = new Task(tname, "", 0L);

        taskService.saveTask(task);
        System.out.println(task.getTid() + "开始文件接收");
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);

        try{
            String filePath;
            filePath = FileUtil.fileUpload(taskFile, task, "");
            if(filePath == null) {
                return ResultUtil.getResult(new Result("未上传文件"), HttpStatus.BAD_REQUEST);
            }

            FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "files");
            FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "example");
            task.setTdis(FileUtil.setMdContent(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "content.md"));
            System.out.println("处理md文件");
            FileUtil.moveTaskImg(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "images");
            FileUtil.deleteDirectory(filePath);
            System.out.println("文件接收成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

        try{
            gitProcess.gitcreateTask(taskModel);
            System.out.println("创建Git成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("创建题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
        taskService.saveTask(task);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

//    创建汇编题目
    @PostMapping(value = "/createAssembleTask")
    public ResponseEntity<Result> createAssembleTask(Task task, @RequestBody MultipartFile taskFile, MultipartFile exampleFile, MultipartFile simuPic1, MultipartFile simuPic2, String chooseTask){
//        获取 gitProscess对象
        gitProcess = new GitProcess();
        task.setTtype(1L);

        if(task.getSimuid1() == null || taskService.getSimulationBySimuid(task.getSimuid1()) == null ||
            task.getSimuid2() == null || taskService.getSimulationBySimuid(task.getSimuid2()) == null ||
            task.getInstrid() == null || taskService.getInstructionByinstrid(task.getInstrid()) == null){
            return ResultUtil.getResult(new Result("仿真器或者指令集选择有误"), HttpStatus.BAD_REQUEST);
        }

//        保存题目
        taskService.saveTask(task);

        System.out.println(task.getTid());
        String filePath;
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);
//        根据输入方式，选择不同的方式
        try{
            filePath = FileUtil.fileUpload(taskFile, task, "taskFile");
            FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath);
            filePath = FileUtil.fileUpload(exampleFile, task, "exampleFile");
            FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath);
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
            System.out.println("创建git题目失败");
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("创建git题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

//        读取选择题
        ObjectMapper mapper = new ObjectMapper();
        try{
            if(chooseTask != null){
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
            }
        } catch (Exception e){
            System.out.println("创建选择题失败");
            e.printStackTrace();
            taskService.deletTaskByTid(task.getTid());
        }

        try{
            String simulatePicPath1 = FileUtil.saveStaticUploadFile(simuPic1);
            if (simulatePicPath1 != null) task.setSimuPicPath1(simulatePicPath1);
            else task.setSimuPicPath1(Simulation.EXAMPLE_SIMULATION_PICPATH);
        } catch (Exception e){
            task.setSimuPicPath1(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }
        try {
            String simulatePicPath2 = FileUtil.saveStaticUploadFile(simuPic2);
            if (simulatePicPath2 != null) task.setSimuPicPath2(simulatePicPath2);
            else task.setSimuPicPath2(Simulation.EXAMPLE_SIMULATION_PICPATH);
        } catch (Exception e){
            task.setSimuPicPath2(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }

        taskService.saveTask(task);
        System.out.println("创建题目成功");
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @PostMapping(value = "/uploadInstructionFile")
    public ResponseEntity<Result> uploadInstructionFile(@RequestBody MultipartFile instrFile){
        if (instrFile != null){
            Instruction instruction = new Instruction();
            instruction.setInstrname(instrFile.getOriginalFilename());
            String instruFilePath = FileUtil.saveStaticUploadFile(instrFile);
            instruction.setInstrFilePath(instruFilePath);
            this.taskService.saveInstruction(instruction);
        }
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteTask")
    public ResponseEntity<Result> deleteTask(Long tid){
        taskService.deletTaskByTid(tid);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteQuestion")
    public ResponseEntity<Result> deleteQuestion(Long qid){
        questionService.deleteQuestionById(qid);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }


    //    创建verilog题目
//    @PostMapping(value = "/createVerilogTask")
//    public ResponseEntity<Result> createVerilogTask(ConfigJson configJson, String tname, String tdis, @RequestBody MultipartFile taskFile, MultipartFile testFile, MultipartFile exampleFile){
//        获取 gitProscess对象
//        gitProcess = new GitProcess();
//        String filetyes[] = {"taskFile", "testFile", "exampleFile"};
//        Task task = new Task(tname, tdis, 0L);
//
//        try{
//            taskService.saveTask(task);
//        } catch (Exception e){
//            return ResultUtil.getResult(new Result("保存题目失败" + e.toString()), HttpStatus.BAD_REQUEST);
//        }
//        System.out.println(task.getTid());
//        System.out.println("开始文件接收");
//        String filePath;
//        String task_id = GitProcess.tidToTaskid(task.getTid());
//        TaskModel taskModel = new TaskModel(task_id);
//        MultipartFile multipartFiles[] = {taskFile, testFile, exampleFile};
//        for(int i = 0; i < 3; i++){
//            MultipartFile multipartFile = multipartFiles[i];
//            try{
//                filePath = FileUtil.fileUpload(multipartFile, filetyes[i], task);
//                if(filePath == null) continue;
//
//                List<TaskFile> taskFiles;
//                switch (i){
//                    case 0: taskFiles = taskModel.getTaskFiles();break;
//                    case 1: taskFiles = taskModel.getTestFiels();break;
//                    case 2: taskFiles = taskModel.getExampleFiles();break;
//                    default: taskFiles = taskModel.getTaskFiles();break;
//                }
//                FileUtil.setTaskModelFiles(taskFiles, filePath);
//                System.out.println(multipartFile.getOriginalFilename() + "   "+ filePath);
//                if(multipartFile.getOriginalFilename().endsWith(".zip")){
//                    FileUtil.deleteDirectory(filePath);
//                }
//            } catch (Exception e){
//                taskService.deletTaskByTid(task.getTid());
//                e.printStackTrace();
//                return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
//            }
//        }
//
//        if (configJson.isConfig()) {
//            try{
//                taskModel.setConfigJson(new TaskFile());
//                taskModel.getConfigJson().setContent(configJson.toJson());
//                System.out.println(taskModel.getConfigJson().getContent());
//            } catch (Exception e){
//                System.out.println(e.toString());
//            }
//            taskModel.getConfigJson().setTitle("config.json");
//            System.out.println("config.json生成成功");
//        } else{
//            System.out.println("无config文件");
//        }
//        try{
//            gitProcess.gitcreateTask(taskModel);
//        } catch (Exception e){
//            taskService.deletTaskByTid(task.getTid());
//            e.printStackTrace();
//            return ResultUtil.getResult(new Result("创建题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
//        }
//        taskService.saveTask(task);
//        return ResultUtil.getResult(new Result(), HttpStatus.OK);
//    }
}
