package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Model.DataModel.User;
import com.example.gitlabdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("userService")
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        Assert.notNull(userRepository, "userRepository must not be null!");
        this.userRepository = userRepository;
    }

    public User getByUsernameAndPwd(User user){
        Example<User> example = Example.of(user);

        List<User> list = this.userRepository.findAll(example);
//        System.out.println(this.userRepository.findAll());
//        System.out.println(user);
//        System.out.println(list);
        if(!list.isEmpty()){
            return list.get(0);
        }
        else return null;
    }

    public User findByUserName(String userName){
        User user = new User();
        user.setUusername(userName);
        Example<User> example = Example.of(user);
        List<User> list = this.userRepository.findAll(example);
        if(!list.isEmpty()){
            return list.get(0);
        }
        else return null;
    }

    public int addUser(User user){
        try{
            this.userRepository.save(user);
//            this.userRepository.flush();
            return 0;
        } catch (Exception e){
            System.out.println(e.toString());
            return 1;
        }
    }
}
