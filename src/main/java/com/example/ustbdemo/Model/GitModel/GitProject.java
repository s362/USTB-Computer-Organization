package com.example.ustbdemo.Model.GitModel;

import lombok.ToString;

import java.util.List;

@ToString
public class GitProject {
//    除了加注释的变量，其他变量都是前端冗余的信息，不加前端会报错
    private List<GitFile> modules; // 文件
    private String git;
    private String alias;
    private String sourceId;
    private List<String> tags;
    private String description; // 描述
    private List<String> imgURL;
    private List<GitFolder> directories;
    private String id;
    private String title;

    public List<GitFile> getModules() {
        return modules;
    }

    public void setModules(List<GitFile> modules) {
        this.modules = modules;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImgURL() {
        return imgURL;
    }

    public void setImgURL(List<String> imgURL) {
        this.imgURL = imgURL;
    }

    public List<GitFolder> getDirectories() {
        return directories;
    }

    public void setDirectories(List<GitFolder> directories) {
        this.directories = directories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
