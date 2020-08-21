package com.example.ustbdemo.Controller;
import com.example.ustbdemo.Aspect.ControllerRequestAdvice;
import com.example.ustbdemo.Model.DataModel.Instruction;
import com.example.ustbdemo.Model.DataModel.Simulation;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Service.UserService;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Shiro.TestJWT;
import com.example.ustbdemo.Util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.gitlab4j.api.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;


@RestController
@RequestMapping("/api/signin")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    final String loginUrl = "http://202.205.145.156:8017/sys/api/user/validate?";//??


//    普通登录验证,正确就返回jwt，错误返回报错信息
    @PostMapping(value = "/", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> login(@RequestBody User user){
        logger.info(user.toString());
        try{
            User user1=userService.getByUsernameAndPwd(user.getUsername(), user.getPasswd());
            if ( user1 != null){
//                签名，生成jwt
                String token = JwtUtil.sign(user.getUsername());
                if(token != null){
                    Result result = new Result();
                    result.setObject(token);
                    result.setNote(user1.getUtype());           //返回token的同时返回该用户的权限等级，方便前端判断
                    return ResultUtil.getResult(result, HttpStatus.OK);
                }
            } logger.info("无此用户");

            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            logger.info(e.toString());
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        }
    }

//    国家项目会带有token，这里对国家平台生成的token进行验证，并且在本地数据库生成一个对应用户
    @PostMapping(value = "/jwtlogin", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> jwtlogin(@RequestBody JsonNode tokenNode){
        String token = tokenNode.path("token").asText();
        logger.info(token);
        try {
//            对token进行校验
            String json = TestJWT.dencrty(token);
            logger.info(json);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            logger.info(token);
//            logger.info(String.valueOf(root.path("un")));
            if(root.path("un")==null||root.path("un").asText().equals("")){
                logger.info("json格式不是国家平台格式");
                if(root.path("username")!=null&& !root.path("username").asText().equals("")){
                    logger.info("json格式是本地平台格式，直接返回");
                    return ResultUtil.getResult(new Result(token,true), HttpStatus.OK);
                }
                logger.info("json格式不是本地平台格式");
                return ResultUtil.getResult(new Result("json格式错误",false), HttpStatus.BAD_REQUEST);
            }
//            如果该用户在本地系统里没有，则在本地系统中添加该用户
            if(userService.findByUserName(root.path("un").asText())== null){
                User user = new User();
                user.setUsername(root.path("un").asText());
                userService.addUser(user);
            }
//            进行签名，返回本地生成的token给前端
            String jwtToken = JwtUtil.sign(root.path("un").asText());
            return ResultUtil.getResult(new Result(jwtToken,true), HttpStatus.OK);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.info(e.toString());
            return ResultUtil.getResult(new Result("json错误  "+ e.toString()), HttpStatus.UNAUTHORIZED);
        }
    }

//    用国家平台用户名密码进行登录，用本地调用国家接口进行验证
    @PostMapping(value = "/platformlogin", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> platformlogin(@RequestBody User user){
        try{
//            国家平台要求的格式
            String nonce = getRandomString();
            String cnonce = getRandomString();
            String username = user.getUsername();
            String password = user.getPasswd();
            password = getSHA256(password);
            password = getSHA256(nonce+password.toUpperCase()+cnonce.toUpperCase()).toUpperCase();
            String param = String.format("username=%s&password=%s&nonce=%s&cnonce=%s", username, password, nonce,cnonce);

            String command = String.format(loginUrl + param);
//            if(OSUtil.isLinux()) command = String.format("curl \'%s?%s\'", url, param);
            logger.info(command);
//            进行get请求
            String strbr = HttpClient.doGet(command);
            ObjectMapper mapper = new ObjectMapper();
            Map readValue = mapper.readValue(strbr, Map.class);
            if(readValue == null) throw new Exception();
//            如果code 为 0，表示帐号密码正确，否则错误
            if ((int)readValue.get("code") != 0) throw new Exception();

//            如果系统中没有该user，则在本地平台添加用户
            if(userService.findByUserName(username)== null){
                userService.addUser(user);
            }
//            进行签名，生成token
            String token = JwtUtil.sign(user.getUsername());
            logger.info(user.getUsername());
            logger.info("token: "+token);
            return ResultUtil.getResult(new Result((Object)token), HttpStatus.OK);
        } catch (Exception e){
            logger.info(e.toString());
            return ResultUtil.getResult(new Result("帐号或密码错误"), HttpStatus.BAD_REQUEST);
        }
    }

//    添加用户
    @PostMapping("/adduser")
    public ResponseEntity<Result> addUser(String username, String upassword){
        logger.info(username + "   " + upassword);
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

//    国家平台要求的，生成随机字符
    public String getRandomString(){
        String str="ABCDEF0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<16;i++){
            int number=random.nextInt(str.length()-1);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

////    本地平台初始化，删除所有文件，删除gitlab中所有数据和数据库所有数据，调试用
//    @PostMapping("/initial")
//    public ResponseEntity<Result> initial() throws Exception{
//        initialFile();
//        taskService.initialRepository();
//        try {
//            deleteAllGitGroup();
//        } catch (Exception e) {
//            logger.info("删除所有工程失败" + e.toString());
//        }
//        User user1 = new User("test1", "123456", 2l);
//        User user2 = new User("test2", "123456", 2l);
//        User user3 = new User("test3", "123456", 2l);
//        userService.addUser(user1);
//        userService.addUser(user2);
//        userService.addUser(user3);
//        Simulation simulation = new Simulation("理想5级流水线cpu", 0l);
//        Simulation simulation1 = new Simulation("数据重定向五级流水线cpu", 1L);
//        Simulation simulation2 = new Simulation("重定向+暂停五级流水线cpu", 2L);
//        Simulation simulation3 = new Simulation("其他", 3L);
//        this.taskService.addSimulation(simulation);
//        this.taskService.addSimulation(simulation1);
//        this.taskService.addSimulation(simulation2);
//        this.taskService.addSimulation(simulation3);
//
//        Instruction instruction = new Instruction("指令说明书V1.0", Instruction.EXAMPLE_INSTRUCTION_FILEPATH);
//        this.taskService.addInstruction(instruction);
//        return ResultUtil.getResult(new Result("初始化成功", true), HttpStatus.BAD_REQUEST);
//    }

    private void deleteAllGitGroup() throws Exception{
        GitProcess gitProcess = new GitProcess();
        for (Group group : gitProcess.getGitLabApi().getGroupApi().getGroups()){
            logger.info(group.getName());
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


    /**
     * 利用java原生的类实现SHA256加密
     *
     * @param str
     * @return
     */
    private String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
