package com.example.ustbdemo.Controller;
import com.example.ustbdemo.Model.DataModel.Instruction;
import com.example.ustbdemo.Model.DataModel.Simulation;
import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Service.UserService;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.FileUtil;
import com.example.ustbdemo.Util.GitProcess;
import com.example.ustbdemo.Util.OSUtil;
import com.example.ustbdemo.Util.ResultUtil;
import org.gitlab4j.api.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.ObjectStreamClass;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/signin")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

//    登录验证,正确就返回jwt，错误返回报错信息
    @PostMapping(value = "/", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> login(@RequestBody User user){
        System.out.println(user);
        try{
            if (userService.getByUsernameAndPwd(user.getUsername(), user.getPasswd()) != null){
                String token = JwtUtil.sign(user.getUsername(), user.getPasswd());
                if(token != null){
                    Result result = new Result();
                    result.setObject(token);
                    return ResultUtil.getResult(result, HttpStatus.OK);
                }
            }
            System.out.println("无此用户");
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        }
    }

//    添加用户
    @PostMapping("/adduser")
    public ResponseEntity<Result> addUser(String username, String upassword){
        System.out.println(username + "   " + upassword);
        User user = new User();
        user.setUsername(username);
        user.setPasswd(upassword);

        if(userService.addUser(user)){
            return ResultUtil.getResult(new Result(), HttpStatus.OK);
        } else{
            return ResultUtil.getResult(new Result("插入失败", false), HttpStatus.BAD_REQUEST);
        }
    }

//    错误返回
    @PostMapping("/401")
    public ResponseEntity<Result> error(){
        return ResultUtil.getResult(new Result("登录失败", false), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/initial")
    public ResponseEntity<Result> initial() throws Exception{
        initialFile();
        taskService.initialRepository();
        try {
            deleteAllGitGroup();
        } catch (Exception e) {
            System.out.println("删除所有工程失败" + e.toString());
        }
        User user1 = new User("41624110", "41624110", 2l);
        User user2 = new User("41624111", "41624111", 2l);
        User user3 = new User("41624112", "41624112", 2l);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        Simulation simulation = new Simulation("理想5级流水线cpu");
        Simulation simulation1 = new Simulation("数据重定向五级流水线cpu");
        Simulation simulation2 = new Simulation("重定向+暂停五级流水线cpu");
        Simulation simulation3 = new Simulation("其他");
        this.taskService.addSimulation(simulation);
        this.taskService.addSimulation(simulation1);
        this.taskService.addSimulation(simulation2);
        this.taskService.addSimulation(simulation3);

        Instruction instruction = new Instruction("指令说明书V1.0", Instruction.EXAMPLE_INSTRUCTION_FILEPATH);
        this.taskService.addInstruction(instruction);
        return ResultUtil.getResult(new Result("初始化成功", true), HttpStatus.BAD_REQUEST);
    }

    private void deleteAllGitGroup() throws Exception{
        GitProcess gitProcess = new GitProcess();
        for (Group group : gitProcess.getGitLabApi().getGroupApi().getGroups()){
            System.out.println(group.getName());
            gitProcess.getGitLabApi().getGroupApi().deleteGroup(group.getId());
        }
    }

    private void initialFile(){
        File static_file = new File(OSUtil.isLinux() ? FileUtil.STATIC_PATH_LINUX : FileUtil.STATIC_PATH_WIN);
        if(static_file.exists()){
            FileUtil.deleteDirectory(static_file.getPath());
        }
        if(!static_file.getParentFile().exists()) static_file.getParentFile().mkdir();
        static_file.mkdir();

        File task_file = new File(OSUtil.isLinux() ? FileUtil.FILE_PATH_LINUX : FileUtil.FILE_PATH_WIN);
        if(task_file.exists()){
            FileUtil.deleteDirectory(task_file.getPath());
        }
        if(!task_file.getParentFile().exists()) task_file.getParentFile().mkdir();
        task_file.mkdir();

        String initialNames[] = {"exampleSimulationPic.png", "exampleSimuResult.json", "exampleInstructionFile.doc", "exampleTaskFile.zip"};

        for(int i = 0; i < 4; i++){
            String exampleInstructionOrignPath = (OSUtil.isLinux()? FileUtil.INITIAL_PATH_LINUX : FileUtil.INITIAL_PATH_WIN) + initialNames[i];
            String exampleInstructionAfterPath = (OSUtil.isLinux()? FileUtil.STATIC_PATH_LINUX : FileUtil.STATIC_PATH_WIN) + initialNames[i];
            FileUtil.copyFile(exampleInstructionOrignPath, exampleInstructionAfterPath);
        }
      }
}
