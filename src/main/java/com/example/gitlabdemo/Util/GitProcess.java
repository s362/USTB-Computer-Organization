package com.example.gitlabdemo.Util;

import com.example.gitlabdemo.Model.GitModel.GitFile;
import com.example.gitlabdemo.Model.GitModel.GitProject;
import com.example.gitlabdemo.Model.GitModel.TaskFile;
import com.example.gitlabdemo.Model.GitModel.TaskModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gitlab4j.api.*;
import org.gitlab4j.api.models.*;

import java.util.*;

public class GitProcess {
    GitLabApi gitLabApi;
//    String hostURL = "http://222.28.41.217:8099";
    String hostURL = "http://202.204.62.155:8099";
//    String hostURL = "http://gitlab.blazarx.com:6300/";
//    String privateToken = "TEvXW8r5fiUZ-6i2V5hn";
//    String hostURL = "http://140.143.62.131:8099/";
    String privateToken = "Y3FS-iYhSGq4A5GwV6Fq";


    public GitProcess(){
        try{
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


    public Integer getProjectId(String task_id, String user_id) {
        Integer project_id;
        try{
//            System.out.println(user);
            project_id = gitLabApi.getProjectApi().getProject(task_id, user_id).getId();
        } catch (GitLabApiException e){
            System.out.println(e.toString());
            System.out.println("no this project");
            project_id = null;
        } catch (Exception e){
            System.out.println(e.toString());
//            e.printStackTrace();
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

            GitFile gitFile = new GitFile(Base64Convert.strConvertBase("README.md"), Base64Convert.baseConvertStr(root.findValue("task_content").asText()));
            gitFile.setId(Base64Convert.strConvertBase(Base64Convert.strConvertBase("README.md")));
            gitFile.setIs_binary(false);
            gitFile.setDirectory_shortid(null);
            gitFile.setSourceId(repositoryFile.getCommitId());
            gitFile.setTitle("README.md");
            gitProject.getModules().add(gitFile);
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

    public boolean gitcreateTask(TaskModel taskModel) {
        Integer project_id;
         String path = taskModel.getTask_id();
         GroupApi groupApi = gitLabApi.getGroupApi();

        try{
            groupApi.getGroup(path);
            System.out.println("题目已存在，开始更新题目");
            try{
                project_id = gitLabApi.getProjectApi().getProject(path, "teacher").getId();
                gitLabApi.getProjectApi().deleteProject(project_id);
                Thread.sleep(2000);
                System.out.println("删除原工程成功");

            } catch (GitLabApiException erro){
                System.out.println("删除源工程失败， 无源工程");
            } catch (Exception e){
                System.out.println(e.toString());
            }
        }
        catch (GitLabApiException e){
            System.out.println("题目不存在，开始新建题目");
            try{
                groupApi.addGroup(path, path);
            } catch (GitLabApiException er){
                System.out.println(er.toString());
                return false;
            }
        }

        try{
            Integer groupId = groupApi.getGroup(path).getId();
            System.out.println(groupId);
            gitLabApi.getProjectApi().createProject(groupId, "teacher");

        }catch (GitLabApiException e){
            System.out.println("创建工程失败");
            System.out.println(e.toString());
            e.printStackTrace();
            return false;
        }

        try{
            project_id = gitLabApi.getProjectApi().getProject(path, "teacher").getId();
            for(TaskFile taskFile : taskModel.getTaskFiles()){
                RepositoryFile repositoryFile = new RepositoryFile();
                if(taskFile.getContent() == null){
                    repositoryFile.setContent("");
                } else {
                    repositoryFile.setContent(taskFile.getContent());
                }

                repositoryFile.setFileName(taskFile.getTitle());
                repositoryFile.setFilePath(taskFile.getTitle());
                gitLabApi.getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");
            }

            RepositoryFile repositoryFile = new RepositoryFile();
            repositoryFile.setFilePath("task.config");
            repositoryFile.setFileName("task.config");

            Map m1 = new HashMap();
            m1.put("task_title", taskModel.getTask_title());
            m1.put("task_content", taskModel.getTask_content());

            ObjectMapper objectMapper = new ObjectMapper();
            String taskInfo = objectMapper.writeValueAsString(m1);
            repositoryFile.setContent(taskInfo);
            gitLabApi.getRepositoryFileApi().createFile(project_id, repositoryFile, "master", "update");
            System.out.println("创建成功");
            return true;

        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("创建文件失败");
            return false;
        }
    }
}
