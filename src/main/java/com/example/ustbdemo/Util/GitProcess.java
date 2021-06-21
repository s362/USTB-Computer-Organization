package com.example.ustbdemo.Util;

import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Model.GitModel.GitFile;
import com.example.ustbdemo.Model.GitModel.GitProject;
import com.example.ustbdemo.Model.GitModel.TaskFile;
import com.example.ustbdemo.Model.GitModel.TaskModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jdk.internal.dynalink.beans.StaticClass;
import org.gitlab4j.api.*;
import org.gitlab4j.api.models.*;

import java.util.*;

public class GitProcess {
    GitLabApi gitLabApi;
//    iptables -I INPUT -p tcp --dport 8080 -j ACCEPT
//    String hostURL = "http://202.204.62.155:8099";
    //    String privateToken = "Y3FS-iYhSGq4A5GwV6Fq"; // 202.204.62.155 root
//    String hostURL = "http://123.56.0.67:3016/";
//    String privateToken = "m89F6zsGaXtxFwsD2AMy"; //202.204.62.155 ustbdemo ustbdemo
//    String hostURL ="http://49.232.207.151:8099/";  //测试用
//    String privateToken ="qGsB2feqzx3J_-t-uoDT";


    String hostURL =OSUtil.isLinux()?"http://127.0.0.1:8099/":"http://192.168.17.130:8099/";
    String privateToken =OSUtil.isLinux()?"qGsB2feqzx3J_-t-uoDT":"fVBagapg2jRDeJzRALv6";  //腾讯云系统密钥


//    校内服务器gitlab
//    String hostURL ="http://127.0.0.1:8099/";
//    String privateToken = "xzzjrr9NK9oYc_wqCqxC";

//   String privateToken =OSUtil.isLinux()?"s3txBpf9sGr8hTCFxxm3":"fVBagapg2jRDeJzRALv6";   //cg新系统gitlab秘钥  root ustbdemo

    public GitProcess(){
        try{
            System.out.println(this.hostURL+"  "+this.privateToken);
            this.gitLabApi = new GitLabApi(this.hostURL, this.privateToken);
        } catch (Exception e){
            e.toString();
        }
    }

    public GitLabApi getGitLabApi(){
        return this.gitLabApi;
    }

    public Integer createProject(String task_id, String user_id)throws GitLabApiException{
        Integer project_id;
        Integer groupId = gitLabApi.getGroupApi().getGroup(task_id).getId();
        gitLabApi.getProjectApi().createProject(groupId ,user_id);

        project_id = getProjectId(task_id, user_id);
        return project_id;
    }

    public void deleteGroupByTid(Long tid) throws GitLabApiException{
        Integer groupId = gitLabApi.getGroupApi().getGroup(GitProcess.tidToTaskid(tid)).getId();
        gitLabApi.getGroupApi().deleteGroup(groupId);
    }

    public void deleteProject(String task_id, String projectName) throws GitLabApiException{
        System.out.println(task_id + "  " + projectName);
        Integer projectId = gitLabApi.getProjectApi().getProject(task_id, projectName).getId();
        gitLabApi.getProjectApi().deleteProject(projectId);
    }


    public Integer getProjectId(String task_id, String user_id) {
        Integer project_id;
        System.out.println(task_id + user_id);
        try{
            Project gitProject = gitLabApi.getProjectApi().getProject(task_id, user_id);
            if(gitProject == null){
                System.out.println("no this project");
                throw new GitLabApiException("无工程");
            }
            project_id = gitProject.getId();
        } catch (GitLabApiException e){
            System.out.println(e.toString());
            System.out.println("no this project");
            project_id = null;
        } catch (Exception e){
            System.out.println("未知错误");
            System.out.println(e.toString());
            project_id = null;
        }
        return project_id;
    }

