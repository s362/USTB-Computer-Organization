package com.example.ustbdemo;

import com.example.ustbdemo.Util.RsaUtil;
import org.junit.Test;

import java.util.Map;
//import redis.clients.jedis.Jedis;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TempTest {
    private static final int BUFFER_SIZE = 1024;
//    @Autowired
//    DataSource dataSource;
//    @Test
//    public void handleImg() throws Exception{
//        String str = FileUtil.getContent("C:\\Users\\bearking\\Desktop\\相关文件\\ide作业备份\\第五周作业\\01_vector介绍\\content.md");
//        String regex = "!\\[]\\((.+?)\\)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(str);
//        StringBuffer sb = new StringBuffer();
//
//        while (matcher.find()) {
//            String imgName = matcher.group(0).substring(11, matcher.group(0).length()-5);
//            String imgPah = OSUtil.isLinux()? "/OjFiles/"  + "/"  + "/images/" + imgName + ".png"  : "\\images\\" + imgName + ".png";
//            System.out.println(imgPah);
//            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
//    //            System.out.println(imgPah);
//            imgPah =  "<div align=center><img src=" + "\"" + "https://ide.eustb.com" + imgPah + "\"" + " width = \"80%\"></div>";
//            System.out.println(imgPah);
//            matcher.appendReplacement(sb, imgPah);
//        }
//        matcher.appendTail(sb);
//        System.out.println(sb.toString());
//    }
//
//    @Test
//    public void getFile(){
//        GitProcess gitProcess = new GitProcess();
//        try {
//            Integer project_id = gitProcess.getGitLabApi().getProjectApi().getProject("t20190820002", "teacher").getId();
//            System.out.println(project_id);
//        } catch (Exception e){
//            System.out.println(e.toString());
//            e.printStackTrace();
//        }
//
//        Integer teacher_id = gitProcess.getProjectId("t20190820002", "teacher");
//        GitProject gitProject = new GitProject();
//        gitProject.setModules(new LinkedList<GitFile>());
//        gitProject = gitProcess.setTeacherInfo(gitProject, teacher_id);
//        System.out.println(gitProject.getModules());
//
//    }
//
//    @Test
//    public void unzip(){
//        File srcFile = new File("C:\\Users\\bearking\\Desktop\\归档.zip");
////        FileUtil.unZip(srcFile, "C:\\Users\\bearking\\Desktop\\lala");
//    }
//
//    @Test
//    public void getFileName() {
//        String path = "C:\\Users\\bearking\\Desktop\\lala"; // 路径
//        File f = new File(path);
//        if (!f.exists()) {
//            System.out.println(path + " not exists");
//            return;
//        }
//        File fa[] = f.listFiles();
//        for (int i = 0; i < fa.length; i++) {
//            File fs = fa[i];
//            if (fs.isDirectory()) {
//                File imageFiles = new File(fs.getPath()+"/images");
//                File codeFiles = new File(fs.getPath()+"/files");
//                System.out.println(fs.getName() + " [目录]");
//            } else {
//                System.out.println(fs.getName());
//            }
//        }
//    }
//    @Test
//    public void getContent() throws Exception{
//        String path = "C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\files";
//        File f = new File(path);
//        if (!f.exists()) {
//            System.out.println(path + " not exists");
//            return;
//        }
//        File fa[] = f.listFiles();
//        for(int i = 0; i < fa.length; i++){
//            File fs = fa[i];
//            FileInputStream in = new FileInputStream(fs);
//            byte[] filecontent = new byte[(int)fs.length()];
//            in.read(filecontent);
//            String fcontent = new String(filecontent, "UTF-8");
//            System.out.println(fcontent);
//        }
//    }
//
//    @Test
//    public void readRM() throws Exception{
//        String path = "C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\content.txt";
//        File fs = new File(path);
//        FileInputStream in = new FileInputStream(fs);
//        byte[] filecontent = new byte[(int)fs.length()];
//        in.read(filecontent);
//        String fcontent = new String(filecontent, "UTF-8");
//        System.out.println(fcontent);
//
//    }
//
//    public String readRM_test(String path) throws Exception{
////        String path = "C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\content.txt";
//        File fs = new File(path);
//        FileInputStream in = new FileInputStream(fs);
//        byte[] filecontent = new byte[(int)fs.length()];
//        in.read(filecontent);
//        String fcontent = new String(filecontent, "UTF-8");
////        System.out.println(fcontent);
//        return fcontent;
//    }
//
//
//    @Test
//    public void patternUser() throws Exception{
//        String str = readRM_test("C:\\Users\\bearking\\Desktop\\lala\\1_wire类型\\content.txt");
//        String regex = "\\[(.+?)]";
////        String str = "<a οnclick=\"showUserName('[session.user.username]','[session.user.password]');\" >linkme</a>" ;
//        Pattern pattern = Pattern.compile(regex);
//        System.out.println(pattern);
//        Matcher matcher = pattern.matcher(str);
//        StringBuffer sb = new StringBuffer();
//        while (matcher.find()) {
//            System.out.println(">>>>> replace sequence : " + matcher.group(0) + "   "  +  matcher.group(0).substring(1, matcher.group(0).length()-1));
//            System.out.println(">>>>> index range : (" + matcher.start() + ", " + matcher.end() + ")");
//            System.out.println(">>>>> sub : " + str.substring(matcher.start(), matcher.end()));
//            matcher.appendReplacement(sb, "![avatar](/home/picture/1.png)");
//            System.out.println("-----------------");
//        }
//        matcher.appendTail(sb);
////        System.out.println(">>>> sb : " + sb.toString());
//        str = sb.toString();
//        System.out.println(str);
//    }
//
//    @Test
//    public void TestOS(){
//        System.out.println(OSUtil.isLinux());
//        System.out.println(OSUtil.isWindows());
//    }
//
//    @Test
//    public void getGBK() throws Exception{
//        System.out.println(FileUtil.codeString("C:\\Users\\bearking\\Desktop\\第一次作业.zip"));
//        System.out.println(FileUtil.codeString("C:\\Users\\bearking\\Desktop\\第三次作业.zip"));
//        System.out.println(FileUtil.codeString("C:\\Users\\bearking\\Desktop\\task.zip"));
//    }

//    @Test
//    public void redisTest(){
//        Jedis jedis = new Jedis("202.204.62.155", 6379);
//        jedis.set("lala", "1212");
//        System.out.println(jedis.get("lala"));System.out.println(jedis.get("25"));
//        System.out.println(jedis.ping());
//    }



    /**
     * @author zhanghongkai
     * @version 1.0
     * @Classname Test5
     * @Description Test5
     * @Date 2020/12/25 10:24
     */
    @Test
    public void main() {
        String rsaKeys = RsaUtil.encode("123");
        System.out.println("encode:"+ rsaKeys);
        String Key = RsaUtil.decode(rsaKeys);
        System.out.println("Key:"+ Key);
    }
}
