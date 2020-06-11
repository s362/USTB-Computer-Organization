package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {
}
