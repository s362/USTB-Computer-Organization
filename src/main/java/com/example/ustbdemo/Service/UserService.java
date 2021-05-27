package com.example.ustbdemo.Service;

import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static com.example.ustbdemo.Util.Base64Convert.baseConvertStr;
import static com.example.ustbdemo.Util.Base64Convert.strConvertBase;

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
            user.setPasswd(strConvertBase(user.getPasswd()));
            this.userRepository.save(user);
            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     * 得到所有的老师的信息，包括密码，只有管理员有权访问
     * @return 老师的信息列表
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
     * 新建老师，
     * @param teacher  老师个人信息
     * @return  新建是否成功
     */
    public boolean addTeacher(User teacher){
        teacher.setUtype(1L);
        try {
            teacher.setPasswd(strConvertBase(teacher.getPasswd()));
            this.userRepository.save(teacher);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除老师
     * @param teacherId 老师id
     * @return 删除是否成功
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
     * 新建学生，
     * @param student  学生个人信息
     * @return  新建是否成功
     */
    public boolean addStudent(User student){
        student.setUtype(2L);
        try {
            student.setPasswd(strConvertBase(student.getPasswd()));
            this.userRepository.save(student);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 得到所有的学生的信息，包括密码，只有管理员有权访问
     * @return 学生的信息列表
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

    /**
     * 修改数据库中学生用户的密码存储，只有管理员有权访问
     * @return 学生的信息列表
     */
    public boolean changeStudentPassword(){
        User user=new User();
        user.setUtype(0L);
        Example<User> userExample=Example.of(user);
        try {
            List<User> userList = this.userRepository.findAll(userExample);
            for(User u:userList){
                u.setPasswd(strConvertBase(u.getPasswd()));
                this.userRepository.save(u);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除学生
     * @param studentId 学生id
     * @return 删除是否成功
     */
    public boolean deleteStudentIdByStudentId(Long studentId){
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
        if (!baseConvertStr(user.getPasswd()).equals(oldPwd)) return -2;
        user.setPasswd(strConvertBase(newPwd));
        try {
            this.userRepository.save(user);
            return 0;
        }catch (Exception e){
            return -3;
        }
    }
}
