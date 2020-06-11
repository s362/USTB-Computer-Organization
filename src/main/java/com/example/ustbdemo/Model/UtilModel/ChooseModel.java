package com.example.ustbdemo.Model.UtilModel;

import java.util.List;

public class ChooseModel {
    List<String> options;
    Long score;
    Long tcid;
    String discri;

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getTcid() {
        return tcid;
    }

    public void setTcid(Long tcid) {
        this.tcid = tcid;
    }

    public String getDiscri() {
        return discri;
    }

    public void setDiscri(String discri) {
        this.discri = discri;
    }
}
