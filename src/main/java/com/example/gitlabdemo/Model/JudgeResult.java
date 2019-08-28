package com.example.gitlabdemo.Model;

public class JudgeResult {
    String verdict;
    Integer score;
    String comment;
    String detail;


    public JudgeResult(){
        verdict = "error";
        score = 0;
        comment = "工程不存在";
        detail = "";
    }
    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
