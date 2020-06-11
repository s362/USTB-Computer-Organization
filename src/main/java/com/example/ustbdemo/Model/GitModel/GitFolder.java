package com.example.ustbdemo.Model.GitModel;

import lombok.ToString;

@ToString
public class GitFolder {
    private String title;
    private String sourceId;
    private String shortid;
    private String id;
    private String directoryShortid;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirectoryShortid() {
        return directoryShortid;
    }

    public void setDirectoryShortid(String directoryShortid) {
        this.directoryShortid = directoryShortid;
    }
}
