package com.example.ustbdemo.Model.GitModel;

import lombok.ToString;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

@ToString
public class GitFile {
    private String title;
    private String sourceId; // commitid
    private String shortid; // base64加密的路径
    private boolean is_binary; // 是否是二进制文件
    private String directory_shortid; // 目录base64加密路径
    private String id; // 和shortID相同
    private String code; // 代码

    public GitFile(){

    }

    public GitFile(String title){
        this.title = title;
    }

    public GitFile(String shortid, String code){
        this.shortid = shortid;
        this.code = code;
    }

    public GitFile(boolean is_binary,String shortid, String code){
        this.is_binary = is_binary;
        this.shortid = shortid;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
    }

    public boolean isIs_binary() {
        return is_binary;
    }

    public void setIs_binary(boolean is_binary) {
        this.is_binary = is_binary;
    }

    public String getDirectory_shortid() {
        return directory_shortid;
    }

    public void setDirectory_shortid(String directory_shortid) {
        this.directory_shortid = directory_shortid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
