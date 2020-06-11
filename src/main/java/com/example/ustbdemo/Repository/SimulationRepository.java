package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {
}
