package com.example.ustbdemo.Model.DataModel;

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

    public Instruction(){};

    public Instruction(String instrname){
        this.instrname = instrname;
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
