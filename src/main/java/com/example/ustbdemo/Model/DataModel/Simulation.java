package com.example.ustbdemo.Model.DataModel;
import com.example.ustbdemo.Util.FileUtil;
import com.example.ustbdemo.Util.OSUtil;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.File;

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

    @Column(name = "innerid")
    private Long innerid;

    public static final String EXAMPLE_SIMULATION_PICPATH = OSUtil.isLinux() ?
            FileUtil.STATIC_PATH_LINUX + "exampleSimulationPic.png" : FileUtil.STATIC_PATH_WIN + "exampleSimulationPic.png";
                            ;
    public static final String EXAMPLE_SIMULATION_RESULT = OSUtil.isLinux() ?
            FileUtil.STATIC_PATH_LINUX + "exampleSimuResult.json" : FileUtil.STATIC_PATH_WIN + "exampleSimuResult.json";

    public Simulation(){};

    public Simulation(String simuname, Long innerid){
        this.simuname = simuname;
        this.innerid = innerid;
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