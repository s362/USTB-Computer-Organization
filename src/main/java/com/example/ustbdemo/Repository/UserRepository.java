package com.example.ustbdemo.Repository;

import com.example.ustbdemo.Model.DataModel.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
