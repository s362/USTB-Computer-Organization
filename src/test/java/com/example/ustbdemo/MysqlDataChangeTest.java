package com.example.ustbdemo;


import com.example.ustbdemo.Model.DataModel.Score;
import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Service.ScoreService;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Service.UserService;
//import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import com.example.ustbdemo.Util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = GitlabdemoApplication.class)
public class MysqlDataChangeTest {

//    @Autowired
//    TaskService taskService;
//
//    @Test
//    public void changeData(){
//        System.out.println(taskService);
//        List<Task> taskList=taskService.findAllTask();
//        for (Task item:taskList){
//            String tdis=item.getTdis();
//            int index=tdis.indexOf("202.204.62.136");
//            if (index>0){
//                System.out.println(index+"----------------------------------");
//                System.out.println(item.getTid());
//                System.out.println(item.getTdis());
//                System.out.println("+++++++++++++++++++++++++++++++++++++==");
//                String ans=tdis.replaceFirst("202.204.62.136","192.168.100.104");
//                System.out.println(ans);
//
//                item.setTdis(ans);
//                taskService.saveTask(item);
//            }
////            System.out.println(item.getTdis());
//
//        }
//
//    }
//    @Autowired
//    UserService userService;
//
//    @Test
//    public void addUser(){
//
//        File file=new File("e:\\vscode-workspace\\python\\信安18.txt");
//        BufferedReader reader = null;
//        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
//            reader = new BufferedReader(new FileReader(file));
//            String tempString = null;
//            int line = 1;
//            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
//                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
//
//
//
//                User xx=userService.findByUserName(tempString);
//                if (xx==null){
//                    User user=new User();
//                    user.setUtype(2L);   //学生权限
//                    user.setUsername(tempString);
//                    user.setPasswd(tempString);
//                    userService.addUser(user);
//                }
//
//
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e1) {
//                }
//            }
//        }
//
//
//    }


//    //将学生的实验成绩导出
//    @Autowired
//    ScoreService scoreService;
//
//    @Autowired
//    UserService userService;
//
//    @Test
//    public void exportScore() {
//        List<User> userList=userService.findAll();
//
//        Map<String,Integer> mapUserScore552=new HashMap<>();  //汇编仿真一
//        Map<String,Integer> mapUserScore863=new HashMap<>();  //汇编仿真二
//        Map<String,Integer> mapUserScore871=new HashMap<>();  //verilog代码一
//        Map<String,Integer> mapUserScore872=new HashMap<>();  //verilog代码二
//
//        if (userList!=null){
//            for ( User user: userList) {
//                Score score;
//                score=scoreService.findScoreByUserandTid(user.getUid(),552L);
//                mapUserScore552.put(user.getUsername(),score==null?null:score.getTscore().intValue());
//                score=scoreService.findScoreByUserandTid(user.getUid(),863L);
//                mapUserScore863.put(user.getUsername(),score==null?null:score.getTscore().intValue());
//                score=scoreService.findScoreByUserandTid(user.getUid(),871L);
//                mapUserScore871.put(user.getUsername(),score==null?null:score.getTscore().intValue());
//                score=scoreService.findScoreByUserandTid(user.getUid(),872L);
//                mapUserScore872.put(user.getUsername(),score==null?null:score.getTscore().intValue());
//            }
//        }
//
//        File file=new File("F:\\D_disk\\ustbdemo\\grade.csv");
//
//        FileOutputStream fos = null;
//        OutputStreamWriter osw = null;
//
//        try {
//            if (!file.exists()) {
//                boolean hasFile = file.createNewFile();
//                if(hasFile){
//                    System.out.println("file not exists, create new file");
//                }
//                fos = new FileOutputStream(file);
//            } else {
//                System.out.println("file exists");
//                fos = new FileOutputStream(file, false);  //不要追加
//            }
//
//            osw = new OutputStreamWriter(fos, "GBK");
//            String content = "学号,实验一原理学习,实验一工程实现,实验二原理学习,实验二工程实现,";
//            osw.write(content); //写入内容
//            osw.write("\r\n");  //换行
//            for(User user: userList) {
//                content = user.getUsername()    + ',' + mapUserScore552.get(user.getUsername())
//                                                + ','+mapUserScore871.get(user.getUsername())
//                                                +','+mapUserScore863.get(user.getUsername())
//                                                +','+mapUserScore872.get(user.getUsername())+',';
//                osw.write(content); //写入内容
//                osw.write("\r\n");  //换行
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {   //关闭流
//            try {
//                if (osw != null) {
//                    osw.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//

}
