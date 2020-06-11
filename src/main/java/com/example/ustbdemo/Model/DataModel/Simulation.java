package com.example.ustbdemo.Model.DataModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "simulation")
@Data
@ToString
public class Simulation {
    @Id
    @GeneratedValue
    private Long simuid;

    @Column(name = "simuname", length = 1000)
    private String simuname;

    public Simulation(){};

    public Simulation(String simuname){
        this.simuname = simuname;
    }

    public Long getSimuid() {
        return simuid;
    }

    public void setSimuid(Long simuid) {
        this.simuid = simuid;
    }

    public String getSimuname() {
        return simuname;
    }

    public void setSimuname(String simuname) {
        this.simuname = simuname;
    }
}