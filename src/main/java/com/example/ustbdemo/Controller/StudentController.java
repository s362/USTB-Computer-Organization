package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Model.UtilModel.ChooseModel;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Service.QuestionService;
import com.example.ustbdemo.Service.ScoreService;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.*;
import com.example.ustbdemo.Model.GitModel.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    ScoreService scoreService;

    @Autowired
    QuestionService questionService;

    @Autowired
    TaskService taskService;

    GitProcess gitProcess;

//    后台测试类，用于测试后端是否上线
    @PostMapping("/test")
    public ResponseEntity<Result> testConnect(String test){
        System.out.println(test);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

//    获取用户的所有题目和所有作业。
    @PostMapping("/getQuestionAndTasks")
    public ResponseEntity<Result> getQuestionAndTasks(HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        List<Question> questions = questionService.getAllQuestion();

        List<QuestionAndTask> questionAndTasks = new LinkedList<>();
        for(int i = 0; i < questions.size(); i++){
            Question question = questions.get(i);
            QuestionAndTask questionAndTask = new QuestionAndTask(question);
            questionAndTask.setTaskScores(getTaskScores(Long.parseLong(user_id), question.getQid()));
            questionAndTasks.add(questionAndTask);
        }
        Result result = new Result();
        result.setObject(questionAndTasks);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getChooseByTid")
    public ResponseEntity<Result> getChooseByTid(Long tid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));

        List<Assemble_Choose> assemble_chooses = taskService.getAssebleChoosesByTid(tid);
        List<ChooseModel> chooseModels = new LinkedList<>();
        for (Assemble_Choose assemble_choose : assemble_chooses){
            ChooseModel chooseModel = new ChooseModel();
            chooseModel.setTcid(assemble_choose.getTcid());
            chooseModel.setDiscri(assemble_choose.getDiscri());
            chooseModel.setOptions(Arrays.asList(assemble_choose.getOptions().split("###")));
            chooseModels.add(chooseModel);
        }
        chooseModels = getAssembleChooseScores(Long.parseLong(user_id), chooseModels);
        Result result = new Result(chooseModels);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/runAssembleChoose")
    public ResponseEntity<Result> runAssembleChoose(Long tcid, String answer, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        Assemble_Choose assemble_choose = taskService.getAssembleChooseByTid(tcid);
        Assemble_Choose_Score assemble_choose_score = scoreService.findAssembleChooseScoreByUserandTid(Long.parseLong(user_id), tcid);
        if (assemble_choose_score == null){
            assemble_choose_score = new Assemble_Choose_Score();
            assemble_choose_score.setTcid(tcid);
            assemble_choose_score.setUid(Long.parseLong(user_id));
        }
        assemble_choose_score.setUpdatedate(new Date());
        Result result = new Result();
        if(assemble_choose.getAnswers().split("###")[0].equals(answer)){
            assemble_choose_score.setAcscore(100L);
            this.scoreService.saveAssembleChooseScore(assemble_choose_score);
            result.setObject(100L);
            return ResultUtil.getResult(result, HttpStatus.OK);
        } else {
            assemble_choose_score.setAcscore(0L);
            this.scoreService.saveAssembleChooseScore(assemble_choose_score);
            result.setObject(0L);
            return ResultUtil.getResult(result, HttpStatus.OK);
        }
    }

    @PostMapping("/runSimulation")
    public ResponseEntity<Result> runSimulation(Long tid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        try {
            String content = FileUtil.getContent(Simulation.EXAMPLE_SIMULATION_RESULT);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonStr = mapper.readTree(content);
            Result result = new Result();
            result.setObject(jsonStr);
            return ResultUtil.getResult(result, HttpStatus.OK);
        } catch (Exception e){
            return ResultUtil.getResult(new Result(false), HttpStatus.BAD_REQUEST);
        }
    }

//    获取选择题分数
    private  List<ChooseModel> getAssembleChooseScores(Long uid, List<ChooseModel> chooseModels){
        for(ChooseModel chooseModel : chooseModels){
            Assemble_Choose_Score assemble_choose_score = scoreService.findAssembleChooseScoreByUserandTid(uid, chooseModel.getTcid());
            if (assemble_choose_score == null){
                assemble_choose_score = new Assemble_Choose_Score(uid, chooseModel.getTcid(), new Date());
                scoreService.saveAssembleChooseScore(assemble_choose_score);
            }
            chooseModel.setScore(assemble_choose_score.getAcscore());
        }
        return chooseModels;
    }

//    获取所有题目分数
    private List<TaskScore> getTaskScores(Long uid, Long qid){
        List<TaskScore> taskScores = new LinkedList<>();
        List<Task> tasks = taskService.getTaskbyQid(qid);
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

//    进行测评，调用python脚本。调用过程封装在了JudgeUtil中。
    @PostMapping(value = "/run", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> run_judge(String task_id, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(user_id + "   " + task_id + "   开始评测");

        Score score = new Score();
        score.setUid(Long.parseLong(user_id));
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

        JsonNode jsonObject = JudgeUtil.shell(task_id, user_id);

        if(jsonObject == null){
            return ResultUtil.getResult(new Result("error"), HttpStatus.BAD_REQUEST);
        }

        task_score.setTscore(jsonObject.findValue("score").asLong());
        scoreService.saveScore(task_score);
        return ResultUtil.getResult(new Result(jsonObject), HttpStatus.OK);
    }


//    修改IDE文件名称
    @PutMapping(value = "/renameFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> renameFile(String task_id, @RequestBody JsonNode info, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(user_id + "   " + task_id);

        gitProcess = new GitProcess();
        System.out.println(info);
        String shortid = info.path("shortid").asText();
        String title = info.path("title").asText();
        System.out.println(shortid + title);

        Integer project_id = gitProcess.getProjectId(task_id, user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project"), HttpStatus.BAD_REQUEST);

        RepositoryFile repositoryFile;
        try{
            repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(project_id, Base64Convert.baseConvertStr(shortid), "master");
        } catch (Exception e){
            System.out.println("no such file");
            return ResultUtil.getResult(new Result("没有该文件"), HttpStatus.BAD_REQUEST);
        }

        GitFile gitFile = new GitFile();

        gitFile.setShortid(title);
        try{
            gitProcess.isFileExist(project_id, gitFile);
        }catch (Exception e){
            System.out.println("文件存在");
            return ResultUtil.getResult(new Result("文件已存在"), HttpStatus.BAD_REQUEST);
        }

        gitFile.setShortid(Base64Convert.baseConvertStr(shortid));

        if(gitProcess.gitdeleteFile(project_id, gitFile)){
            gitFile.setShortid(Base64Convert.strConvertBase(title));
            gitFile.setCode(repositoryFile.getContent());
            System.out.println("文件删除成功");
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
//    如果是第一次访问，则会自动创建一个新文件
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
            if (project_id == null) {
                project_id = gitProcess.createProject(task_id, user_id);
                System.out.println("创建工程成功");
            }
            if(gitProcess.getRepositoryFiles(project_id).isEmpty()){
                System.out.println("学生文件为空");
                try {
                    List<TreeItem> treeItems = gitProcess.getGitLabApi().getRepositoryApi().getTree(teacher_id, "exampleFile", "master");

                    System.out.println("有exampleFile");
                    if(treeItems.isEmpty()) throw new Exception();
                    TreeItem treeItem = treeItems.get(0);
                    RepositoryFile repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(teacher_id, treeItem.getPath(), "master");
                    repositoryFile.setFilePath("top");
                    repositoryFile.setFileName("top");
                    gitProcess.getGitLabApi().getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");

                } catch (Exception e){
//                        没有 example文件
                    System.out.println("无example文件，创建空的top.v文件");
                    GitFile gitFile = new GitFile(Base64Convert.strConvertBase("top"), "");
                    gitProcess.gitcreateFile(project_id, gitFile);
                }
                System.out.println("创建学生文件成功");
            }
        } catch (GitLabApiException e){
            System.out.println(e.toString());
            return ResultUtil.getResult(new Result("创建工程失败  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

        assembleProject.setTid(tid);
        try {
            assembleProject.setExampleCode(gitProcess.getGitLabApi().getRepositoryFileApi().getFile(project_id,"top","master").getContent());
            assembleProject.setExampleCode(Base64Convert.baseConvertStr(assembleProject.getExampleCode()));
        }catch (Exception e){
//            e.printStackTrace();
            assembleProject.setExampleCode("");
        }
        assembleProject.setSimuPicPath1(PathUtil.toUrlPath(task.getSimuPicPath1()));
        assembleProject.setSimuPicPath2(PathUtil.toUrlPath(task.getSimuPicPath2()));
        Instruction instruction = this.taskService.getInstructionByinstrid(task.getInstrid());
        assembleProject.setInstrPath(PathUtil.toUrlPath(instruction.getInstrFilePath()));
        return ResultUtil.getResult(new Result(assembleProject), HttpStatus.OK);
    }


//    获取题目信息，包括之前写的代码，题目描述
//    如果是第一次访问，则会自动创建一个新文件
    @PostMapping(value = "/getproject")
    public ResponseEntity<Result> getTask(String task_id, HttpServletRequest httpServletRequest) {
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(user_id + "   " + task_id);
        gitProcess = new GitProcess();

        GitProject gitProject = new GitProject();
        Integer project_id;
        project_id = gitProcess.getProjectId(task_id, user_id);
        Integer teacher_id;
        teacher_id = gitProcess.getProjectId(task_id, "teacher");
        try{
            if (project_id == null) {
                project_id = gitProcess.createProject(task_id, user_id);
                System.out.println("创建工程成功");
            }
            /**
             * TODO
             * 如果工程获取后为空
             */
            if(gitProcess.getRepositoryFiles(project_id).isEmpty()){
                System.out.println("学生文件为空");
                try {
                    List<TreeItem> treeItems = gitProcess.getGitLabApi().getRepositoryApi().getTree(teacher_id, "exampleFile", "master");
                    System.out.println("有example文件");
                    if(treeItems.isEmpty()) throw new Exception();
                    for(TreeItem treeItem : treeItems){
                        RepositoryFile repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(teacher_id, treeItem.getPath(), "master");
                        repositoryFile.setFilePath(repositoryFile.getFileName());
                        gitProcess.getGitLabApi().getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");
                    }
                } catch (Exception e){
//                        没有 example文件
                    System.out.println("无example文件，创建空的top.v文件");
                    GitFile gitFile = new GitFile(Base64Convert.strConvertBase("top.v"), "");
                    gitProcess.gitcreateFile(project_id, gitFile);
                }
                System.out.println("创建学生文件成功");
            }
        } catch (GitLabApiException e){
            System.out.println(e.toString());
            return ResultUtil.getResult(new Result("创建工程失败  " + e.toString()), HttpStatus.BAD_REQUEST);
        }
        gitProject.setSourceId(project_id.toString());
        gitProject.setModules(gitProcess.getRepositoryFiles(project_id));
        gitProject.setTags(new LinkedList<String>());
        gitProject.setDirectories(new LinkedList<GitFolder>());
        gitProject.setId(task_id);
        gitProject = gitProcess.setTeacherInfo(gitProject, teacher_id);

        try {
            gitProject.setSourceId(gitProcess.getProjectCommiteId(project_id));
        }
        catch (GitLabApiException e){
            gitProject.setSourceId("first");
            System.out.println(e.toString());
        }
        return ResultUtil.getResult(new Result(gitProject), HttpStatus.OK);
    }

//    IDE创建文件
    @PutMapping(value = "/createFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> createFile(Long tid, @RequestBody GitProject modules, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(user_id + "   " + tid);

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
                System.out.println("createsucess");
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
        System.out.println(user_id + "   " + tid);

        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(GitProcess.tidToTaskid(tid), user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project\n"), HttpStatus.BAD_REQUEST);

        for(int i = 0; i < modules.getModules().size();i++){
            if(modules.getModules().get(i).getShortid().equals(Base64Convert.strConvertBase("README.md"))) continue;

            GitFile gitFile = new GitFile(modules.getModules().get(i).getShortid(), modules.getModules().get(i).getCode());
            if(gitProcess.gitupdateFile(project_id, gitFile)){
                System.out.println("updatesucss");
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
        System.out.println(user_id + "   " + tid);

        if(shortid.equals(Base64Convert.strConvertBase("README.md")))
            return ResultUtil.getResult(new Result("不能删除README.md文件"), HttpStatus.BAD_REQUEST);
        gitProcess = new GitProcess();

        Integer project_id = gitProcess.getProjectId(GitProcess.tidToTaskid(tid), user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project"), HttpStatus.BAD_REQUEST);

        GitFile gitFile = new GitFile();
        gitFile.setShortid(Base64Convert.baseConvertStr(shortid));
        if (gitProcess.gitdeleteFile(project_id, gitFile)){
            System.out.println("delete success");
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        }
        else {
            return ResultUtil.getResult(new Result("delete failure"), HttpStatus.BAD_REQUEST);
        }
    }
}
