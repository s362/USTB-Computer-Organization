package com.example.ustbdemo.Controller;
import com.example.ustbdemo.Aspect.ControllerRequestAdvice;
import com.example.ustbdemo.Model.DataModel.Instruction;
import com.example.ustbdemo.Model.DataModel.Score;
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
import org.glassfish.jersey.internal.guava.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.example.ustbdemo.Shiro.JwtUtil.verify;
import static com.example.ustbdemo.Util.Base64Convert.baseConvertStr;


@RestController
@RequestMapping("/api/signin")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    final String loginUrl = "http://202.205.145.156:8017/sys/api/user/validate?";//??

    //纪录现有的token
    Map<String, String> tokenmap = new HashMap<String, String>();

//    普通登录验证,正确就返回jwt，错误返回报错信息
    @PostMapping(value = "/", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> login(@RequestBody User user){
        logger.info(user.toString());
        try{
//      检查用户名是否存在
            User user0=userService.findByUserName(user.getUsername());
            if(user0 == null){
                return ResultUtil.getResult(new Result("用户名不存在"), HttpStatus.BAD_REQUEST);
            } else{
                user.setUpasswd(baseConvertStr(user.getPasswd()));
//      用户名存在，但是密码错误次数过多
                if(user0.getLock_times() == 5) {
                    long elapsedtime = (new Date().getTime() - user0.getLock_at().getTime()) / (60 * 1000);
                    if (elapsedtime >= 10) {
                        user0.setLock_times(user0.getLock_times() - 1);
                        userService.updateUsr(user0);
                    } else {
                        String t = Long.toString(10l - elapsedtime);
                        Result result = new Result("请" + t + "分钟后重试");
                        result.setSuccess(false);
                        return ResultUtil.getResult(result, HttpStatus.OK);
                    }
                }
            }
            User user1=userService.getByUsernameAndPwd(user.getUsername(), user.getPasswd());

            if ( user1 != null ){
//      登录成功清空密码错误次数，若密码错误次数等于5，禁止登录
                if(user1.getLock_times()<5) {
                    user1.setLock_times(0l);
                    userService.updateUsr(user1);
                } else{
                    Result result = new Result("密码错误次数过多，请稍后再试");
                    result.setSuccess(false);
                    return ResultUtil.getResult(result, HttpStatus.OK);
                }
//      签名，生成jwt
                String token = JwtUtil.sign(user.getUsername());
//      检查密码是否超过90天未修改
                long timeslot = 0L;
                timeslot = new Date().getTime() - user1.getUpdate_at().getTime();
                if( timeslot/(24*60*60*1000)>=90 ){
                    Result result = new Result();
                    result.setObject(token);
                    result.setNote(user1.getUtype());           //返回token的同时返回该用户的权限等级，方便前端判断
                    result.setMessage("密码超过90天未修改，请修改密码");
                    result.setSuccess(false);
                    return ResultUtil.getResult(result, HttpStatus.OK);
                }
                if(token != null){
                    // 将生成的token纪录下来
                    tokenmap.put(user1.getUsername(),token);
                    if(checkTokenList()>500l){
                        String t = Long.toString(checkTokenList() - 500l);
                        Result result = new Result("当前排队人数" + t + "人");
                        result.setSuccess(false);
                        return ResultUtil.getResult(result, HttpStatus.OK);
                    } else {
                        Result result = new Result();
                        result.setObject(token);
                        result.setNote(user1.getUtype());           //返回token的同时返回该用户的权限等级，方便前端判断
                        result.setMessage(user1.getUsername());   //返回前端用户名，便于显示
                        return ResultUtil.getResult(result, HttpStatus.OK);
                    }
                }
            } logger.info("无此用户");


            if(user0.getLock_times()<5){
                long locktimes = user0.getLock_times()+1l;
                user0.setLock_times(locktimes);
                user0.setLock_at(new Date());
                userService.updateUsr(user0);
                return ResultUtil.getResult(new Result("密码错误"), HttpStatus.BAD_REQUEST);
            }else{
                Result result = new Result("密码错误次数过多，请10分钟后再试");
                result.setSuccess(false);
                return ResultUtil.getResult(result, HttpStatus.OK);
            }
        } catch (Exception e){
            logger.info(e.toString());
            return ResultUtil.getResult(new Result("密码错误了"), HttpStatus.BAD_REQUEST);
        }
    }

//    国家项目会带有token，这里对国家平台生成的token进行验证，并且在本地数据库生成一个对应用户
    @PostMapping(value = "/jwtlogin", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> jwtlogin(@RequestBody JsonNode tokenNode){
        String token = tokenNode.path("token").asText();
        if(token.indexOf("&ticket") != -1){
            token = token.substring(0,token.indexOf("&ticket"));
        }
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
                user.setUtype(2L);   //新增学生权限设置
                user.setUsername(root.path("un").asText());
                userService.addUser(user);
            }
//            进行签名，返回本地生成的token给前端
            String jwtToken = JwtUtil.sign(root.path("un").asText());

            Result result=new Result(jwtToken,true);
//            result.setMessage(root.path("un").asText());  //返回前端用户名，便于显示
            result.setMessage(jwtToken);
            result.setObject(root.path("un").asText());  //返回前端用户名，便于显示
            return ResultUtil.getResult(result, HttpStatus.OK);
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
                user.setUtype(2L);//新增学生权限设置
                userService.addUser(user);
            }
//            进行签名，生成token
            String token = JwtUtil.sign(user.getUsername());
            logger.info(user.getUsername());
            logger.info("token: "+token);



            Result result=new Result();
            result.setObject((Object)token);
            result.setMessage(user.getUsername());  //将用户名返回，便于前端显示
            result.setSuccess(true);
            return ResultUtil.getResult(result, HttpStatus.OK);
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

    @PostMapping("/about")
    public ResponseEntity<Result> about(){

        String path=OSUtil.isLinux()?FileUtil.STATIC_PATH_LINUX:FileUtil.STATIC_PATH_WIN+File.separator+"systemAbout.md";
        try {
            String about=FileUtil.getContent(path);
            Result result=new Result();
            result.setSuccess(true);
            result.setObject(about);
            return ResultUtil.getResult(result,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getResult(new Result("file not exist"),HttpStatus.BAD_REQUEST);
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

//    本地平台初始化，删除所有文件，删除gitlab中所有数据和数据库所有数据，调试用
    @PostMapping("/initial")
    public ResponseEntity<Result> initial() throws Exception{
        initialFile();
        taskService.initialRepository();
        try {
            deleteAllGitGroup();
        } catch (Exception e) {
            logger.info("删除所有工程失败" + e.toString());
        }
        User user1 = new User("test1", "123456", 2l);
        User user2 = new User("test2", "123456", 2l);
        User user3 = new User("test3", "123456", 2l);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        Simulation simulation = new Simulation("理想5级流水线cpu", 0l);
        Simulation simulation1 = new Simulation("数据重定向五级流水线cpu", 1L);
        Simulation simulation2 = new Simulation("重定向+暂停五级流水线cpu", 2L);
        Simulation simulation3 = new Simulation("其他", 3L);
        this.taskService.addSimulation(simulation);
        this.taskService.addSimulation(simulation1);
        this.taskService.addSimulation(simulation2);
        this.taskService.addSimulation(simulation3);

        Instruction instruction = new Instruction("指令说明书V1.0", Instruction.EXAMPLE_INSTRUCTION_FILEPATH);
        this.taskService.addInstruction(instruction);
        return ResultUtil.getResult(new Result("初始化成功", true), HttpStatus.OK);
    }

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

        File report_file = new File(OSUtil.isLinux() ? FileUtil.REPORT_PATH_LINUX : FileUtil.REPORT_PATH_WIN);
        if(report_file.exists()){
            FileUtil.deleteDirectory(report_file.getPath());
        }
        if(!report_file.getParentFile().exists()) report_file.getParentFile().mkdir();
        report_file.mkdir();

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

    private long checkTokenList(){
        long count = 0L;
        for (Map.Entry<String, String> entry : tokenmap.entrySet()) {
                if(!verify(entry.getValue())){
                    tokenmap.remove(entry.getKey());
                } else {
                    count++;
                }
        }
        return count;
    }

}
