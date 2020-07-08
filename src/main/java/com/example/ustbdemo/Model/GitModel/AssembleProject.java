package com.example.ustbdemo.Model.GitModel;

import java.util.List;

public class AssembleProject {
    private Long tid;
    private String tname;
    private String tdis;
    private String exampleCode;
    private String simuPicPath1;
    private String simuPicPath2;
    private String instrPath;
    private Long simuid1;
    private Long simuid2;

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdis() {
        return tdis;
    }

    public void setTdis(String tdis) {
        this.tdis = tdis;
    }

    public Long getSimuid1() {
        return simuid1;
    }

    public void setSimuid1(Long simuid1) {
        this.simuid1 = simuid1;
    }

    public Long getSimuid2() {
        return simuid2;
    }

    public void setSimuid2(Long simuid2) {
        this.simuid2 = simuid2;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getExampleCode() {
        return exampleCode;
    }

    public void setExampleCode(String exampleCode) {
        this.exampleCode = exampleCode;
    }

    public String getSimuPicPath1() {
        return simuPicPath1;
    }

    public void setSimuPicPath1(String simuPicPath1) {
        this.simuPicPath1 = simuPicPath1;
    }

    public String getSimuPicPath2() {
        return simuPicPath2;
    }

    public void setSimuPicPath2(String simuPicPath2) {
        this.simuPicPath2 = simuPicPath2;
    }

    public String getInstrPath() {
        return instrPath;
    }

    public void setInstrPath(String instrPath) {
        this.instrPath = instrPath;
    }
}
