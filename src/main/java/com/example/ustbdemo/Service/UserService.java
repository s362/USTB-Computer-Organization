package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

import static com.example.ustbdemo.Util.Base64Convert.baseConvertStr;
import static com.example.ustbdemo.Util.Base64Convert.strConvertBase;
import static com.example.ustbdemo.Util.RsaUtil.decode;

@Service("userService")
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        Assert.notNull(userRepository, "userRepository must not be null!");
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }


    public User getByUsernameAndPwd(String username, String passwd){
        User user = new User();
        user.setUsername(username);
//        user.setPasswd(passwd);
        user.setPasswd(strConvertBase(passwd));
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
            if(!user.getUdis().equals("ilab")){
                user.setPasswd(strConvertBase(user.getPasswd()));
            }
            user.setUpdate_at(new Date());
            user.setLock_times(0l);
            this.userRepository.save(user);
            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     * @return ?????????????????????
     */
    public List<User> getTeachers(){
        User user=new User();
        user.setUtype(1L);
        Example<User> userExample=Example.of(user);
        try {
            List<User> userList = this.userRepository.findAll(userExample);
            for(User u:userList){
                u.setPasswd(baseConvertStr(u.getPasswd()));
            }
            return userList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ???????????????
     * @param teacher  ??????????????????
     * @return  ??????????????????
     */
    public boolean addTeacher(User teacher){
        teacher.setUtype(1L);
        try {
            teacher.setPasswd(strConvertBase(teacher.getPasswd()));
            teacher.setUpdate_at(new Date());
            teacher.setLock_times(0l);
            this.userRepository.save(teacher);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ????????????
     * @param teacherId ??????id
     * @return ??????????????????
     */
    public boolean deleteTeacherByTeacherId(Long teacherId){
        try {
            this.userRepository.deleteById(teacherId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ???????????????
     * @param student  ??????????????????
     * @return  ??????????????????
     */
    public boolean addStudent(User student){
        student.setUtype(2L);
        try {
            student.setPasswd(strConvertBase(student.getPasswd()));
            student.setUpdate_at(new Date());
            student.setLock_times(0l);
            this.userRepository.save(student);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     * @return ?????????????????????
     */
    public List<User> getStudent(){
        User user=new User();
        user.setUtype(2L);
        Example<User> userExample=Example.of(user);
        try {
            List<User> userList = this.userRepository.findAll(userExample);
            for(User u:userList){
                u.setPasswd(baseConvertStr(u.getPasswd()));
            }
            return userList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * ???????????????????????????????????????????????????????????????????????????
//     * @return ?????????????????????
//     */
//    public boolean changeStudentPassword(){
//        User user=new User();
//        user.setUtype(1L);
//        Example<User> userExample=Example.of(user);
//        try {
//            List<User> userList = this.userRepository.findAll(userExample);
//            for(User u:userList){
//                u.setPasswd(strConvertBase(u.getPasswd()));
//                this.userRepository.save(u);
//            }
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * ????????????
     * @param studentId ??????id
     * @return ??????????????????
     */
    public boolean deleteStudentId(Long studentId){
        try {
            this.userRepository.deleteById(studentId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int changePwd(String username,String oldPwd,String newPwd){
        User user=findByUserName(username);
        if (user==null) return -1;
        System.out.println("???????????????????????????"+baseConvertStr(user.getPasswd()));

        System.out.println("?????????????????????"+oldPwd);
        if (!baseConvertStr(user.getPasswd()).equals(decode(oldPwd))) return -2;

        user.setPasswd(strConvertBase(decode(newPwd)));
        user.setUpdate_at(new Date());
        try {
            this.userRepository.save(user);
            return 0;
        }catch (Exception e){
            return -3;
        }
    }

    public boolean updateUsr(User user1){
        if (user1==null) return false;
        try {
            this.userRepository.save(user1);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
