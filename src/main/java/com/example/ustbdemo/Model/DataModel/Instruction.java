package com.example.ustbdemo.Model.DataModel;

import com.example.ustbdemo.Util.FileUtil;
import com.example.ustbdemo.Util.OSUtil;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "instruction")
@Data
@ToString
public class Instruction {
    @Id
    @GeneratedValue
    private Long instrid;

    @Column(name = "instrname", length = 100)
    private String instrname;

    @Column(name = "instrFilePath", length = 100)
    private String instrFilePath;

    public static final String EXAMPLE_INSTRUCTION_FILEPATH = OSUtil.isLinux() ?
            FileUtil.STATIC_PATH_LINUX + "exampleInstructionFile.doc" : FileUtil.STATIC_PATH_WIN + "exampleInstructionFile.doc";
    ;


    public Instruction(){};

    public Instruction(String instrname){
        this.instrname = instrname;
    }

    public Instruction(String instrname, String instrFilePath){
        this.instrname = instrname;
        this.instrFilePath = instrFilePath;
    }

    public String getInstrFilePath() {
        return instrFilePath;
    }

    public void setInstrFilePath(String instrFilePath) {
        this.instrFilePath = instrFilePath;
    }

    public Long getInstrid() {
        return instrid;
    }

    public void setInstrid(Long instrid) {
        this.instrid = instrid;
    }

    public String getInstrname() {
        return instrname;
    }

    public void setInstrname(String instrname) {
        this.instrname = instrname;
    }
}
