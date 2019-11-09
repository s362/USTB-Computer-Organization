package com.example.gitlabdemo.Service;

import com.example.gitlabdemo.Entity.User;
import com.example.gitlabdemo.Repository.UserRepository;
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

    /**
     * 根据用户名和密码获取用户
     * @param user
     * @return
     */
    public User getByUsernameAndPwd(User user){
        Example<User> example = Example.of(user);
        return this.userRepository.findOne(example).get();
    }

    /**
     * 根据用户名寻找用户
     * @param userName
     * @return
     */
    public User findByUserName(String userName){
        User user = new User();
        user.setUusername(userName);
        Example<User> example = Example.of(user);
        return this.userRepository.findOne(example).get();
    }

    /**
     * 增加用户
     * @param user
     * @return
     */
    public User addUser(User user){
        this.userRepository.save(user);
        return user;
    }
}
