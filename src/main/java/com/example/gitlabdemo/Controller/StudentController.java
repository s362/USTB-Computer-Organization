package com.example.gitlabdemo.Controller;

import com.example.gitlabdemo.Model.*;
import com.example.gitlabdemo.Util.Base64Convert;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.JudgeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;

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
    public ResponseEntity<Result> run_judge(String task_id){
        gitProcess = new GitProcess(task_id);
        Integer project_id = gitProcess.getProjectId();
        if (project_id == null) return getResult(new Result("no project\n" +  GitProcess.user.toString()), HttpStatus.BAD_REQUEST);

        JsonNode jsonObject = JudgeUtil.shell(GitProcess.user);
        if(jsonObject == null){
            return getResult(new Result("error"), HttpStatus.BAD_REQUEST);
        }
        return getResult(new Result(jsonObject), HttpStatus.OK);
    }



    @PutMapping(value = "/renameFile", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> renameFile(String task_id, @RequestBody JsonNode info){
        gitProcess = new GitProcess(task_id);
        System.out.println(info);
        String shortid = info.path("shortid").asText();
        String title = info.path("title").asText();
        System.out.println(shortid + title);
        System.out.println(GitProcess.user.toString());
        Integer project_id = gitProcess.getProjectId();
        if (project_id == null) return getResult(new Result("no project\n" +  GitProcess.user.toString()), HttpStatus.BAD_REQUEST);

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


    @PostMapping(value = "/getproject", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> getTask(String task_id) {
        gitProcess = new GitProcess(task_id);
        System.out.println(GitProcess.user.toString());
        GitProject gitProject = new GitProject();
        Integer project_id;
        project_id = gitProcess.getProjectId();
        Integer teacher_id;
        teacher_id = gitProcess.getProjectId(GitProcess.user.getTask_id(), "teacher");
        try{
            if (project_id == null) {
                project_id = gitProcess.createProject();
                System.out.println("创建工程成功");
            }
        } catch (GitLabApiException e){
            System.out.println(e.toString());
            return getResult(new Result("创建工程失败"), HttpStatus.BAD_REQUEST);
        }
        gitProject.setSourceId(project_id.toString());
        gitProject.setModules(gitProcess.getRepositoryFiles(project_id));
        gitProject.setTags(new LinkedList<String>());
        gitProject.setDirectories(new LinkedList<GitFolder>());
        gitProject.setId(GitProcess.user.getTask_id());
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
    public ResponseEntity<Result> createFile(String task_id, @RequestBody GitProject modules){
        gitProcess = new GitProcess(task_id);
        System.out.println(GitProcess.user.toString());
        Integer project_id = gitProcess.getProjectId();
        if (project_id == null) return getResult(new Result("no project\n" +  GitProcess.user.toString()), HttpStatus.BAD_REQUEST);

        for(int i = 0; i < modules.getModules().size();i++){
            if(modules.getModules().get(i).getShortid().equals(Base64Convert.strConvertBase("README.md"))) continue;
            GitFile gitFile = new GitFile(modules.getModules().get(i).getShortid(), modules.getModules().get(i).getCode());
            if(gitProcess.isFileExist(project_id, gitFile)){
                return getResult(new Result("文件已存在"), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Result> saveFile(String task_id, @RequestBody GitProject modules){
        gitProcess = new GitProcess(task_id);
        System.out.println(GitProcess.user.toString());
        Integer project_id = gitProcess.getProjectId();
        if (project_id == null) return getResult(new Result("no project\n" +  GitProcess.user.toString()), HttpStatus.BAD_REQUEST);

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
    public ResponseEntity<Result> deleteFile(String task_id, String shortid){
        if(shortid.equals(Base64Convert.strConvertBase("README.md")))
            return getResult(new Result("不能删除README.md文件"), HttpStatus.BAD_REQUEST);
        gitProcess = new GitProcess(task_id);
        System.out.println(GitProcess.user.toString());
        Integer project_id = gitProcess.getProjectId();
        if (project_id == null) return getResult(new Result("no project\n" +  GitProcess.user.toString()), HttpStatus.BAD_REQUEST);

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
