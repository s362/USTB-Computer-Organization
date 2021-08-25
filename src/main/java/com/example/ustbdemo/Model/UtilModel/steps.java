package com.example.ustbdemo.Model.UtilModel;

public class steps {
    private int seq;
    private String title;
    private long startTime;
    private long endTime;
    private int timeUsed;
    private int expectTime;
    private int maxScore;
    private int score;
    private int repeatCount;
    private String evaluation;
    private String scoringModel;


    public steps(int seq, String title, long startTime, long endTime, int expectTime, int maxScore, int score, int repeatCount, String scoringModel) {
        this.seq = seq;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeUsed = (int) ((endTime-startTime)/1000l);
        this.expectTime = expectTime;
        this.maxScore = maxScore;
        this.score = score;
        this.repeatCount = repeatCount;
        if(maxScore == 0){
            this.evaluation = "完成实验";
        } else if((float)score/maxScore >= 0.9){
            this.evaluation = "优";
        } else if((float)score/maxScore >= 0.8){
            this.evaluation = "良";
        } else if((float)score/maxScore >= 0.7){
            this.evaluation = "中";
        } else if((float)score/maxScore >= 0.6){
            this.evaluation = "及格";
        } else {
            this.evaluation = "不及格";
        }

        this.scoringModel = scoringModel;
    }

    public steps() {
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

    public int getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(int expectTime) {
        this.expectTime = expectTime;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getScoringModel() {
        return scoringModel;
    }

    public void setScoringModel(String scoringModel) {
        this.scoringModel = scoringModel;
    }
}
