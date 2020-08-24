package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Model.GitModel.TaskModel;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Service.*;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    GitProcess gitProcess;

    public static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ManageService manageService;

//    获取instruct
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

//    获取仿真器
    @PostMapping(value = "/getSimulation")
    public ResponseEntity<Result> getSimulation(){
        Result result = new Result();
        result.setObject(taskService.getAllSimulation());
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

//    返回所有作业和他下面的题目
    @PostMapping("/getQuestionAndTasks")
    public ResponseEntity<Result> getQuestionAndTasks(HttpServletRequest httpServletRequest){
        try{
            List<Question> questions = questionService.getAllQuestion();
            List<Map> jsonNodes = new LinkedList<>();
            for(Question question : questions){
                Map questionMap = new HashMap();
                questionMap.put("qid", question.getQid());
                questionMap.put("qname", question.getQname());
                questionMap.put("qdis", question.getQdis());
                questionMap.put("enddate", question.getEnddate());

                List<Task> tasks = taskService.getTaskbyQid(question.getQid());
                questionMap.put("tasks", getMapTasks(tasks));
                jsonNodes.add(questionMap);
            }
            return ResultUtil.getResult(new Result(jsonNodes), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getResult(new Result(e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

//    获取选择题
    @PostMapping("/getChooseByTid")
    public ResponseEntity<Result> getChooseByTid(Long tid, HttpServletRequest httpServletRequest){
        List<Assemble_Choose> assemble_chooses = taskService.getAssebleChoosesByTid(tid);
        List<Map> jsonNodes = new LinkedList<>();
        for (Assemble_Choose assemble_choose : assemble_chooses){
            Map chooseModel = new HashMap();
            chooseModel.put("tcid", assemble_choose.getTcid());
            chooseModel.put("discri", assemble_choose.getDiscri());
            chooseModel.put("options", Arrays.asList(assemble_choose.getOptions().split("###")));
            chooseModel.put("answers", Arrays.asList(assemble_choose.getAnswers().split("###")));
            jsonNodes.add(chooseModel);
        }
        return ResultUtil.getResult(new Result(jsonNodes), HttpStatus.OK);
    }

//    获取所有作业
    @PostMapping("/getQuestions")
    public ResponseEntity<Result> getQuestions(HttpServletRequest httpServletRequest){
        List<Question> questions = questionService.getAllQuestion();
        Result result = new Result();
        result.setObject(questions);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }


//    @PostMapping("/getExampleTaskFile")
//    public ResponseEntity<Result> getExampleTaskFile(HttpServletRequest httpServletRequest){
//        Result result = new Result();
//        result.setObject(PathUtil.toUrlPath(Task.EXAMPLE_TaskFile));
//        return ResultUtil.getResult(result, HttpStatus.OK);
//    }

//    获取所有题目
    @PostMapping("/getTasks")
    public ResponseEntity<Result> getTasks(HttpServletRequest httpServletRequest){
        List<Task> tasks = taskService.getAllTasks();
        List<JsonNode> jsonNodes = getMapTasks(tasks);
        Result result = new Result();
        result.setObject(jsonNodes);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

//    把所有题目根据题目类型转换为json格式返回
    private List<JsonNode> getMapTasks(List<Task> tasks){
        List<JsonNode> jsonNodes = new LinkedList<>();
        for(Task task:tasks){
            Map taskMap = new HashMap();
            taskMap.put("tid", task.getTid());
            taskMap.put("tname", task.getTname());
            taskMap.put("ttype", task.getTtype());
            if(task.getTtype() == 0L){
//                taskMap.put("taskFilePath", PathUtil.toUrlPath(task.getTaskFilePath()));
            } else {
//                taskMap.put("taskFilePath", PathUtil.toUrlPath(task.getTaskFilePath()));
//                taskMap.put("exampleFilePath", PathUtil.toUrlPath(task.getExampleFilePath()));
                taskMap.put("tdis", task.getTdis());
                taskMap.put("simuPicPath1", PathUtil.toUrlPath(task.getSimuPicPath1()));
                taskMap.put("simuPicPath2", PathUtil.toUrlPath(task.getSimuPicPath2()));
                taskMap.put("simuid1",task.getSimuid1());
                taskMap.put("simuid2",task.getSimuid2());
                taskMap.put("instrid",task.getInstrid());
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
        return jsonNodes;
    }

//    创建作业
    @PostMapping(value = "/createQuestion")
    public ResponseEntity<Result> createQuestion(@RequestBody JsonNode info){
        Question question = new Question();
//        判断题目是否存在
        for(JsonNode jsonNode : info.path("tids")){
            Task task = taskService.getTaskByTid(jsonNode.asLong());
            if(task == null){
                return ResultUtil.getResult(new Result(jsonNode.asLong() + "  题目不存在", false), HttpStatus.BAD_REQUEST);
            }
        }
        question.setQname(info.path("qname").asText());
        question.setCreatedate(new Date());
        question.setQdis(info.path("qdis").asText());
        question.setEnddate(DateUtil.getNowDate(info.path("enddate").asText()));
        try{
            questionService.saveQuestion(question);
            for(JsonNode jsonNode : info.path("tids")){
                questionService.saveTaskQuestion(new Question_Task(question.getQid(), jsonNode.asLong()));
            }
            logger.info("创建题目成功");
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } catch (Exception e){
            logger.info("创建题目失败");
            return ResultUtil.getResult(new Result(e.toString(), false), HttpStatus.BAD_REQUEST);
        }
    }
//    编辑作业
    @PostMapping(value = "/editQuestion")
    public ResponseEntity<Result> editQuestion(@RequestBody JsonNode info){
//        判断题目是否存在
        for(JsonNode jsonNode : info.path("tids")){
            Task task = taskService.getTaskByTid(jsonNode.asLong());
            if(task == null){
                return ResultUtil.getResult(new Result(jsonNode.asLong() + "  题目不存在", false), HttpStatus.BAD_REQUEST);
            }
        }
        Question question = questionService.getQuestionByQid(info.path("qid").asLong());
        question.setQname(info.path("qname").asText());
        question.setCreatedate(new Date());
        question.setQdis(info.path("qdis").asText());
        question.setEnddate(DateUtil.getNowDate(info.path("enddate").asText()));
        try{
            questionService.saveQuestion(question);
            questionService.deleteQuestionTasksByQid(question.getQid());
            for(JsonNode jsonNode : info.path("tids")){
                questionService.saveTaskQuestion(new Question_Task(question.getQid(), jsonNode.asLong()));
            }
            logger.info("修改题目成功");
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } catch (Exception e){
            logger.info("修改题目失败");
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
        logger.info(task.getTid() + "开始文件接收");
        String task_id = GitProcess.tidToTaskid(task.getTid());
//        创建gitlab工程生成所对应的对象
        TaskModel taskModel = new TaskModel(task_id);

        try{
            String filePath;
//            接收verilog上传的文件
            filePath = FileUtil.fileUpload(taskFile, task, "", "");
            if(filePath == null) {
                return ResultUtil.getResult(new Result("未上传文件"), HttpStatus.BAD_REQUEST);
            }
//            设置taskFile变量
            FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "files");
            //            设置exampleFile变量

            FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "example");
//            获取content.md内容，并修改其中图片路径，
            task.setTdis(FileUtil.setMdContent(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "content.md"));
            logger.info("处理md文件");
//            把图片移动到静态文件中
            FileUtil.moveTaskImg(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "images");
            FileUtil.deleteDirectory(filePath);
            logger.info("文件接收成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

        try{
//            在gitlab中创建工程
            gitProcess.gitcreateTask(taskModel);
            logger.info("创建Git成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("创建题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
        taskService.saveTask(task);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

//    修改题目
    @PostMapping(value = "/editVerilogTask")
    public ResponseEntity<Result> editVerilogTask(Long tid, String tname, @RequestBody MultipartFile taskFile){
//        获取 gitProscess对象
        gitProcess = new GitProcess();
        Task task = taskService.getTaskByTid(tid);

        if(tname != null) task.setTname(tname);
        taskService.saveTask(task);
//        如果不修改文件，直接返回
        if(taskFile == null || taskFile.isEmpty()){
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        }
//        删除之前的文件
        FileUtil.deleteFileByTid(tid);
        logger.info(task.getTid() + "开始文件接收");
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);

        try{
            String filePath;
//            接收文件
            filePath = FileUtil.fileUpload(taskFile, task, "", "");
            if(filePath == null) {
                return ResultUtil.getResult(new Result("未上传文件"), HttpStatus.BAD_REQUEST);
            }
//            和创建一样。
            FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "files");
            FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "example");
            task.setTdis(FileUtil.setMdContent(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "content.md"));
            logger.info("处理md文件");
            FileUtil.moveTaskImg(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "images");
            FileUtil.deleteDirectory(filePath);
            logger.info("文件接收成功");
        } catch (Exception e){
//            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

        try{
            if(!taskModel.getTaskFiles().isEmpty() || !taskModel.getExampleFiles().isEmpty()) {
                try{
//                    先删除源gitlab 工程
                    gitProcess.deleteProject(GitProcess.tidToTaskid(tid), "teacher");
                    logger.info("删除源工程成功");
//                    这里必须加一个100ms延迟，不然gitlab后台没有清空上一个删除的工程缓存会报错
//                    100ms是我猜的，10ms不够
                    Thread.sleep(100);
                } catch (Exception e){
                    logger.info("无源工程，直接创建");
                }
//                创建新工程
                gitProcess.gitcreateTask(taskModel);
            }
            logger.info("修改git成功");
        } catch (Exception e){
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
//        if(taskFile == null || taskFile.isEmpty()){
//            return ResultUtil.getResult(new Result("未上传taskFile"), HttpStatus.BAD_REQUEST);
//        }
//        if(exampleFile == null || exampleFile.isEmpty()){
//            return ResultUtil.getResult(new Result("未上传exampleFile"), HttpStatus.BAD_REQUEST);
//        }

//        保存题目
        taskService.saveTask(task);

        logger.info(task.getTid().toString());
        String filePath;
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);
//        根据输入方式，选择不同的方式
        try{
//            将taskFile传到 taskFile文件夹中
            filePath = FileUtil.fileUpload(taskFile, task, "taskFile", "code.asm");
//            设置taskFile变量
            FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath);
//            将exampleFile传到exampleFile文件夹中
            filePath = FileUtil.fileUpload(exampleFile, task, "exampleFile", "code.asm");
            FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath);
            logger.info("文件接收成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
//        创建git工程
        try{
            gitProcess.gitcreateTask(taskModel);
            logger.info("创建git工程成功");
        } catch (Exception e){
            logger.info("创建git题目失败");
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
//                    用 ### 进行拼接
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
                    assemble_choose.setTpart(chooseNode.path("partid").asInt());
                    taskService.saveAssembleChoose(assemble_choose);
                }
                logger.info("创建选择题成功");
            }
        } catch (Exception e){
            logger.info("创建选择题失败");
            e.printStackTrace();
            taskService.deletTaskByTid(task.getTid());
        }
        logger.info("开始接受图片");
        try{
//            吧图片放进静态资源文件夹中
            String simulatePicPath1 = FileUtil.saveStaticUploadFile(simuPic1);
            if (simulatePicPath1 != null) {
                logger.info("图片1接受成功" + simuPic1.getOriginalFilename());
                task.setSimuPicPath1(simulatePicPath1);
            }
            else {
                logger.info("无图片1，使用默认图片");
                task.setSimuPicPath1(Simulation.EXAMPLE_SIMULATION_PICPATH);
            }
        } catch (Exception e){
            task.setSimuPicPath1(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }
        try {
            String simulatePicPath2 = FileUtil.saveStaticUploadFile(simuPic2);
            if (simulatePicPath2 != null) {
                logger.info("图片2接受成功" + simuPic1.getOriginalFilename());
                task.setSimuPicPath2(simulatePicPath2);
            }
            else {
                logger.info("无图片2，使用默认图片");
                task.setSimuPicPath2(Simulation.EXAMPLE_SIMULATION_PICPATH);
            }
        } catch (Exception e){
            task.setSimuPicPath2(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }

        taskService.saveTask(task);
        logger.info("创建题目成功");
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

//    修改汇编题目
    @PostMapping(value = "/editAssembleTask")
    public ResponseEntity<Result> editAssembleTask(Task task, @RequestBody MultipartFile taskFile, MultipartFile exampleFile, MultipartFile simuPic1, MultipartFile simuPic2, String chooseTask){
//        获取 gitProscess对象
        gitProcess = new GitProcess();

        Task dataTask = taskService.getTaskByTid(task.getTid());
        if((task.getSimuid1() != null && taskService.getSimulationBySimuid(task.getSimuid1()) == null) ||
                (task.getSimuid2() != null && taskService.getSimulationBySimuid(task.getSimuid2()) == null) ||
                        (task.getInstrid() != null && taskService.getInstructionByinstrid(task.getInstrid()) == null)){
            return ResultUtil.getResult(new Result("仿真器或者指令集选择有误"), HttpStatus.BAD_REQUEST);
        }

//       修改题目
        if(task.getInstrid() != null) dataTask.setInstrid(task.getInstrid());
        if(task.getSimuid1() != null) dataTask.setSimuid1(task.getSimuid1());
        if(task.getSimuid2() != null) dataTask.setSimuid2(task.getSimuid2());
        if(task.getTdis() != null) dataTask.setTdis(task.getTdis());
        if (task.getTname()!=null) dataTask.setTname(task.getTname());
        taskService.saveTask(dataTask);
        task = dataTask;
        logger.info(String.format("tid=%d", task.getTid()));
        String filePath;
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);
//        根据输入方式，选择不同的方式

        try{
//            如果更新文件，才进行这一步
            if(!((taskFile == null || taskFile.isEmpty()) && (exampleFile == null || exampleFile.isEmpty()))) {
                filePath = FileUtil.FILE_PATH_LINUX + task.getTid().toString() + "/" + "taskFile";
                if(!(taskFile == null || taskFile.isEmpty())){
                    logger.info("修改taskFile文件");
                    FileUtil.deleteDirectory(filePath);
                    filePath = FileUtil.fileUpload(taskFile, task, "taskFile", "code.asm");
                }
                FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath);

                filePath = FileUtil.FILE_PATH_LINUX + task.getTid().toString() + "/" + "exampleFile";
                if(!(exampleFile == null || exampleFile.isEmpty())){
                    logger.info("修改exampleFile文件");
                    FileUtil.deleteDirectory(filePath);
                    filePath = FileUtil.fileUpload(exampleFile, task, "exampleFile", "code.asm");
                }
                FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath);
                logger.info("文件接收成功");
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
//        创建git工程
        try{
            if(!taskModel.getTaskFiles().isEmpty() || !taskModel.getExampleFiles().isEmpty()){
                try{
                    gitProcess.deleteProject(GitProcess.tidToTaskid(task.getTid()), "teacher");
                    Thread.sleep(100);
                } catch (Exception e){}
                gitProcess.gitcreateTask(taskModel);
            }
            logger.info("创建git工程成功");
        } catch (Exception e){
            logger.info("创建git题目失败");
//            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("创建git题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

//        读取选择题
        ObjectMapper mapper = new ObjectMapper();
        try{
            if(chooseTask != null){
                JsonNode root = mapper.readTree(chooseTask);
                List<Assemble_Choose> assemble_choose_before = taskService.getAssebleChoosesByTid(task.getTid());
                for(JsonNode chooseNode : root.path("chooseTask")){
                    Assemble_Choose assemble_choose;
//                    如果有tcid字段，说明是之前的题目，进行修改
                    if(chooseNode.hasNonNull("tcid")){
                        assemble_choose = taskService.getAssembleChooseByTid(chooseNode.path("tcid").asLong());
                        if(assemble_choose.getTid() != task.getTid()) {
                            logger.warn(String.format("选择题对应的题号与不符tid=%d assemble_tid=%d", task.getTid(), assemble_choose.getTid()));
                            continue;
                        }
                        for(Assemble_Choose temp : assemble_choose_before){
                            if(temp.getTcid() == assemble_choose.getTcid()){
                                temp.setTcid(null);
                            }
                        }
//                        如果没有，则为新增加的题目，直接添加
                    } else{
                        assemble_choose = new Assemble_Choose();
                    }

                    String optionsStr = "";
                    for (JsonNode optionNode : chooseNode.path("options")){
                        optionsStr += optionNode.asText() + "###";
                    }
                    String answerStr = "";
                    for(JsonNode answerNode : chooseNode.path("answers")){
                        answerStr += answerNode.asText() + "###";
                    }
                    assemble_choose.setTid(task.getTid());
                    assemble_choose.setTpart(chooseNode.path("partid").asInt());
                    assemble_choose.setOptions(optionsStr.substring(0, optionsStr.length()-3));
                    assemble_choose.setAnswers(answerStr.substring(0, answerStr.length()-3));
                    assemble_choose.setDiscri(chooseNode.path("discri").asText());
                    taskService.saveAssembleChoose(assemble_choose);
                }
                for(Assemble_Choose temp : assemble_choose_before){
                    if(temp.getTcid() != null){
                        taskService.deleteAssembleChooseByTcide(temp.getTcid());
                    }
                }
                logger.info("修改选择题成功");
            }
        } catch (Exception e){
            logger.error("修改选择题失败 " + e.toString());
        }

        try{
            if(simuPic1 != null && !simuPic1.isEmpty()){
//                如果不是默认图片，则删除
                if(task.getSimuPicPath1() != Simulation.EXAMPLE_SIMULATION_PICPATH){
                    FileUtil.deleteDirectory(task.getSimuPicPath1());
                }
                String simulatePicPath1 = FileUtil.saveStaticUploadFile(simuPic1);
                task.setSimuPicPath1(simulatePicPath1);
            }
        } catch (Exception e){
            task.setSimuPicPath1(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }
        try {
            if(task.getSimuPicPath2() != Simulation.EXAMPLE_SIMULATION_PICPATH){
                FileUtil.deleteDirectory(task.getSimuPicPath2());
            }
            String simulatePicPath2 = FileUtil.saveStaticUploadFile(simuPic2);
            task.setSimuPicPath2(simulatePicPath2);
        } catch (Exception e){
            task.setSimuPicPath2(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }
        taskService.saveTask(task);

        logger.info("创建题目成功");
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


    //下面是含有老师权限过滤部分


    // 获取老师教学的所有课程
    @PostMapping(value = "/getTeacherCourse")
    public ResponseEntity<Result> getTeacherCourse(HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        String username= JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        User user=userService.findByUserName(username);
        List<Course> courseList=manageService.getTeacherCourseList(user.getUid());
        Result result=new Result();
        result.setObject(courseList);
        result.setSuccess(true);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

    // 获取该课程下的所有题目
    @PostMapping(value = "/getTasksByCourseId")
    public ResponseEntity<Result> getTasksByCourseId(Long courseId,HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        List<Task> taskList=taskService.getTasksByCourseId(courseId);
        List<JsonNode> tasks=getMapTasks(taskList);     //把必要的信息打包成json格式返回
        Result result=new Result();
        result.setObject(tasks);
        result.setSuccess(true);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

    //删除题目
    @PostMapping(value = "/deleteTasksByTaskId")
    public ResponseEntity<Result> deleteTasksByTaskId(Long taskId,HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        taskService.deletTaskByTid(taskId);
        return ResultUtil.getResult(new Result(),HttpStatus.OK);
    }

    //将题目公开到题库
    @PostMapping(value = "/makeTaskPublic")
    public ResponseEntity<Result> makeTaskPublic(Long tid,HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest))return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        if (taskService.makeTaskPublic(tid)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("公开到公共题库失败"),HttpStatus.BAD_REQUEST);
    }

    //取消题目公开
    @PostMapping(value = "/cancelTaskPublic")
    public ResponseEntity<Result> cancelTaskPublic(Long tid,HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest))return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        if (taskService.cancelTaskPublic(tid)) return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("公开到公共题库失败"),HttpStatus.BAD_REQUEST);
    }

    //获取公开的题目
    @PostMapping(value = "/getPublicTasks")
    public ResponseEntity<Result> getPublicTasks(HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest))return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        List<Task> taskList=taskService.getPublicTasks();
        List<JsonNode> tasks=getMapTasks(taskList);
        Result result=new Result();
        result.setObject(tasks);
        result.setSuccess(true);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

    //添加公共题库里的题目到本课程中来，采取的策略是将公开的题目复制一份，本地和git上都要复制
    @PostMapping(value = "/addPublicTaskToCourse")
    public ResponseEntity<Result> addPublicTaskToCourse(Long tid,Long courseId,HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)) return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        Task task=taskService.getTaskByTid(tid);
        if (task==null) return ResultUtil.getResult(new Result("该题目id不正确"), HttpStatus.BAD_REQUEST);
        if (task.getIsPublic()!=1) return ResultUtil.getResult(new Result("无法添加非公开题目到此课程"), HttpStatus.BAD_REQUEST);

        Task newTask=new Task(task.getTname(),task.getTdis(),task.getTtype());  //新建一个同样的题目
        newTask.setIsPublic(0);         //新的题目默认不公开
        newTask.setCourseId(courseId);  //绑定到此课程
        taskService.saveTask(newTask);
        logger.info("tid="+newTask.getTid());
        String newTaskId=GitProcess.tidToTaskid(newTask.getTid()); //新建题目的projectid
        TaskModel taskModel=new TaskModel(newTaskId);

        gitProcess=new GitProcess();

        try {
            if (task.getTtype()==0L) { //是Verilog编程题目
                try{
                    logger.info("开始复制Verilog编程题");
                    String finalPath=FileUtil.copyAndUnzipFile(task.getTaskFilePath(),newTask);
                    //            设置taskFile变量
                    FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), finalPath + (OSUtil.isLinux()? "/" : "\\") + "files");
                    //            设置exampleFile变量
                    FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), finalPath + (OSUtil.isLinux()? "/" : "\\") + "example");
    //            获取content.md内容，并修改其中图片路径，
                    task.setTdis(FileUtil.setMdContent(newTask.getTid(), finalPath + (OSUtil.isLinux()? "/" : "\\") + "content.md"));
                    logger.info("处理md文件");
    //            把图片移动到静态文件中
                    FileUtil.moveTaskImg(newTask.getTid(), finalPath + (OSUtil.isLinux()? "/" : "\\") + "images");
                    FileUtil.deleteDirectory(finalPath);
                    logger.info("文件复制成功");
                }catch (Exception e){
                    throw new Exception("Verilog题目文件处理出错");
                }

                try{
//            在gitlab中创建工程
                    gitProcess.gitcreateTask(taskModel);
                    logger.info("创建Git成功");
                } catch (Exception e){
                    e.printStackTrace();
                    throw new Exception("gitlab创建题目失败");
                }
                logger.info("Verilog编程题复制成功");
            }else { //是汇编仿真题目
                logger.info("开始复制汇编仿真题");
                try{
                    String finalPath=FileUtil.copySimulationFile(task.getExampleFilePath(),newTask,"exampleFile");
                    FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), finalPath);  //设置task中的exampleFile
                    finalPath=FileUtil.copySimulationFile(task.getTaskFilePath(),newTask,"taskFile");
                    FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), finalPath);    //设置task中的taskFile
                }catch (Exception e){
                    e.printStackTrace();
                    throw new Exception("文件复制错误");
                }
                logger.info("文件复制成功");
                //        创建git工程
                try{
                    gitProcess.gitcreateTask(taskModel);
                    logger.info("创建git工程成功");
                } catch (Exception e){
                    logger.info("创建git题目失败");
                    e.printStackTrace();
                    throw new Exception("汇编仿真题git工程创建失败");
                }

                //将原题的部分信息直接复制过来
                newTask.setInstrid(task.getInstrid());
                newTask.setSimuid1(task.getSimuid1());
                newTask.setSimuid2(task.getSimuid2());

                //图片是直接保存在static文件夹下的，可以直接共用，不用将图片也复制一遍
                newTask.setSimuPicPath1(task.getSimuPicPath1());
                newTask.setSimuPicPath2(task.getSimuPicPath2());

                //复制选择题
                try{
                    List<Assemble_Choose> assembleChooseList=taskService.getAssebleChoosesByTid(task.getTid());
                    for (Assemble_Choose item:assembleChooseList) {
                        Assemble_Choose assembleChoose=new Assemble_Choose();
                        assembleChoose.setTid(newTask.getTid());
                        assembleChoose.setTpart(item.getTpart());
                        assembleChoose.setAnswers(item.getAnswers());
                        assembleChoose.setDiscri(item.getDiscri());
                        assembleChoose.setOptions(item.getOptions());
                        taskService.saveAssembleChoose(assembleChoose);     //这里必须要新建一个对象用来存储，不能直接用item来进行save
                    }
                    logger.info("选择题复制成功");
                }catch (Exception e){
                    e.printStackTrace();
                    throw  new Exception("选择题复制出错");
                }
                logger.info("汇编仿真题复制成功");
            }
            taskService.saveTask(newTask);
            return ResultUtil.getResult(new Result(),HttpStatus.OK);
        }catch (Exception e){
            taskService.deletTaskByTid(newTask.getTid());
            return ResultUtil.getResult(new Result("操作出错："+e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    //    获取instruct  指令说明书
    @PostMapping(value = "/getInstructFiles")
    public ResponseEntity<Result> getInstructFiles(HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        Result result = new Result();
        List<Instruction> instructions = taskService.getAllInstruction();
        for(Instruction instruction : instructions){
            instruction.setInstrFilePath(PathUtil.toUrlPath(instruction.getInstrFilePath()));
        }
        result.setObject(instructions);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    //    获取仿真器
    @PostMapping(value = "/getSimulators")
    public ResponseEntity<Result> getSimulators(HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
        Result result = new Result();
        result.setObject(taskService.getAllSimulation());
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    //    创建汇编题目
    @PostMapping(value = "/createSimulationTaskofCourse")
    public ResponseEntity<Result> createSimulationTask(Task task, @RequestBody MultipartFile taskFile, MultipartFile exampleFile, MultipartFile simuPic1, MultipartFile simuPic2, String chooseTask,HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
//        获取 gitProscess对象
        gitProcess = new GitProcess();
        task.setTtype(1L);
        if(task.getSimuid1() == null || taskService.getSimulationBySimuid(task.getSimuid1()) == null ||
                task.getSimuid2() == null || taskService.getSimulationBySimuid(task.getSimuid2()) == null ||
                task.getInstrid() == null || taskService.getInstructionByinstrid(task.getInstrid()) == null){
            return ResultUtil.getResult(new Result("仿真器或者指令集选择有误"), HttpStatus.BAD_REQUEST);
        }
//        if(taskFile == null || taskFile.isEmpty()){
//            return ResultUtil.getResult(new Result("未上传taskFile"), HttpStatus.BAD_REQUEST);
//        }
//        if(exampleFile == null || exampleFile.isEmpty()){
//            return ResultUtil.getResult(new Result("未上传exampleFile"), HttpStatus.BAD_REQUEST);
//        }

//        保存题目
        if (task.getIsPublic()==null) task.setIsPublic(0);
        if (task.getCourseId()==null) return ResultUtil.getResult(new Result("课程id为空"), HttpStatus.BAD_REQUEST);
        taskService.saveTask(task);

        logger.info(task.getTid().toString());
        String filePath;
        String task_id = GitProcess.tidToTaskid(task.getTid());
        TaskModel taskModel = new TaskModel(task_id);
//        根据输入方式，选择不同的方式
        try{
//            将taskFile传到 taskFile文件夹中
            filePath = FileUtil.fileUpload(taskFile, task, "taskFile", "code.asm");
//            设置taskFile变量
            FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath);
//            将exampleFile传到exampleFile文件夹中
            filePath = FileUtil.fileUpload(exampleFile, task, "exampleFile", "code.asm");
            FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath);
            logger.info("文件接收成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
//        创建git工程
        try{
            gitProcess.gitcreateTask(taskModel);
            logger.info("创建git工程成功");
        } catch (Exception e){
            logger.info("创建git题目失败");
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
//                    用 ### 进行拼接
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
                    assemble_choose.setTpart(chooseNode.path("partid").asInt());
                    taskService.saveAssembleChoose(assemble_choose);
                }
                logger.info("创建选择题成功");
            }
        } catch (Exception e){
            logger.info("创建选择题失败");
            e.printStackTrace();
            taskService.deletTaskByTid(task.getTid());
        }
        logger.info("开始接受图片");
        try{
//            吧图片放进静态资源文件夹中
            String simulatePicPath1 = FileUtil.saveStaticUploadFile(simuPic1);
            if (simulatePicPath1 != null) {
                logger.info("图片1接受成功" + simuPic1.getOriginalFilename());
                task.setSimuPicPath1(simulatePicPath1);
            }
            else {
                logger.info("无图片1，使用默认图片");
                task.setSimuPicPath1(Simulation.EXAMPLE_SIMULATION_PICPATH);
            }
        } catch (Exception e){
            task.setSimuPicPath1(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }
        try {
            String simulatePicPath2 = FileUtil.saveStaticUploadFile(simuPic2);
            if (simulatePicPath2 != null) {
                logger.info("图片2接受成功" + simuPic1.getOriginalFilename());
                task.setSimuPicPath2(simulatePicPath2);
            }
            else {
                logger.info("无图片2，使用默认图片");
                task.setSimuPicPath2(Simulation.EXAMPLE_SIMULATION_PICPATH);
            }
        } catch (Exception e){
            task.setSimuPicPath2(Simulation.EXAMPLE_SIMULATION_PICPATH);
        }

        taskService.saveTask(task);
        logger.info("创建题目成功");
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }


    //    创建题目
    @PostMapping(value = "/createVerilogTaskOfCourse")
    public ResponseEntity<Result> createVerilogTaskOfCourse(String tname,Long courseId, @RequestBody MultipartFile taskFile,HttpServletRequest httpServletRequest){
        if (IsNotTeacher(httpServletRequest)){
            return ResultUtil.getResult(new Result("权限受限"), HttpStatus.BAD_REQUEST);
        }
//        获取 gitProscess对象
        gitProcess = new GitProcess();
        Task task = new Task(tname, "", 0L);
        task.setIsPublic(0);
        task.setCourseId(courseId);
        taskService.saveTask(task);
        logger.info(task.getTid() + "开始文件接收");
        String task_id = GitProcess.tidToTaskid(task.getTid());
//        创建gitlab工程生成所对应的对象
        TaskModel taskModel = new TaskModel(task_id);

        try{
            String filePath;
//            接收verilog上传的文件
            filePath = FileUtil.fileUpload(taskFile, task, "", "");
            if(filePath == null) {
                return ResultUtil.getResult(new Result("未上传文件"), HttpStatus.BAD_REQUEST);
            }
//            设置taskFile变量
            FileUtil.setTaskModelFiles(taskModel.getTaskFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "files");
            //            设置exampleFile变量

            FileUtil.setTaskModelFiles(taskModel.getExampleFiles(), filePath + (OSUtil.isLinux()? "/" : "\\") + "example");
//            获取content.md内容，并修改其中图片路径，
            task.setTdis(FileUtil.setMdContent(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "content.md"));
            logger.info("处理md文件");
//            把图片移动到静态文件中
            FileUtil.moveTaskImg(task.getTid(), filePath + (OSUtil.isLinux()? "/" : "\\") + "images");
            FileUtil.deleteDirectory(filePath);
            logger.info("文件接收成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("文件接收失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

        try{
//            在gitlab中创建工程
            gitProcess.gitcreateTask(taskModel);
            logger.info("创建Git成功");
        } catch (Exception e){
            taskService.deletTaskByTid(task.getTid());
            e.printStackTrace();
            return ResultUtil.getResult(new Result("创建题目失败" + "  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
        taskService.saveTask(task);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }


    // 批量导入题目
    @PostMapping(value = "/importTasks")
    public ResponseEntity<Result> importTasks(){
        return ResultUtil.getResult(new Result(),HttpStatus.OK);
    }

    //批量导出题目
    @PostMapping(value = "/exportTasks")
    public ResponseEntity<Result> exportTask(@RequestBody JsonNode jsonNode, HttpServletRequest httpServletRequest){
        System.out.println(jsonNode.toString());
        JsonNode taskIdList=jsonNode.findValue("taskId");
        for (int i=0;i<taskIdList.size();i++) {
            Task task=taskService.getTaskByTid(taskIdList.get(i).asLong());;
            if (task==null) {
                logger.info("taskId="+taskIdList.get(i).asLong()+"的题目不存在");
                continue;
            }
            try {
                if (task.getTtype()==0L){

                }
            }catch (Exception e){
//                System.out.println(task);
                logger.info("taskId="+task.getTid());
            }




        }
        String path="000";
        Result result=new Result();
        result.setSuccess(true);
        result.setObject(path);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }
    /**TODO
     *  1.更新Verilog题目出题接口
     *  2.批量导入导出接口
     */


    private boolean IsNotTeacher(HttpServletRequest httpServletRequest){      //判断该用户是否拥有老师权限
        String token=httpServletRequest.getHeader("Authorization");
        String username= JwtUtil.getUsername(token);
        User user=userService.findByUserName(username);
        if (user==null) return true;
        return user.getUtype() != 1;
    }
}
