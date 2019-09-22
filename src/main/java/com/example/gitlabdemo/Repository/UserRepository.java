package com.example.gitlabdemo.Repository;

import com.example.gitlabdemo.Model.DataModel.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
