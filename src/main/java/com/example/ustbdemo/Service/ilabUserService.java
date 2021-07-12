package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.ilabUser;
import com.example.ustbdemo.Repository.ilabUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static com.example.ustbdemo.Util.Base64Convert.strConvertBase;

@Service("ilabUserService")
public class ilabUserService {
    private final ilabUserRepository ilabuserRepository;
    @Autowired
    public ilabUserService(ilabUserRepository userRepository){
        Assert.notNull(userRepository, "ilabUserRepository must not be null!");
        this.ilabuserRepository = userRepository;
    }
    public Boolean addilabUser (ilabUser user){
        try{
            this.ilabuserRepository.save(user);
            return true;
        } catch (Exception e){
            return null;
        }
    }
//    public ilabUser getilabUser(String username){
//        ilabUser user = new ilabUser();
//        user.setUsername(username);
//        Example<ilabUser> example = Example.of(user);
//        try{
//            List<ilabUser> userList = this.ilabuserRepository.findAll(example);
//            return userList.get(0);
//        } catch (Exception e){
//            return null;
//        }
//    }

    public ilabUser findByUserName(String userName){
        ilabUser user = new ilabUser();
        user.setUsername(userName);
        Example<ilabUser> example = Example.of(user);
        try{
            ilabUser result = this.ilabuserRepository.findOne(example).get();
            return result;
        } catch (Exception e){
            return null;
        }
    }

}
