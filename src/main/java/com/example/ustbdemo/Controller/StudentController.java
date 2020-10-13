package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Model.UtilModel.ChooseModel;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Service.QuestionService;
import com.example.ustbdemo.Service.ScoreService;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Service.UserService;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.*;
import com.example.ustbdemo.Model.GitModel.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {
    public static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    ScoreService scoreService;

    @Autowired
    QuestionService questionService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    GitProcess gitProcess;


    //对学生的工程信息进行暂时的一个保存
    @PostMapping("/temporarilySave")
    public ResponseEntity<Result> temporarilySave(@RequestBody Stage stage,HttpServletRequest httpServletRequest){
        String username=JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        logger.info(stage.toString());
        User user = userService.findByUserName(username);
        stage.setUid(user.getUid());
        //查询该题有没有过暂存信息
        Stage finalStage=taskService.findTemporaryData(stage.getUid(),stage.getTid());
        if (finalStage!=null) {
            //若有，则直接覆盖掉原来的信息
            stage.setStageId(finalStage.getStageId());
        }
        try {
            taskService.saveTemporaryData(stage);
            Result result=new Result();
            result.setSuccess(true);
            result.setObject(getGradeOfTask(user.getUid(),stage.getTid()));
            generateGradeCSV(username,stage.getUid(),stage.getTid());  //每次保存/提交的时候生成csv文件
            return ResultUtil.getResult(result,HttpStatus.OK);
        }catch (Exception e){
            Result result=new Result();
            result.setMessage("数据保存失败");
            return ResultUtil.getResult(result,HttpStatus.BAD_REQUEST);
        }
    }

    //对学生的暂存信息进行查询
    @PostMapping("/findTemporaryData")
    public ResponseEntity<Result> findTemporaryData(Long tid,HttpServletRequest httpServletRequest){
        String username=JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        logger.info("tid= "+tid);
        User user = userService.findByUserName(username);
        Stage stage=taskService.findTemporaryData(user.getUid(),tid);
        Result result=new Result();
        result.setObject(stage);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }



//    获取用户的所有题目和所有作业。
    @PostMapping("/getQuestionAndTasks")
    public ResponseEntity<Result> getQuestionAndTasks(HttpServletRequest httpServletRequest){
//        从header中读取token，并提取用户名，注意是用户名username，不是用户id。
        System.out.println(httpServletRequest.getHeader("Authorization"));
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
//        获取所有作业
        List<Question> questions = questionService.getAllQuestion();

        List<QuestionAndTask> questionAndTasks = new LinkedList<>();
//        对每一个作业，获取其中所有题目
        for(int i = 0; i < questions.size(); i++){
            Question question = questions.get(i);
            QuestionAndTask questionAndTask = new QuestionAndTask(question);
//            设置学生每个题目的分数
            questionAndTask.setTaskScores(getTaskScores(user_id, question.getQid()));
            questionAndTasks.add(questionAndTask);
        }
        Result result = new Result();
        result.setObject(questionAndTasks);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getChooseByTid")
    public ResponseEntity<Result> getChooseByTid(Long tid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        User user = userService.findByUserName(user_id);
//        获取该题目下的所有选择题
        List<Assemble_Choose> assemble_chooses = taskService.getAssebleChoosesByTid(tid);
        List<ChooseModel> chooseModels = new LinkedList<>();
//        变为前端要求格式。因为数据库用字符串来存的选项，这里要断开
        for (Assemble_Choose assemble_choose : assemble_chooses){
            ChooseModel chooseModel = new ChooseModel();
            chooseModel.setTcid(assemble_choose.getTcid());
            chooseModel.setDiscri(assemble_choose.getDiscri());
            chooseModel.setOptions(Arrays.asList(assemble_choose.getOptions().split("###")));
            chooseModel.setPartid(assemble_choose.getTpart());
            chooseModels.add(chooseModel);
        }
//        设置选择题分数
        chooseModels = getAssembleChooseScores(user.getUid(), chooseModels);
        Result result = new Result(chooseModels);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getChooseByTidAndPartId")
    public ResponseEntity<Result> getChooseByTidAndPartId(Long tid, int partId,HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        User user = userService.findByUserName(user_id);
//        获取该题目下的所有选择题
        List<Assemble_Choose> assemble_chooses = taskService.getAssembleChooseByTidAndPartId(tid,partId);
        List<ChooseModel> chooseModels = new LinkedList<>();
//        变为前端要求格式。因为数据库用字符串来存的选项，这里要断开
        for (Assemble_Choose assemble_choose : assemble_chooses){
            ChooseModel chooseModel = new ChooseModel();
            chooseModel.setTcid(assemble_choose.getTcid());
            chooseModel.setDiscri(assemble_choose.getDiscri());
            chooseModel.setOptions(Arrays.asList(assemble_choose.getOptions().split("###")));
            chooseModel.setPartid(assemble_choose.getTpart());
            chooseModels.add(chooseModel);
        }
//        设置选择题分数
        chooseModels = getAssembleChooseScores(user.getUid(), chooseModels);
        Result result = new Result(chooseModels);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/runAssembleChoose")
    public ResponseEntity<Result> runAssembleChoose(Long tcid, String answer, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        Long uid = userService.findByUserName(user_id).getUid();
        Assemble_Choose assemble_choose = taskService.getAssembleChooseByTid(tcid);
//        从数据库中获取分数，如果没有，则新建
        Assemble_Choose_Score assemble_choose_score = scoreService.findAssembleChooseScoreByUidandTid(uid, tcid);
        if (assemble_choose_score == null){
            assemble_choose_score = new Assemble_Choose_Score();
            assemble_choose_score.setTcid(tcid);
            assemble_choose_score.setUid(uid);
            assemble_choose_score.setAcscore(0L);
            assemble_choose_score.setTimes(0L);
        }
        if (assemble_choose_score.getAcscore()<100L) assemble_choose_score.addTimes();  //若该选择之前没有对则将提交次数加一，因为本次提交算入其中
        assemble_choose_score.setUpdatedate(new Date());
        Result result = new Result();
//        将数据库正确答案进行分割，与学生答案比较
        if(assemble_choose.getAnswers().split("###")[0].equals(answer)){
            assemble_choose_score.setAcscore(100L);
            this.scoreService.saveAssembleChooseScore(assemble_choose_score);
            saveSimulationScore(uid,assemble_choose.getTid());
//            generateGradeCSV(user_id,uid,assemble_choose.getTid());
            result.setObject(100L);
            return ResultUtil.getResult(result, HttpStatus.OK);
        } else {
            if(assemble_choose_score.getAcscore()<100L) assemble_choose_score.setAcscore(0L);  //若已经提交正确过，则错误信息不予记录
            this.scoreService.saveAssembleChooseScore(assemble_choose_score);
            saveSimulationScore(uid,assemble_choose.getTid());
//            generateGradeCSV(user_id,uid,assemble_choose.getTid());
            result.setObject(0L);
            return ResultUtil.getResult(result, HttpStatus.OK);
        }
    }

    @PostMapping("/runSimulation")
    public ResponseEntity<Result> runSimulation(Long tid, HttpServletRequest httpServletRequest, @RequestBody JsonNode answerNode){
//        防止answer被截断，将answer放进了body中
        String answer = answerNode.path("answer").asText();
        answer = Base64Convert.baseConvertStr(answer);
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        gitProcess = new GitProcess();

        User user=userService.findByUserName(user_id);
        if (user==null) return ResultUtil.getResult(new Result("用户不存在"), HttpStatus.BAD_REQUEST);
        Assemble_Code_Score assembleCodeScore=scoreService.findAssembleCodeScoreByUidAndTid(user.getUid(),tid);
        if(assembleCodeScore==null){  //如果之前不存在该题目的数据，则新建一个
            assembleCodeScore=new Assemble_Code_Score();
            assembleCodeScore.setUid(user.getUid());
            assembleCodeScore.setTid(tid);
            assembleCodeScore.setTimes(0L);
            assembleCodeScore.setAssembleCodeScore(0L);
        }
//        判断答案是否正确
        try{
            Integer project_id;
//            更新学生gitlab文件
            project_id = gitProcess.getProjectId(GitProcess.tidToTaskid(tid), user_id);
            RepositoryFile repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(project_id, "code.asm", "master");
            repositoryFile.setContent(Base64Convert.strConvertBase(answer));
            gitProcess.getGitLabApi().getRepositoryFileApi().updateFile(project_id, repositoryFile, "master", "udpate");

//            从老师的工程中获取正确answer
            Integer teacher_id;
            teacher_id = gitProcess.getProjectId(GitProcess.tidToTaskid(tid), "teacher");
            RepositoryFile refFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(teacher_id, "taskFile/code.asm", "master");
            String af_answer = answer;
            String rf_answer = Base64Convert.baseConvertStr(refFile.getContent());
//            调用，找到第一个错误的行，如果正确返回-1。
            int wrongIndex = findFirstWrongCode(af_answer, rf_answer);

            if (assembleCodeScore.getAssembleCodeScore()<100L){  //放在此处是为了防止前面拉去gitlab文件方便报错导致提交次数无故增加
                assembleCodeScore.addTimes();
                assembleCodeScore.setUpdatedate(new Date());
            }
            if (wrongIndex == -1){
                assembleCodeScore.setAssembleCodeScore(100L);
                scoreService.saveAssembleCodeScore(assembleCodeScore);
                saveSimulationScore(user.getUid(),tid);  //刷新该实验的成绩信息
//                generateGradeCSV(user_id,user.getUid(),tid); //生成csv文件
                logger.info(user_id + "  答案正确，开始仿真");
            } else {
                if (assembleCodeScore.getAssembleCodeScore()<100) assembleCodeScore.setAssembleCodeScore(0L); //若之前已经提交正确过，则错误信息不予记录
                scoreService.saveAssembleCodeScore(assembleCodeScore);
                saveSimulationScore(user.getUid(),tid);  //刷新该实验的成绩信息
//                generateGradeCSV(user_id,user.getUid(),tid); //生成csv文件
                logger.info(user_id + "  答案错误");
                Result result = new Result();
                Map map = new HashMap();
                map.put("correctIsOk", false);
                map.put("error", wrongIndex);
                result.setObject(map);
                result.setNote(getGradeOfTask(user.getUid(),tid));      //将分数一块传入
                return ResultUtil.getResult(result, HttpStatus.OK);
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getResult(new Result(e.toString()), HttpStatus.BAD_REQUEST);
        }

        Task task = taskService.getTaskByTid(tid);
//        获取仿真器id
        Simulation simulation1 = taskService.getSimulationBySimuid(task.getSimuid1());
        Simulation simulation2 = taskService.getSimulationBySimuid(task.getSimuid2());
        try {
//            去掉answer中的注释
            String _answers[] = answer.split("\n");
            String _answer = "";
            for(int i = 0; i < _answers.length;i++){
                int index=min(_answers[i].indexOf(" "),_answers[i].indexOf("\t"));  //去掉每行代码前的编号
                _answers[i] = _answers[i].substring(index);
                index = _answers[i].indexOf("//");
                if(index != -1) _answers[i] = _answers[i].substring(0, index);
                _answer += _answers[i] + "\n";
            }
            logger.info(_answer);
//            调用评测docker
            Map resultValue = JudgeUtil.simulationOut(Base64Convert.strConvertBase(_answer), simulation1.getInnerid(), simulation2.getInnerid());
            resultValue.put("correctIsOk", true);
            resultValue.put("error", -1);
            Result result = new Result();
            result.setObject(resultValue);
            result.setNote(getGradeOfTask(user.getUid(),tid));  //将分数一块传入
            return ResultUtil.getResult(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getResult(new Result(e.toString(), false), HttpStatus.BAD_REQUEST);
        }
    }

    //  进行测评，调用python脚本。调用过程封装在了JudgeUtil中。
    @PostMapping(value = "/run", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> run_judge(Long tid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        User user = userService.findByUserName(user_id);
        String task_id = GitProcess.tidToTaskid(tid);
        logger.info(user_id + "   " + task_id + "   开始评测");

        Score score = new Score();

        score.setUid(user.getUid());
        score.setTid(GitProcess.taskIdtoTid(task_id));
        Score task_score;
        task_score = scoreService.findScoreByUserandTid(score.getUid(), score.getTid());

        if (task_score == null) {
            scoreService.saveScore(score);
            task_score = score;
        }

        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(task_id, user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project"), HttpStatus.BAD_REQUEST);
//        运行评测docker
        JsonNode jsonObject = JudgeUtil.shell(task_id, user_id);

        if(jsonObject == null){
            return ResultUtil.getResult(new Result("error"), HttpStatus.BAD_REQUEST);
        }

        task_score.setTscore(jsonObject.findValue("score").asLong());
//        更新分数
        scoreService.saveScore(task_score);
        generateGradeCSV(user_id,task_score.getUid(),task_score.getTid());
        return ResultUtil.getResult(new Result(jsonObject), HttpStatus.OK);
    }

//    修改IDE文件名称，删掉之前的文件，以新文件名创建新文件，ide相关，最好别改
    @PutMapping(value = "/renameFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> renameFile(Long tid, @RequestBody JsonNode info, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = GitProcess.tidToTaskid(tid);
        logger.info(user_id + "   " + task_id);

        gitProcess = new GitProcess();
        logger.info(info.toString());
        String shortid = info.path("shortid").asText();
        String title = info.path("title").asText();
        logger.info(shortid + title);

        Integer project_id = gitProcess.getProjectId(task_id, user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project"), HttpStatus.BAD_REQUEST);

        RepositoryFile repositoryFile;
        try{
            repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(project_id, Base64Convert.baseConvertStr(shortid), "master");
        } catch (Exception e){
            logger.info("no such file");
            return ResultUtil.getResult(new Result("没有该文件"), HttpStatus.BAD_REQUEST);
        }

        GitFile gitFile = new GitFile();

        gitFile.setShortid(title);
        try{
            gitProcess.isFileExist(project_id, gitFile);
        }catch (Exception e){
            logger.info("文件存在");
            return ResultUtil.getResult(new Result("文件已存在"), HttpStatus.BAD_REQUEST);
        }

        gitFile.setShortid(Base64Convert.baseConvertStr(shortid));

        if(gitProcess.gitdeleteFile(project_id, gitFile)){
            gitFile.setShortid(Base64Convert.strConvertBase(title));
            gitFile.setCode(repositoryFile.getContent());
            logger.info("文件删除成功");
            if(gitProcess.gitcreateFile(project_id, gitFile)){
                String new_shortid = Base64Convert.strConvertBase(title);
                Result result = new Result();
                result.setObject(new_shortid);
                return ResultUtil.getResult(result, HttpStatus.OK);
            }
        }
        return ResultUtil.getResult(new Result("false"), HttpStatus.BAD_REQUEST);
    }

    //    获取题目信息，包括之前写的代码，题目描述
//    如果是第一次访问，则自动给学生创建一个新工程，并老师工程中的样例文件拷贝到学生工程中
    @PostMapping(value = "/getAssembleTask")
    public ResponseEntity<Result> getAssembleTask(Long tid, HttpServletRequest httpServletRequest) {
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = GitProcess.tidToTaskid(tid);
        Task task = taskService.getTaskByTid(tid);
        if(task.getTtype() != 1L) return ResultUtil.getResult(new Result("不是汇编题目"), HttpStatus.BAD_REQUEST);

        gitProcess = new GitProcess();
        Integer project_id;
        project_id = gitProcess.getProjectId(task_id, user_id);
        Integer teacher_id;
        teacher_id = gitProcess.getProjectId(task_id, "teacher");
        AssembleProject assembleProject = new AssembleProject();
        try{
//            如果之前没建过学生工程，则创建一个
            if (project_id == null) {
                project_id = gitProcess.createProject(task_id, user_id);
                logger.info("创建工程成功");
            }
//            如果学生工程是空的，则把老师的样例文件拷贝过来
            if(gitProcess.getRepositoryFiles(project_id).isEmpty()){
                logger.info("学生文件为空");
                try {
                    List<TreeItem> treeItems = gitProcess.getGitLabApi().getRepositoryApi().getTree(teacher_id, "exampleFile", "master");
                    logger.info("有exampleFile");
                    if(treeItems.isEmpty()) throw new Exception();
                    TreeItem treeItem = treeItems.get(0);
                    RepositoryFile repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(teacher_id, treeItem.getPath(), "master");
                    repositoryFile.setFilePath("code.asm");
                    repositoryFile.setFileName("code.asm");
                    gitProcess.getGitLabApi().getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");

                } catch (Exception e){
//                        没有 example文件
                    logger.info("无example文件，创建空的code.asm文件");
                    GitFile gitFile = new GitFile(Base64Convert.strConvertBase("code.asm"), "");
                    gitProcess.gitcreateFile(project_id, gitFile);
                }
                logger.info("创建学生文件成功");
            }
        } catch (GitLabApiException e){
            logger.info(e.toString());
            return ResultUtil.getResult(new Result("创建工程失败  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

        assembleProject.setTid(tid);
        try {
            assembleProject.setExampleCode(gitProcess.getGitLabApi().getRepositoryFileApi().getFile(project_id,"code.asm","master").getContent());
            assembleProject.setExampleCode(Base64Convert.baseConvertStr(assembleProject.getExampleCode()));
        }catch (Exception e){
            assembleProject.setExampleCode("");
        }
//        返回前端要求格式的对象
        assembleProject.setSimuPicPath1(PathUtil.toUrlPath(task.getSimuPicPath1()));
        assembleProject.setSimuPicPath2(PathUtil.toUrlPath(task.getSimuPicPath2()));
        Instruction instruction = this.taskService.getInstructionByinstrid(task.getInstrid());
        assembleProject.setInstrPath(PathUtil.toUrlPath(instruction.getInstrFilePath()));
        assembleProject.setSimuid1(task.getSimuid1());
        assembleProject.setSimuid2(task.getSimuid2());
        assembleProject.setTname(task.getTname());
        assembleProject.setTdis(task.getTdis());
        return ResultUtil.getResult(new Result(assembleProject), HttpStatus.OK);
    }

    //    获取题目信息，包括之前写的代码，题目描述
//    如果是第一次访问，则会自动创建一个新文件
//    ide相关，最好别改
    @PostMapping(value = "/getproject")
    public ResponseEntity<Result> getTask(Long tid, HttpServletRequest httpServletRequest) {
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = GitProcess.tidToTaskid(tid);
        logger.info(user_id + "   " + task_id);
        gitProcess = new GitProcess();
        Task task = taskService.getTaskByTid(tid);

        GitProject gitProject = new GitProject();
        Integer project_id;
        project_id = gitProcess.getProjectId(task_id, user_id);
        Integer teacher_id;
        teacher_id = gitProcess.getProjectId(task_id, "teacher");
        try{
            if (project_id == null) {
                project_id = gitProcess.createProject(task_id, user_id);
                logger.info("创建工程成功");
            }
            if(gitProcess.getRepositoryFiles(project_id).isEmpty()){
                logger.info("学生文件为空");
                try {
                    List<TreeItem> treeItems = gitProcess.getGitLabApi().getRepositoryApi().getTree(teacher_id, "exampleFile", "master");
                    logger.info("有example文件");
                    if(treeItems.isEmpty()) throw new Exception();
                    for(TreeItem treeItem : treeItems){
                        RepositoryFile repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(teacher_id, treeItem.getPath(), "master");
                        repositoryFile.setFilePath(repositoryFile.getFileName());
                        gitProcess.getGitLabApi().getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");
                    }
                } catch (Exception e){
//                        没有 example文件
                    logger.info("无example文件，创建空的top.v文件");
                    GitFile gitFile = new GitFile(Base64Convert.strConvertBase("top.v"), "");
                    gitProcess.gitcreateFile(project_id, gitFile);
                }
                logger.info("创建学生文件成功");
            }
        } catch (GitLabApiException e){
            logger.info(e.toString());
            return ResultUtil.getResult(new Result("创建工程失败  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
        gitProject.setSourceId(project_id.toString());
        gitProject.setModules(gitProcess.getRepositoryFiles(project_id));
        gitProject.setTags(new LinkedList<String>());
        gitProject.setDirectories(new LinkedList<GitFolder>());
        gitProject.setId(GitProcess.taskIdtoTid(task_id).toString());
//        gitProject = gitProcess.setTeacherInfo(gitProject, teacher_id);
        gitProject.setDescription(task.getTdis());
        gitProject.setAlias(task.getTname());
        gitProject.setTitle(task.getTname());
        try {
            gitProject.setSourceId(gitProcess.getProjectCommiteId(project_id));
        }
        catch (GitLabApiException e){
            gitProject.setSourceId("first");
            logger.info(e.toString());
        }
        return ResultUtil.getResult(new Result(gitProject), HttpStatus.OK);
    }

    //    获取所有作业
    @PostMapping("/getQuestions")
    public ResponseEntity<Result> getQuestions(HttpServletRequest httpServletRequest){
        List<Question> questions = questionService.getAllQuestion();
        Result result = new Result();
        result.setObject(questions);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getTtype")
    public ResponseEntity<Result> getTtype(Long tid,HttpServletRequest httpServletRequest){
        Task task=taskService.getTaskByTid(tid);
        Result result=new Result();
        result.setObject(task);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

//    IDE创建文件
    @PutMapping(value = "/createFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> createFile(Long tid, @RequestBody GitProject modules, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        logger.info(user_id + "   " + tid);

        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(GitProcess.tidToTaskid(tid), user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project  "), HttpStatus.BAD_REQUEST);

        for(int i = 0; i < modules.getModules().size();i++){
            if(modules.getModules().get(i).getShortid().equals(Base64Convert.strConvertBase("README.md"))) continue;
            GitFile gitFile = new GitFile(modules.getModules().get(i).getShortid(), modules.getModules().get(i).getCode());
            if(gitProcess.isFileExist(project_id, gitFile)){
                return ResultUtil.getResult(new Result("文件已存在"), HttpStatus.BAD_REQUEST);
            }

            if (gitProcess.gitcreateFile(project_id, gitFile)){
                logger.info("createsucess");
            }
            else {
                return ResultUtil.getResult(new Result("fail"), HttpStatus.BAD_REQUEST);
            }
        }
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

//    IDE保存文件
    @PutMapping(value = "/saveFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> saveFile(Long tid, @RequestBody GitProject modules, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        logger.info(user_id + "   " + tid);

        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(GitProcess.tidToTaskid(tid), user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project\n"), HttpStatus.BAD_REQUEST);

        for(int i = 0; i < modules.getModules().size();i++){
            if(modules.getModules().get(i).getShortid().equals(Base64Convert.strConvertBase("README.md"))) continue;

            GitFile gitFile = new GitFile(modules.getModules().get(i).getShortid(), modules.getModules().get(i).getCode());
            if(gitProcess.gitupdateFile(project_id, gitFile)){
                logger.info("updatesucss");
            }
            else {
                return ResultUtil.getResult(new Result("fail"), HttpStatus.BAD_REQUEST);
            }
        }
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

//    IDE删除文件，因为前端限制必须有一个文件存在，所以如果删除了最后一个文件，则会自动创建一个新文件。
    @DeleteMapping("/deleteFile")
    public ResponseEntity<Result> deleteFile(Long tid, String shortid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        logger.info(user_id + "   " + tid);

        if(shortid.equals(Base64Convert.strConvertBase("README.md")))
            return ResultUtil.getResult(new Result("不能删除README.md文件"), HttpStatus.BAD_REQUEST);
        gitProcess = new GitProcess();

        Integer project_id = gitProcess.getProjectId(GitProcess.tidToTaskid(tid), user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project"), HttpStatus.BAD_REQUEST);

        GitFile gitFile = new GitFile();
        gitFile.setShortid(Base64Convert.baseConvertStr(shortid));
        if (gitProcess.gitdeleteFile(project_id, gitFile)){
            logger.info("delete success");
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        }
        else {
            return ResultUtil.getResult(new Result("delete failure"), HttpStatus.BAD_REQUEST);
        }
    }


    //获取成绩信息
    @PostMapping(value = "/getGrade")
    public ResponseEntity<Result> getGrade(Long tid,HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        User user=userService.findByUserName(user_id);
        Result result=new Result();
        result.setObject(getGradeOfTask(user.getUid(),tid));
        result.setSuccess(true);
        return ResultUtil.getResult(result,HttpStatus.OK);
    }

    @PostMapping(value = "/initGrade")
    public ResponseEntity<Result> initGrade(Long tid,HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        User user=userService.findByUserName(user_id);
        Task task=taskService.getTaskByTid(tid);
        if (task==null) return ResultUtil.getResult(new Result("该题目不存在"),HttpStatus.BAD_REQUEST);
        if (scoreService.deleteScore(user.getUid(),task.getTid(),task.getTtype()))
            return ResultUtil.getResult(new Result(),HttpStatus.OK);
        else return ResultUtil.getResult(new Result("初始化出错"),HttpStatus.BAD_REQUEST);
    }

    //    获取选择题分数
    private  List<ChooseModel> getAssembleChooseScores(Long uid, List<ChooseModel> chooseModels){
        for(ChooseModel chooseModel : chooseModels){
            Assemble_Choose_Score assemble_choose_score = scoreService.findAssembleChooseScoreByUidandTid(uid, chooseModel.getTcid());
            if (assemble_choose_score == null){
                assemble_choose_score = new Assemble_Choose_Score(uid, chooseModel.getTcid(), new Date());
                scoreService.saveAssembleChooseScore(assemble_choose_score);
            }
            chooseModel.setScore(assemble_choose_score.getAcscore());
        }
        return chooseModels;
    }

    //    获取所有题目分数
    private List<TaskScore> getTaskScores(String user_id, Long qid){
        List<TaskScore> taskScores = new LinkedList<>();
        List<Task> tasks = taskService.getTaskbyQid(qid);
        User user = userService.findByUserName(user_id);
        Long uid = user.getUid();
        for(int i = 0; i < tasks.size(); i++){
            Score _score = scoreService.findScoreByUserandTid(uid, tasks.get(i).getTid());
            if (_score == null){
                _score = new Score(uid, tasks.get(i).getTid(), new Date());
                scoreService.saveScore(_score);
            }
            TaskScore taskScore = new TaskScore(tasks.get(i), _score);
            taskScores.add(taskScore);
        }
        return taskScores;
    }



    private int findFirstWrongCode(String answer, String ref_answer){
        String answers[] = answer.split("\n");
        String ref_answers[] = ref_answer.split("\n");
        for(int i = 0; i < answers.length && i < ref_answers.length; i++){
            //对学生代码进行处理
            int index = answers[i].indexOf("//");       //去掉每行代码后的注释
            if(index != -1) answers[i] = answers[i].substring(0, index);
            index=min(answers[i].indexOf(" "),answers[i].indexOf("\t"));  //去掉每行代码前的编号
            if(index != -1) answers[i]=answers[i].substring(index);
            answers[i] = answers[i].replace("\t", "").replace(" ", "");
            //对老师正确代码进行处理
            index = ref_answers[i].indexOf("//");
            if(index != -1) ref_answers[i] = ref_answers[i].substring(0, index);
            index = min(ref_answers[i].indexOf(" "),ref_answers[i].indexOf("\t"));
            if(index != -1) ref_answers[i] = ref_answers[i].substring(index);
            ref_answers[i] = ref_answers[i].replace("\t", "").replace(" ", "");

            if(!answers[i].equals(ref_answers[i])){
                return i;
            }
        }
        if(answers.length == ref_answers.length) return  -1;
        else return answers.length;
    }

    private Integer min(Integer a,Integer b){
        if (a<b) return a;
        return b;
    }

    private Long max(Long a,Long b){
        if (a>b) return a;
        return b;
    }

    //计算并保存汇编仿真实验题的分数
    private void saveSimulationScore(Long uid,Long tid) {
        List<Assemble_Choose> assembleChooseList=taskService.getAssebleChoosesByTid(tid); //取出该实验题对应的所有选择题
        int number=assembleChooseList.size();
        Long grade;

        Assemble_Code_Score assembleCodeScore=scoreService.findAssembleCodeScoreByUidAndTid(uid,tid);
        Long codeGrade=0L;
        if (assembleCodeScore!=null&&assembleCodeScore.getAssembleCodeScore()==100L){
            codeGrade=max(100-10*(assembleCodeScore.getTimes()-1),0L);
        }

        Long chooseGrade=0L;
        for (Assemble_Choose assembleChoose: assembleChooseList){
            Assemble_Choose_Score assembleChooseScore = scoreService.findAssembleChooseScoreByUidandTid(uid,assembleChoose.getTcid());
            if (assembleChooseScore == null||assembleChooseScore.getAcscore()==0L) continue;
            chooseGrade=chooseGrade+max(assembleChooseScore.getAcscore()-25*(assembleChooseScore.getTimes()-1),0L);
        }
        if(number==0) chooseGrade=0L;
        else chooseGrade=chooseGrade/number;

        grade=(codeGrade+chooseGrade*2)/3;//代码和选择的占分比例是1：2

        Score score = new Score();
        score.setUid(uid);
        score.setTid(tid);
        Score task_score;
        task_score = scoreService.findScoreByUserandTid(score.getUid(), score.getTid());
        if (task_score==null){
            scoreService.saveScore(score);
            task_score=score;
        }
        task_score.setTscore(grade);
        task_score.setUpdatedate(new Date());
        scoreService.saveScore(task_score);
    }


    /**
     * 将当前用户的当前实验题的得分信息写入csv文件，用于与CG对接
     * @param user_id  用户名
     * @param uid  用户的主键
     * @param tid  题目的id
     */
    private void generateGradeCSV(String user_id,Long uid,Long tid){
        Score score=scoreService.findScoreByUserandTid(uid,tid);
        int grade;
        if (score==null) grade=0;
        else grade=score.getTscore().intValue();
        FileUtil.saveCSVFile(user_id,tid,grade);
    }

    //获取题目对应的分数
    private Long getGradeOfTask(Long uid,Long tid){
        Score score=scoreService.findScoreByUserandTid(uid,tid);
        if (score==null) return null;
        return score.getTscore();
    }
}
