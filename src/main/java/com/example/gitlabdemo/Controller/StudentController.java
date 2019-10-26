package com.example.gitlabdemo.Controller;

import com.example.gitlabdemo.Model.*;
import com.example.gitlabdemo.Model.DataModel.Question;
import com.example.gitlabdemo.Model.DataModel.Score;
import com.example.gitlabdemo.Model.DataModel.Task;
import com.example.gitlabdemo.Model.GitModel.*;
import com.example.gitlabdemo.Service.QuestionService;
import com.example.gitlabdemo.Service.ScoreService;
import com.example.gitlabdemo.Service.TaskService;
import com.example.gitlabdemo.Shiro.JwtUtil;
import com.example.gitlabdemo.Util.Base64Convert;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.JudgeUtil;
import com.example.gitlabdemo.Util.ResultUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 用于测试
     * @param test 任意字符串
     * @return
     */
    @PostMapping("/test")
    public ResponseEntity<Result> testConnect(String test){
        System.out.println(test);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    /**
     * 得到所有作业及其下的题目列表
     * @param httpServletRequest 请求体
     * @return 作业及题目列表
     */
    @PostMapping("/getQuestionAndTasks")
    public ResponseEntity<Result> getQuestionAndTasks(Long cid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        List<Question> questions = questionService.getAllQuestion(cid);
        List<QuestionModel> questionModels = new LinkedList<>();
        for(int i = 0; i < questions.size(); i++){
            Question question = questions.get(i);
            QuestionModel questionModel = new QuestionModel();
            questionModel.setQid(question.getQid());
            questionModel.setQname(question.getQname());
            questionModel.setBeginDate(question.getBegindate());
            questionModel.setEndDate(question.getEnddate());
            questionModel.setTaskScores(getTaskScores(Long.parseLong(user_id), question.getQid()));
            questionModels.add(questionModel);
        }

        Result result = new Result();
        result.setObject(questionModels);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    /**
     * 得到该用户在cid课程下的所有题目
     * @param cid 课程id
     * @param httpServletRequest 请求体
     * @return
     */
    @PostMapping("/getQuestion")
    public ResponseEntity<Result> getAllQuestion(Long cid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        Result result = new Result();
        // 返回所有题目
        result.setObject(questionService.getAllQuestion(cid));
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    /**
     * 得到一次作业下的题目列表
     * @param qid 作业id
     * @param cid 课程id
     * @param httpServletRequest 请求体
     * @return 题目列表
     */
    @PostMapping("/getTasks")
    public ResponseEntity<Result> getAllTasks(Long qid, Long cid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
//        如果 qid 为0，则返回所有作业及其下的题目列表
        if (qid == 0l){
            List<TaskScore> taskScores = new LinkedList<>();
            List<Question> questions = questionService.getAllQuestion(cid);
            for(int i = 0; i < questions.size(); i++){
                taskScores.addAll(getTaskScores(Long.parseLong(user_id), questions.get(i).getQid()));
            }
            Result result = new Result();
            result.setObject(taskScores);
            return ResultUtil.getResult(result, HttpStatus.OK);
        } else{
//            否则返回当前作业的所有题目
            Result result = new Result();
            result.setObject(getTaskScores(Long.parseLong(user_id), qid));
            return ResultUtil.getResult(result, HttpStatus.OK);
        }
    }


//    得到一次作业下的每个题目的分数

    /**
     *
     * @param uid 学生id
     * @param qid 作业id
     * @return
     */
    private List<TaskScore> getTaskScores(Long uid, Long qid){
        // 作业分数列表
        List<TaskScore> taskScores = new LinkedList<>();
        // 得到并遍历该次作业下的所有题目
        List<Task> tasks = taskService.getTaskbyQid(qid);
        for(int i = 0; i < tasks.size(); i++){
            // 得到该用户每到作业题的分数
            Score score = new Score();
            score.setTid(tasks.get(i).getTid());
            score.setUid(uid);
            Score _score = scoreService.findScoreByUserandTaskid(score);
            // 如果该用户这道题没有做过，则数据库记其为0分；
            if (_score == null){
                _score = score;
                scoreService.saveScore(_score);
            }
            TaskScore taskScore = new TaskScore();
            taskScore.setTid(tasks.get(i).getTid());
            taskScore.setTname(tasks.get(i).getTname());
            taskScore.setTscore(_score.getTscore());
            taskScore.setUpdatedate(tasks.get(0).getUpdatedate());
            taskScores.add(taskScore);
        }
        return taskScores;
    }


    /**
     * 运行代码
     * @param tid 题目id
     * @param httpServletRequest 请求体
     * @return
     */
    @PostMapping(value = "/run", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> run_judge(Long tid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = "t" + tid;
        System.out.println(user_id + "   t" + tid);

        Score score = new Score();
        score.setUid((long)Integer.parseInt(user_id));
        score.setTid(tid);
        Score task_score;
        task_score = scoreService.findScoreByUserandTaskid(score);

        if (task_score == null) {
            int temp = scoreService.saveScore(score);
            if (temp == -1 )return ResultUtil.getResult(new Result("数据库获取失败"), HttpStatus.BAD_REQUEST);
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

    /**
     * 更改文件名
     * @param tid 题目id
     * @param info 更改前后文件名称
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/renameFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> renameFile(Long tid, @RequestBody JsonNode info, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
//        gitlab中的项目名
        String task_id = "t" + tid;
        System.out.println(user_id + "   " + task_id);

        gitProcess = new GitProcess();
        System.out.println(info);
        String shortid = info.path("shortid").asText();
        String title = info.path("title").asText();
        System.out.println(shortid + title);

        Integer project_id = gitProcess.getProjectId(task_id, user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project"), HttpStatus.BAD_REQUEST);

//        获取更改名称前的文件
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

//        删除老文件
        if(gitProcess.gitdeleteFile(project_id, gitFile)){
            gitFile.setShortid(Base64Convert.strConvertBase(title));
            gitFile.setCode(repositoryFile.getContent());
            System.out.println("文件删除成功");
//            创建新文件
            if(gitProcess.gitcreateFile(project_id, gitFile)){
                String new_shortid = Base64Convert.strConvertBase(title);
                Result result = new Result();
                result.setObject(new_shortid);
                return ResultUtil.getResult(result, HttpStatus.OK);
            }
        }
        return ResultUtil.getResult(new Result("false"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 进入ide界面调用的请求，返回这个题目的所有文件信息和题目信息
     * @param tid
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/getproject")
    public ResponseEntity<Result> getTask(Long tid, HttpServletRequest httpServletRequest) {
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = "t" + tid;
        System.out.println(user_id + "   " + task_id);
        gitProcess = new GitProcess();

        GitProject gitProject = new GitProject();
        // 得到学生项目id和老师题目项目id
        Integer project_id;
        project_id = gitProcess.getProjectId(task_id, user_id);
        Integer teacher_id;
        teacher_id = gitProcess.getProjectId(task_id, "teacher");
        try{
            if (project_id == null) {
                project_id = gitProcess.createProject(task_id, user_id);
                System.out.println("创建工程成功");

                try {
                    GitFile gitFile = new GitFile(Base64Convert.strConvertBase("top.v"), "");
                    gitProcess.gitcreateFile(project_id, gitFile);
                    System.out.println("创建学生文件成功");
                } catch (Exception e){
                    System.out.println("创建学生文件失败");
                    System.out.println(e.toString());
                }

            }
        } catch (GitLabApiException e){
            System.out.println(e.toString());
            return ResultUtil.getResult(new Result("创建工程失败  " + e.toString()), HttpStatus.BAD_REQUEST);
        }

//        因为前端要求文件列表不能为空
//        所以如果学生工程目录下没有文件，则创建top.v文件
        List<GitFile> gitFiles = gitProcess.getRepositoryFiles(project_id);
        if(gitFiles.isEmpty()){
            GitFile gitFile = new GitFile(Base64Convert.strConvertBase("top.v"), "");
            gitProcess.gitcreateFile(project_id, gitFile);
            System.out.println("创建学生文件成功");
            gitProject.setModules(gitProcess.getRepositoryFiles(project_id));
        } else{
            gitProject.setModules(gitFiles);
        }

        gitProject.setSourceId(project_id.toString());
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

    /**
     * 创建新文件
     * @param tid 题目id
     * @param modules
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/createFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> createFile(Long tid, @RequestBody GitProject modules, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = "t" + tid;
        System.out.println(user_id + "   " + task_id);

        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(task_id, user_id);
        if (project_id == null) return ResultUtil.getResult(new Result("no project  "), HttpStatus.BAD_REQUEST);

//        循环判断文件是否已经存在。
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

    /**
     * 保存文件
     * @param tid 题目id
     * @param modules
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/saveFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> saveFile(Long tid, @RequestBody GitProject modules, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = "t" + tid;
        System.out.println(user_id + "   " + task_id);

        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(task_id, user_id);
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

    /**
     * 删除文件
     * @param tid
     * @param shortid 文件
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/deleteFile")
    public ResponseEntity<Result> deleteFile(Long tid, String shortid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        String task_id = "t" + tid;
        System.out.println(user_id + "   " + task_id);

//        if(shortid.equals(Base64Convert.strConvertBase("README.md")))
//            return ResultUtil.getResult(new Result("不能删除README.md文件"), HttpStatus.BAD_REQUEST);
        gitProcess = new GitProcess();

        Integer project_id = gitProcess.getProjectId(task_id, user_id);
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
