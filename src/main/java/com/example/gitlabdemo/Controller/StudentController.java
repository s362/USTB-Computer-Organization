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

    @PostMapping("/test")
    public ResponseEntity<Result> testConnect(String test){
        System.out.println(test);
        return ResultUtil.getResult(new Result(), HttpStatus.OK);
    }

    @PostMapping("/getQuestionAndTasks")
    public ResponseEntity<Result> getQuestionAndTasks(HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        List<Question> questions = questionService.getAllQuestion();
        List<QuestionAndTask> questionAndTasks = new LinkedList<>();
        for(int i = 0; i < questions.size(); i++){
            Question question = questions.get(i);
            QuestionAndTask questionAndTask = new QuestionAndTask();
            questionAndTask.setQid(question.getQid());
            questionAndTask.setQname(question.getQname());
            questionAndTask.setUpdatedate(question.getUpdatedate());
            questionAndTask.setCreatedate(question.getCreatedate());
            questionAndTask.setTaskScores(getTaskScores(Long.parseLong(user_id), question.getQid()));
            questionAndTasks.add(questionAndTask);
        }

        Result result = new Result();
        result.setObject(questionAndTasks);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getQuestion")
    public ResponseEntity<Result> getAllQuestion(HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        Result result = new Result();
        result.setObject(questionService.getAllQuestion());
        return ResultUtil.getResult(result, HttpStatus.OK);
    }

    @PostMapping("/getTasks")
    public ResponseEntity<Result> getAllTasks(Long qid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        if (qid == 0l){
            List<TaskScore> taskScores = new LinkedList<>();
            List<Question> questions = questionService.getAllQuestion();
            for(int i = 0; i < questions.size(); i++){
                taskScores.addAll(getTaskScores(Long.parseLong(user_id), questions.get(i).getQid()));
            }
            Result result = new Result();
            result.setObject(taskScores);
            return ResultUtil.getResult(result, HttpStatus.OK);
        } else{
            Result result = new Result();
            result.setObject(getTaskScores(Long.parseLong(user_id), qid));
            return ResultUtil.getResult(result, HttpStatus.OK);
        }
    }

    private List<TaskScore> getTaskScores(Long uid, Long qid){
        List<TaskScore> taskScores = new LinkedList<>();
        List<Task> tasks = taskService.getTaskbyQid(qid);
        for(int i = 0; i < tasks.size(); i++){
            Score score = new Score();
            score.setTask_id("t" + tasks.get(i).getTid());
            score.setUid(uid);
            Score _score = scoreService.findScoreByUserandTaskid(score);
            if (_score == null){
                _score = score;
                scoreService.saveScore(_score);
            }
            TaskScore taskScore = new TaskScore();
            taskScore.setTask_id("t" + tasks.get(i).getTid());
            taskScore.setTname(tasks.get(i).getTname());
            taskScore.setTscore(_score.getTscore());
            taskScore.setUpdatedate(tasks.get(0).getUpdatedate());
            taskScores.add(taskScore);
        }
        return taskScores;
    }




    @PostMapping(value = "/run", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> run_judge(String task_id, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(user_id + "   " + task_id);

        Score score = new Score();
        score.setUid((long)Integer.parseInt(user_id));
        score.setTask_id(task_id);
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

    @PutMapping(value = "/createFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> createFile(String task_id, @RequestBody GitProject modules, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(user_id + "   " + task_id);

        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(task_id, user_id);
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

    @PutMapping(value = "/saveFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> saveFile(String task_id, @RequestBody GitProject modules, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
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

    @DeleteMapping("/deleteFile")
    public ResponseEntity<Result> deleteFile(String task_id, String shortid, HttpServletRequest httpServletRequest){
        String user_id = JwtUtil.getUsername(httpServletRequest.getHeader("Authorization"));
        System.out.println(user_id + "   " + task_id);

        if(shortid.equals(Base64Convert.strConvertBase("README.md")))
            return ResultUtil.getResult(new Result("不能删除README.md文件"), HttpStatus.BAD_REQUEST);
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
