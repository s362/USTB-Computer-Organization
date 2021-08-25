package com.example.ustbdemo;

import org.junit.Test;

//package com.example.ustbdemo;

import com.alibaba.fastjson.JSON;
import com.example.ustbdemo.Model.DataModel.*;
import com.example.ustbdemo.Model.UtilModel.ilabResult;
import com.example.ustbdemo.Model.UtilModel.steps;
import com.example.ustbdemo.Service.*;
import com.example.ustbdemo.Shiro.JwtUtil;
import com.example.ustbdemo.Util.HttpClient;
import com.example.ustbdemo.Util.HttpClientUtil;
import com.example.ustbdemo.Util.RsaUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import redis.clients.jedis.Jedis;
//
////@RunWith(SpringRunner.class)
////@SpringBootTest
public class TempTest {
    @Test
    public void test(){
        String url = "jdbc:mysql://127.0.0.1:3306/ustbdemo?autoReconnect=true&useSSL=false&characterEncoding=utf-8 ";
        String newurl = url.substring(url.indexOf("3306"), url.indexOf("?")).replaceAll("/", "").replaceAll("3306", "");
        System.out.println(newurl);
    }
}
//    private static final int BUFFER_SIZE = 1024;
////    @Autowired
////    DataSource dataSource;
////    @Test
////    public void handleImg() throws Exception{
////        String str = FileUtil.getContent("C:\\Users\\bearking\\Desktop\\相关文件\\ide作业备份\\第五周作业\\01_vector介绍\\content.md");
////        String regex = "!\\[]\\((.+?)\\)";
////        Pattern pattern = Pattern.compile(regex);
////        Matcher matcher = pattern.matcher(str);
////        StringBuffer sb = new StringBuffer();
////
////        while (matcher.find()) {
////            String imgName = matcher.group(0).substring(11, matcher.group(0).length()-5);
////            String imgPah = OSUtil.isLinux()? "/OjFiles/"  + "/"  + "/images/" + imgName + ".png"  : "\\images\\" + imgName + ".png";
////            System.out.println(imgPah);
////            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
////    //            System.out.println(imgPah);
////            imgPah =  "<div align=center><img src=" + "\"" + "https://ide.eustb.com" + imgPah + "\"" + " width = \"80%\"></div>";
////            System.out.println(imgPah);
////            matcher.appendReplacement(sb, imgPah);
////        }
////        matcher.appendTail(sb);
////        System.out.println(sb.toString());
////    }
////
////    @Test
////    public void getFile(){
////        GitProcess gitProcess = new GitProcess();
////        try {
////            Integer project_id = gitProcess.getGitLabApi().getProjectApi().getProject("t20190820002", "teacher").getId();
////            System.out.println(project_id);
////        } catch (Exception e){
////            System.out.println(e.toString());
////            e.printStackTrace();
////        }
////
////        Integer teacher_id = gitProcess.getProjectId("t20190820002", "teacher");
////        GitProject gitProject = new GitProject();
////        gitProject.setModules(new LinkedList<GitFile>());
////        gitProject = gitProcess.setTeacherInfo(gitProject, teacher_id);
////        System.out.println(gitProject.getModules());
////
////    }
////
////    @Test
////    public void unzip(){
////        File srcFile = new File("C:\\Users\\bearking\\Desktop\\归档.zip");
//////        FileUtil.unZip(srcFile, "C:\\Users\\bearking\\Desktop\\lala");
////    }
////
////    @Test
////    public void getFileName() {
////        String path = "C:\\Users\\bearking\\Desktop\\lala"; // 路径
////        File f = new File(path);
////        if (!f.exists()) {
////            System.out.println(path + " not exists");
////            return;
////        }
////        File fa[] = f.listFiles();
////        for (int i = 0; i < fa.length; i++) {
////            File fs = fa[i];
////            if (fs.isDirectory()) {
////                File imageFiles = new File(fs.getPath()+"/images");
////                File codeFiles = new File(fs.getPath()+"/files");
////                System.out.println(fs.getName() + " [目录]");
////            } else {
////                System.out.println(fs.getName());
////            }
////        }
////    }
////    @Test
////    public void getContent() throws Exception{
////        String path = "C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\files";
////        File f = new File(path);
////        if (!f.exists()) {
////            System.out.println(path + " not exists");
////            return;
////        }
////        File fa[] = f.listFiles();
////        for(int i = 0; i < fa.length; i++){
////            File fs = fa[i];
////            FileInputStream in = new FileInputStream(fs);
////            byte[] filecontent = new byte[(int)fs.length()];
////            in.read(filecontent);
////            String fcontent = new String(filecontent, "UTF-8");
////            System.out.println(fcontent);
////        }
////    }
////
////    @Test
////    public void readRM() throws Exception{
////        String path = "C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\content.txt";
////        File fs = new File(path);
////        FileInputStream in = new FileInputStream(fs);
////        byte[] filecontent = new byte[(int)fs.length()];
////        in.read(filecontent);
////        String fcontent = new String(filecontent, "UTF-8");
////        System.out.println(fcontent);
////
////    }
////
////    public String readRM_test(String path) throws Exception{
//////        String path = "C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\content.txt";
////        File fs = new File(path);
////        FileInputStream in = new FileInputStream(fs);
////        byte[] filecontent = new byte[(int)fs.length()];
////        in.read(filecontent);
////        String fcontent = new String(filecontent, "UTF-8");
//////        System.out.println(fcontent);
////        return fcontent;
////    }
////
////
////    @Test
////    public void patternUser() throws Exception{
////        String str = readRM_test("C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\content.txt");
////        String regex = "\\[(.+?)]";
//////        String str = "<a οnclick=\"showUserName('[session.user.username]','[session.user.password]');\" >linkme</a>" ;
////        Pattern pattern = Pattern.compile(regex);
////        System.out.println(pattern);
////        Matcher matcher = pattern.matcher(str);
////        StringBuffer sb = new StringBuffer();
////        while (matcher.find()) {
////            System.out.println(">>>>> replace sequence : " + matcher.group(0) + "   "  +  matcher.group(0).substring(1, matcher.group(0).length()-1));
////            System.out.println(">>>>> index range : (" + matcher.start() + ", " + matcher.end() + ")");
////            System.out.println(">>>>> sub : " + str.substring(matcher.start(), matcher.end()));
////            matcher.appendReplacement(sb, "![avatar](/home/picture/1.png)");
////            System.out.println("-----------------");
////        }
////        matcher.appendTail(sb);
//////        System.out.println(">>>> sb : " + sb.toString());
////        str = sb.toString();
////        System.out.println(str);
////    }
////
////    @Test
////    public void TestOS(){
////        System.out.println(OSUtil.isLinux());
////        System.out.println(OSUtil.isWindows());
////    }
////
////    @Test
////    public void getGBK() throws Exception{
////        System.out.println(FileUtil.codeString("C:\\Users\\bearking\\Desktop\\第一次作业.zip"));
////        System.out.println(FileUtil.codeString("C:\\Users\\bearking\\Desktop\\第三次作业.zip"));
////        System.out.println(FileUtil.codeString("C:\\Users\\bearking\\Desktop\\task.zip"));
////    }
//
////    @Test
////    public void redisTest(){
////        Jedis jedis = new Jedis("202.204.62.155", 6379);
////        jedis.set("lala", "1212");
////        System.out.println(jedis.get("lala"));System.out.println(jedis.get("25"));
////        System.out.println(jedis.ping());
////    }
////
////    @Autowired
////    ScoreService scoreService;
////
////    @Autowired
////    QuestionService questionService;
////
////    @Autowired
////    TaskService taskService;
////
////    @Autowired
////    UserService userService;
////
////    /**
////     * @author zhanghongkai
////     * @version 1.0
////     * @Classname Test5
////     * @Description Test5
////     * @Date 2020/12/25 10:24
////     */
////    @Test
////    public void main() {
////        System.out.println(System.currentTimeMillis());
////        String username= "test1";
////        userService.getTeachers();
////        User user = userService.findByUserName("test1");
////        Long uid = userService.findByUserName(username).getUid();
////        Assemble_Choose_Score assemble_choose_score = scoreService.findAssembleChooseScoreByUidandTid(uid, 552l);
////
////
////        ilabResult Result = new ilabResult();
////        Result.setAppid(10040);
////        Result.setStartTime(System.currentTimeMillis());
////        Result.setEndTime(System.currentTimeMillis()+10000);
////
////        steps step1 =new steps(1,"汇编仿真",assemble_choose_score.getUpdatedate().getTime()-12000l,assemble_choose_score.getUpdatedate().getTime(),1200,10, (int) (getGradeOfTask(uid,552l)/4), 2,"优","通过汇编程序在线评测，即可获得该步骤满分。");
////    }
//
//    //获取题目对应的分数
//    private Long getGradeOfTask(Long uid, Long tid) {
//        Score score = scoreService.findScoreByUserandTid(uid, tid);
//        if (score == null) return 0l;
//        return score.getTscore();
//    }
//
//
//    @Autowired
//    ScoreService scoreService;
//
//    @Autowired
//    QuestionService questionService;
//
//    @Autowired
//    TaskService taskService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    ilabUserService ilabuserService;
//
//    @Test
//    public void serviceTest() throws Exception{
//        System.out.println(System.currentTimeMillis());
//    }
//
//
//    @Test
//    public void ilab() throws Exception {
////        User user = userService.findByUserName("test1");
////        ilabUser ilabuser = ilabuserService.getilabUser("test1");
//        String token = java.net.URLEncoder.encode("JQXIvOb3lNoNZrt5EPUWA2wse2ws9r/ZQKQyeG4TNJH/V793F3VSrDvuuCJL6nOmq0YPblrY8M3w+t8PWtBeO1J0UJzdBKFVXZY50WsNKeo=");
////        Long uid = user.getUid();
////        System.out.println(uid);
////        Assemble_Code_Score assemble_code_score = scoreService.findAssembleCodeScoreByUidAndTid(uid, 552l);
//
//        long t = 1625952587963l;
//        ilabResult Result = new ilabResult();
//        Result.setUsername("test1");
//        Result.setTitle("流水线CPU虚拟仿真实验");
//        Result.setStatus(1);
//        Result.setScore(10);
//        Result.setStartTime(1625952587963l);
//        Result.setEndTime(1625952587963l + 992815464203l);
//        Result.setAppid(100400l);
//        Result.setOriginId("54362");
//        List<steps> stepsList = new LinkedList<>();
////        System.out.println((int) (getGradeOfTask(uid, 552l) / 4));
////        System.out.println(assemble_code_score.getUpdatedate());
////        System.out.println((int) (getGradeOfTask(uid, 552l) / 4));
//
//        stepsList.add(new steps(1, "汇编仿真", 1625952587963l + 60l, 1625952587963l + 1000l, 1200, 10, 10, 1, "优", "通过汇编程序在线评测，即可获得该步骤满分。"));
//        stepsList.add(new steps(2, "单步仿真", 1625952587963l + 2000l, 1625952587963l + 3000l, 600, 0, 0, 1, "完成实验", "单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(3, "客观题评测", 1625952587963l + 1640465, 1625952587963l + 2304655, 600, 0, 0, 1, "完成实验", "单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(4, "冲突单步仿真", 1625952587963l + 2305666, 1625952587963l + 3004636, 600, 0, 0, 1, "完成实验", "单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(5, "客观题评测", 1625952587963l + 3105626, 1625952587963l + 3703455, 600, 0, 0, 1, "完成实验", "单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(6, "汇编仿真", 1625952587963l + 3803455, 1625952587963l + 4405021, 600, 20, 10, 2, "完成实验", "单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(7, "第七步", 1625952587963l + 4504451, 1625952587963l + 5204655, 600, 10, 5, 3, "完成实验", "单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(8, "第八步", 1625952587963l + 5214945, 1625952587963l + 5702345, 500, 10, 7, 2, "完成实验", "单步执行"));
//        stepsList.add(new steps(9, "第九步", 1625952587963l + 5810345, 1625952587963l + 6121723, 650, 20, 10, 1, "完成实验", "第九步"));
//        stepsList.add(new steps(10,"十",1625952587963l+6132894,1625952587963l+6217239,100,10,3,4,"完成实验","单步执行，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(11,"十一",1625952587963l+6355948,1625952587963l+7022348,700,7,3,2,"完成实验","单步执行，寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(12,"客观题评测",1625952587963l+9042348,1625952587963l+9822138,600,0,0,1,"完成实验","单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(13,"客观题评测",1625952587963l+9823338,1625952587963l+11104238,1200,0,0,1,"完成实验","单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//        stepsList.add(new steps(14,"客观题评测",1625952587963l+11114238,1625952587963l+11704239,600,0,0,1,"完成实验","单步执行，分别完成汇编程序在指令级视角和流水线级视角的仿真，观察分析寄存器堆中数据变化，作为步骤3测评题目的答题依据。与步骤3共占10分。"));
//
//
//        Result.setSteps(stepsList);
////        Map res = convertBean.convertTomap(Result);
////        String token = "AAABepW2L2MCAAAAAAABlXo%3D.yoBqz5uRz0AU4Zn0ZvBAqdSMcxpRs5mCe074tEzW0JD75UoRrpcnF0%2Fs5eXwa2Mw.l9HEtnf2Dx%2BNNGFArjc9EhcAeblfIrW1xIKHXlZnUBA%3D";
//        String command = "http://202.205.145.156:8017/open/api/v2/data_upload?access_token=" + token;
//        ;
////        logger.info(command);
//        HttpClient httpClient = new HttpClient();
//        // 要调用的接口方
//        String json = JSON.toJSONString(Result);
//        System.out.println(json);
//        String strbr = HttpClientUtil.doPostJson(command, json);
//
//
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonObj = mapper.readTree(strbr);
//        if (jsonObj.path("code").asInt() == 0) {
//            System.out.println("上传调用成功");
////            return true;
//        } else {
//            System.out.println(strbr.toString());
//            System.out.println(jsonObj.path("code").asText());
//            System.out.println(jsonObj.path("msg").asText());
////            return false;
//        }
//    }
//}
