package com.example.gitlabdemo.Controller;

import com.example.gitlabdemo.Model.*;
import com.example.gitlabdemo.Util.Base64Convert;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.JudgeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.tomcat.util.http.ResponseUtil;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {
    GitProcess gitProcess;

    @PostMapping("/test")
    public ResponseEntity<Result> testConnect(String test){
        System.out.println(test);
        return getResult(new Result(), HttpStatus.OK);
    }

    @PostMapping(value = "/run", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> run_judge(@RequestBody User user){
        System.out.println(user.toString());
        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(user);
        if (project_id == null) return getResult(new Result("no project\n" +  user.toString()), HttpStatus.BAD_REQUEST);

        JsonNode jsonObject = JudgeUtil.shell(user);
        if(jsonObject == null){
            return getResult(new Result("error"), HttpStatus.BAD_REQUEST);
        }
        return getResult(new Result(jsonObject), HttpStatus.OK);
    }

    @PostMapping(value = "/getproject", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> getTask_post(@RequestBody User user) {
        return getTask(user);
    }

    @PutMapping(value = "/renameFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> renameFile(User user, String shortid, String title){
        System.out.println(user.toString());
        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(user);
        if (project_id == null) return getResult(new Result("no project\n" +  user.toString()), HttpStatus.BAD_REQUEST);

        RepositoryFile repositoryFile;
        try{
            repositoryFile = gitProcess.getGitLabApi().getRepositoryFileApi().getFile(project_id, Base64Convert.baseConvertStr(shortid), "master");
        } catch (Exception e){
            System.out.println("no such file");
            return getResult(new Result("没有该文件"), HttpStatus.BAD_REQUEST);
        }

        GitFile gitFile = new GitFile();

        gitFile.setShortid(title);
        try{
            gitProcess.isFileExist(project_id, gitFile);
        }catch (Exception e){
            System.out.println("文件存在");
            return getResult(new Result("文件已存在"), HttpStatus.BAD_REQUEST);
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
                return getResult(result, HttpStatus.OK);
            }
        }
        return getResult(new Result("false"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getproject")
    public ResponseEntity<Result> getTask(User user) {
        gitProcess = new GitProcess();
        System.out.println(user.toString());
        GitProject gitProject = new GitProject();
        Integer project_id;
        project_id = gitProcess.getProjectId(user);
        Integer teacher_id;
        teacher_id = gitProcess.getProjectId(user.getTask_id(), "teacher");
        try{
            if (project_id == null) {
                project_id = gitProcess.createProject(user);
                System.out.println("创建工程成功");
            }
        } catch (GitLabApiException e){
            System.out.println(e.toString());
            return getResult(new Result("创建工程失败"), HttpStatus.BAD_REQUEST);
        }

        gitProject.setModules(gitProcess.getRepositoryFiles(project_id));
        gitProject.setTags(new LinkedList<String>());
        gitProject.setDirectories(new LinkedList<GitFolder>());
        gitProject.setId(user.getTask_id());
        gitProject = gitProcess.setTeacherInfo(gitProject, teacher_id);

        try {
            gitProject.setSourceId(gitProcess.getProjectCommiteId(project_id));
        }
        catch (GitLabApiException e){
            System.out.println(e.toString());
        }
        return getResult(new Result(gitProject), HttpStatus.OK);
    }

    @PutMapping(value = "/createFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> createFile(User user, @RequestBody GitProject modules){
        gitProcess = new GitProcess();
        System.out.println(user.toString());
        Integer project_id = gitProcess.getProjectId(user);
        if (project_id == null) return getResult(new Result("no project\n" +  user.toString()), HttpStatus.BAD_REQUEST);

        for(int i = 0; i < modules.getModules().size();i++){
            if(modules.getModules().get(i).getShortid().equals(Base64Convert.strConvertBase("README.md"))) continue;
            GitFile gitFile = new GitFile(modules.getModules().get(i).getShortid(), modules.getModules().get(i).getCode());
            try{
                gitProcess.isFileExist(project_id, gitFile);
            }catch (Exception e){
                return getResult(new Result("false"), HttpStatus.BAD_REQUEST);
            }

            if (gitProcess.gitcreateFile(project_id, gitFile)){
                System.out.println("createsucess");
            }
            else {
                return getResult(new Result("fail"), HttpStatus.BAD_REQUEST);
            }
        }
        return getResult(new Result(), HttpStatus.OK);
    }

    @PutMapping(value = "/saveFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> saveFile(User user, @RequestBody GitProject modules){
        gitProcess = new GitProcess();
        System.out.println(user.toString());
        Integer project_id = gitProcess.getProjectId(user);
        if (project_id == null) return getResult(new Result("no project\n" +  user.toString()), HttpStatus.BAD_REQUEST);

        for(int i = 0; i < modules.getModules().size();i++){
            if(modules.getModules().get(i).getShortid().equals(Base64Convert.strConvertBase("README.md"))) continue;

            GitFile gitFile = new GitFile(modules.getModules().get(i).getShortid(), modules.getModules().get(i).getCode());
            if(gitProcess.gitupdateFile(project_id, gitFile)){
                System.out.println("updatesucss");
            }
            else {
                return getResult(new Result("fail"), HttpStatus.BAD_REQUEST);
            }
        }
        return getResult(new Result(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<Result> deleteFile(User user, String shortid){
        System.out.println(user.toString());
        if(shortid.equals(Base64Convert.strConvertBase("README.md")))
            return getResult(new Result("不能删除README.md文件"), HttpStatus.BAD_REQUEST);
        gitProcess = new GitProcess();
        Integer project_id = gitProcess.getProjectId(user);
        if (project_id == null) return getResult(new Result("no project\n" +  user.toString()), HttpStatus.BAD_REQUEST);

        GitFile gitFile = new GitFile();
        gitFile.setShortid(Base64Convert.baseConvertStr(shortid));
        if (gitProcess.gitdeleteFile(project_id, gitFile)){
            System.out.println("delete success");
            return getResult(new Result(), HttpStatus.OK);
        }
        else {
            return getResult(new Result("delete failure"), HttpStatus.BAD_REQUEST);
        }
    }

    ResponseEntity<Result> getResult(Result result, HttpStatus httpStatus){
        return new ResponseEntity<>(result, httpStatus);
    }
}
