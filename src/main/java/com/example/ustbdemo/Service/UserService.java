package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("userService")
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        Assert.notNull(userRepository, "userRepository must not be null!");
        this.userRepository = userRepository;
    }

//    public List<User> findAll(){
//        return userRepository.findAll();
//    }


    public User getByUsernameAndPwd(String username, String passwd){
        User user = new User();
        user.setUsername(username);
        user.setPasswd(passwd);
        Example<User> example = Example.of(user);
        try{
            User result = this.userRepository.findOne(example).get();
            return result;
        } catch (Exception e){
            return null;
        }
    }

    public User findByUserName(String userName){
        User user = new User();
        user.setUsername(userName);
        Example<User> example = Example.of(user);
        try{
            User result = this.userRepository.findOne(example).get();
            return result;
        } catch (Exception e){
            return null;
        }
    }

    public Boolean addUser(User user){
        try{
            this.userRepository.save(user);
            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }
}
