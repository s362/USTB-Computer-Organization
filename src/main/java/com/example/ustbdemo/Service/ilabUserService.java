package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.Score;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Model.DataModel.ilabUser;
import com.example.ustbdemo.Repository.UserRepository;
import com.example.ustbdemo.Repository.ilabUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static com.example.ustbdemo.Util.Base64Convert.strConvertBase;

@Service("ilabUserService")
public class ilabUserService {
    private final ilabUserRepository ilabuserRepository;
    private final UserRepository userRepository ;
    @Autowired
    public ilabUserService(ilabUserRepository ilabuserRepository,UserRepository userRepository){
        Assert.notNull(ilabuserRepository, "ilabUserRepository must not be null!");
        Assert.notNull(userRepository, "UserRepository must not be null!");
        this.ilabuserRepository = ilabuserRepository;
        this.userRepository = userRepository;
    }
    public Boolean addilabUser (ilabUser ilabuser){
        try{
//            User user = new User();
//            user.setUsername(ilabuser.getUsername());
//            Example<User> example = Example.of(user);
//            User userdemo =userRepository.findOne(example).get();
//            ilabuser.setUid(userdemo.getUid());
            this.ilabuserRepository.save(ilabuser);
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

    public boolean deleteById(Long IlabId){
        try {
            this.ilabuserRepository.deleteById(IlabId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
