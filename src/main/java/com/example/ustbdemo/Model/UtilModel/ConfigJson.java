package com.example.ustbdemo.Model.UtilModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ConfigJson {
    private Long wavesize;
    private Long wavewidth;
    private Boolean display_flag;
    private Long combinsize;
    private Long simulateTime;
    private Long  finishTime;
    private Boolean showWave;
    private Boolean showResult;
    private Long waveType;

    @Override
    public String toString() {
        String result = "config.json";
        try{result += "wavesize" + "  " + wavesize.toString() + "\n";} catch(NullPointerException e){};
        try{result += "wavewidth" + "  " + wavewidth.toString() + "\n";} catch(NullPointerException e){};
        try{result += "display_flag" + "  " + display_flag.toString() + "\n";} catch(NullPointerException e){};
        try{result += "combinsize" + "  " + combinsize.toString() + "\n";} catch(NullPointerException e){};
        try{result += "simulateTime" + "  " + simulateTime.toString() + "\n";} catch(NullPointerException e){};
        try{result += "finishTime" + "  " + finishTime.toString() + "\n";} catch(NullPointerException e){};
        try{result += "showWave" + "  " + showWave.toString() + "\n";} catch(NullPointerException e){};
        try{result += "showResult" + "  " + showResult.toString() + "\n";} catch(NullPointerException e){};
        try{result += "waveType" + "  " + waveType.toString() + "\n";} catch(NullPointerException e){};
        return result;
    }

    public Boolean isConfig(){
        if(wavesize != null) return true;
        if(wavewidth != null) return true;
        if(display_flag != null) return true;
        if(combinsize != null) return true;
        if(simulateTime != null) return true;
        if(finishTime != null) return true;
        if(showWave != null) return true;
        if(showResult != null) return true;
        if(waveType != null) return true;
        return false;
    }

    public String toJson(){
        Map json = new HashMap();
        if(wavesize != null) json.put("wavesize", wavesize);
        if(wavewidth != null) json.put("wavewidth", wavewidth);
        if(display_flag != null) json.put("display_flag", display_flag);
        if(combinsize != null) json.put("combinsize", combinsize);
        if(simulateTime != null) json.put("simulateTime", simulateTime);
        if(finishTime != null) json.put("finishTime", finishTime);
        if(showWave != null) json.put("showWave", showWave);
        if(showResult != null) json.put("showResult", showResult);
        if(waveType != null) json.put("waveType", waveType);
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JsonNode jsonStr = mapper.readTree(objectMapper.writeValueAsString(json));
            return jsonStr.toString();
        } catch (Exception e){
            System.out.println(e.toString());
            return "";
        }
    }

    public Long getWavesize() {
        return wavesize;
    }

    public void setWavesize(Long wavesize) {
        this.wavesize = wavesize;
    }

    public Long getWavewidth() {
        return wavewidth;
    }

    public void setWavewidth(Long wavewidth) {
        this.wavewidth = wavewidth;
    }

    public Boolean getDisplay_flag() {
        return display_flag;
    }

    public void setDisplay_flag(Boolean display_flag) {
        this.display_flag = display_flag;
    }

    public Long getCombinsize() {
        return combinsize;
    }

    public void setCombinsize(Long combinsize) {
        this.combinsize = combinsize;
    }

    public Long getSimulateTime() {
        return simulateTime;
    }

    public void setSimulateTime(Long simulateTime) {
        this.simulateTime = simulateTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean getShowWave() {
        return showWave;
    }

    public void setShowWave(Boolean showWave) {
        this.showWave = showWave;
    }

    public Boolean getShowResult() {
        return showResult;
    }

    public void setShowResult(Boolean showResult) {
        this.showResult = showResult;
    }

    public Long getWaveType() {
        return waveType;
    }

    public void setWaveType(Long waveType) {
        this.waveType = waveType;
    }
}