    public GitProject setTeacherInfo(GitProject gitProject, Integer teacher_id){
        try{
            RepositoryFile repositoryFile = gitLabApi.getRepositoryFileApi().getFile(teacher_id, "task.config", "master");
            String content = Base64Convert.baseConvertStr(repositoryFile.getContent());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);
            gitProject.setTitle(root.findValue("task_title").asText());
            gitProject.setAlias(root.findValue("task_title").asText());
            gitProject.setDescription(Base64Convert.baseConvertStr(root.findValue("task_content").asText()));

            return gitProject;

        } catch (GitLabApiException e){
            System.out.println(e.toString()  +  "老师文件获取失败");
            return gitProject;
        } catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return gitProject;
        }
    }

    public String getProjectCommiteId(Integer projectId) throws GitLabApiException{
        return gitLabApi.getRepositoryApi().getBranch(projectId, "master").getCommit().getId();
    }


//    public List<Project> getAllprojects(String hostUrl, String privateToken){
//        this.gitLabApi = new GitLabApi(hostUrl, privateToken);
//        try {
//            return gitLabApi.getProjectApi().getOwnedProjects();
//        }
//        catch (GitLabApiException e){
//            System.out.println(e.toString());
//            return null;
//        }
//    }

//    public File getRepositoryZip(String hostUrl, String privateToken, Integer projectId){
//        this.gitLabApi = new GitLabApi(hostUrl, privateToken);
//        try {
//            File file = this.gitLabApi.getRepositoryApi().getRepositoryArchive(projectId, "master", (File)null);
//            System.out.println(file.getName());
//            return file;
//        }
//        catch (GitLabApiException e){
//            System.out.println(e.toString());
//            return null;
//        }
//    }

    public List<GitFile> getRepositoryFiles(Integer projectId){
        RepositoryApi repositoryApi = this.gitLabApi.getRepositoryApi();
        RepositoryFileApi repositoryFileApi = this.gitLabApi.getRepositoryFileApi();
        List<GitFile> gitFiles = new LinkedList<GitFile>();
        try {
            List<TreeItem> treeItems = repositoryApi.getTree(projectId);
            for(int i = 0; i < treeItems.size();i++){
                RepositoryFile repositoryFile = repositoryFileApi.getFile(projectId, repositoryApi.getTree(projectId).get(i).getPath(), "master");
                GitFile gitFile = new GitFile(Base64Convert.strConvertBase(repositoryFile.getFilePath()), Base64Convert.baseConvertStr(repositoryFile.getContent()));
                gitFile.setId(Base64Convert.strConvertBase(repositoryFile.getFilePath()));
                gitFile.setIs_binary(false);
                gitFile.setDirectory_shortid(null);
                gitFile.setSourceId(repositoryFile.getCommitId());
                gitFile.setTitle(repositoryFile.getFileName());
                gitFiles.add(gitFile);
            }
            return gitFiles;
        }
        catch (GitLabApiException e){
            System.out.println(e.toString() + "  无学生文件");
            return gitFiles;
        }
    }

    public List<GitFile> getRepositoryFiles(Integer projectId,String path){
        RepositoryApi repositoryApi = this.gitLabApi.getRepositoryApi();
        RepositoryFileApi repositoryFileApi = this.gitLabApi.getRepositoryFileApi();
        List<GitFile> gitFiles = new LinkedList<GitFile>();
        try {
            List<TreeItem> treeItems = repositoryApi.getTree(projectId,path,"master");
            for (TreeItem treeItem : treeItems) {
                RepositoryFile repositoryFile = repositoryFileApi.getFile(projectId, treeItem.getPath(), "master");
                GitFile gitFile = new GitFile(Base64Convert.strConvertBase(repositoryFile.getFilePath()), Base64Convert.baseConvertStr(repositoryFile.getContent()));
                gitFile.setId(Base64Convert.strConvertBase(repositoryFile.getFilePath()));
                gitFile.setIs_binary(false);
                gitFile.setDirectory_shortid(null);
                gitFile.setSourceId(repositoryFile.getCommitId());
                gitFile.setTitle(repositoryFile.getFileName());
                gitFiles.add(gitFile);
            }
            return gitFiles;
        }
        catch (GitLabApiException e){
            System.out.println(e.toString() + "  无学生文件");
            return gitFiles;
        }
    }

    public boolean isFileExist(Integer projectId, GitFile gitFile) {
        RepositoryApi repositoryApi = this.gitLabApi.getRepositoryApi();
        List<TreeItem> treeItems;
        try{
            treeItems = repositoryApi.getTree(projectId);
        } catch (Exception e){
            System.out.println("当前文件为空");
            return false;
        }
        for(int i = 0; i < treeItems.size();i++){
            if(treeItems.get(i).getPath().equals(gitFile.getShortid())){
                System.out.println(treeItems.get(i).getPath() + "  " + gitFile.getShortid());
                return true;
            }
        }
        return false;
    }

    public boolean gitupdateFile(Integer projectId, GitFile gitFile){
        try {
            RepositoryFile repositoryFile = this.gitLabApi.getRepositoryFileApi().getFile(projectId, Base64Convert.baseConvertStr(gitFile.getShortid()), "master");
            repositoryFile.setContent(Base64Convert.strConvertBase(gitFile.getCode()));
            this.gitLabApi.getRepositoryFileApi().updateFile(projectId, repositoryFile, "master", "update");
            return true;
        }
        catch (GitLabApiException e){
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean gitcreateFile(Integer projectId, GitFile gitFile){
        try {
            RepositoryFile file = new RepositoryFile();
            file.setFilePath(Base64Convert.baseConvertStr(gitFile.getShortid()));
            System.out.println(file.getFilePath());
            file.setContent(Base64Convert.strConvertBase(gitFile.getCode()));

            RepositoryFile createdFile = this.gitLabApi.getRepositoryFileApi().createFile(projectId, file, "master", "create");
            System.out.println("create success");
            return true;
        }
        catch (GitLabApiException e){
            System.out.println("文件创建失败");
            return false;
        }
    }

    public boolean gitdeleteFile(Integer projectId, GitFile gitFile){
        try {
            this.gitLabApi.getRepositoryFileApi().deleteFile(projectId, gitFile.getShortid(), "master", "deleteFile");
            return true;
        }
        catch (GitLabApiException e){
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean gitcreateTask(TaskModel taskModel) throws Exception{
        String path = taskModel.getTask_id();
        GroupApi groupApi = gitLabApi.getGroupApi();
        Integer groupId;
//        创建group
        try{
            groupId = groupApi.getGroup(path).getId();
            System.out.println("题目已存在 groupid=" + groupId);
        } catch (Exception e){
            groupApi.addGroup(path, path);
//        拿到groupid
            groupId = groupApi.getGroup(path).getId();
            System.out.println("题目不存在，创建group, groupid="+groupId);
        }
//        创建teacher工程
        Integer project_id;
        try{
            gitLabApi.getProjectApi().createProject(groupId, "teacher");

            project_id = gitLabApi.getProjectApi().getProject(path, "teacher").getId();
//      添加老师上传的文件
            createRepositorys(taskModel.getTaskFiles(), "taskFile", project_id);
            createRepositorys(taskModel.getExampleFiles(), "exampleFile", project_id);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("创建git工程失败");
            throw new Exception();
        }

        if(taskModel.getConfigJson() != null){
            createRepository(taskModel.getConfigJson(), taskModel.getConfigJson().getTitle(), project_id);
        }
        return true;
    }



    public void createRepositorys(List<TaskFile> taskFiles, String path, Integer project_id) throws Exception{
        for(TaskFile taskFile : taskFiles){
            RepositoryFile repositoryFile = new RepositoryFile();
            if(taskFile.getContent() == null){
                repositoryFile.setContent("");
            } else {
                repositoryFile.setContent(taskFile.getContent());
            }
            repositoryFile.setFileName(taskFile.getTitle());
            repositoryFile.setFilePath(path + "/" + taskFile.getTitle());
            gitLabApi.getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");
        }
    }

    public void createRepository(TaskFile taskFile, String path, Integer project_id) throws Exception{
        RepositoryFile repositoryFile = new RepositoryFile();
        if(taskFile.getContent() == null){
            repositoryFile.setContent("");
        } else {
            repositoryFile.setContent(taskFile.getContent());
        }
        repositoryFile.setFileName(taskFile.getTitle());
        repositoryFile.setFilePath(path);
        gitLabApi.getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");
    }

    public static String tidToTaskid(Long tid){
        return "s" + tid.toString();
    }
    public static Long taskIdtoTid(String task_id){
        return Long.parseLong(task_id.substring(1));
    }

    public static void sss(){
//        this.gitLabApi.
    }
}
