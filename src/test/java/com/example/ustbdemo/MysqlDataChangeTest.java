package com.example.ustbdemo;


import com.example.ustbdemo.Model.DataModel.*;
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
import java.util.ArrayList;
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

    /*

    //将学生的详细实验成绩导出，包括每个选择题题目的分数和提交次数等
    @Autowired
    ScoreService scoreService;
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Test
    public void exportDetailScore() {
        List<User> userList=userService.findAll();

        List<Long> simulateTaskList=new ArrayList<>();  //汇编仿真题的题号
        simulateTaskList.add(552L);
        simulateTaskList.add(863L);

        List<Long> verilogTaskList=new ArrayList<>();  //汇编仿真题的题号
        verilogTaskList.add(871L);
        verilogTaskList.add(872L);

        Map<String,Map<String,Map<String,Integer>>> userGrades=new HashMap<>();  //<姓名，<题号，<times/grades,次数/分数>>>
        Map<String,List<Assemble_Choose>> chooses=new HashMap<>();//<题号，选择题信息列表>
        for (Long it:simulateTaskList) {
            List<Assemble_Choose> assembleChooseList=taskService.getAssebleChoosesByTid(it);
            chooses.put(it.toString(),assembleChooseList);
        }


        if (userList!=null){
            for ( User user: userList) {
                System.out.println(user.toString()+"开始处理");
                Map<String,Map<String,Integer>> grades=new HashMap<>();

                //汇编题
                for (Long simulateTask:simulateTaskList){
                    List<Assemble_Choose> assembleChooseList=chooses.get(simulateTask.toString());
                    for (Assemble_Choose assembleChoose:assembleChooseList){   //其中的每个选择题
                        Assemble_Choose_Score assembleChooseScore=scoreService.findAssembleChooseScoreByUidandTid(user.getUid(),assembleChoose.getTcid());
                        Map<String,Integer> map=new HashMap<>();
                        map.put("times",assembleChooseScore==null?0:assembleChooseScore.getTimes().intValue());
                        map.put("grades",assembleChooseScore==null?0:assembleChooseScore.getAcscore().intValue());
                        grades.put(assembleChoose.getTcid().toString(),map);
//                        assert assembleChooseScore != null;
//                        System.out.println(assembleChooseScore.toString());
                    }
                    Assemble_Code_Score assembleCodeScore=scoreService.findAssembleCodeScoreByUidAndTid(user.getUid(),simulateTask);  //汇编代码
                    Map<String,Integer> map=new HashMap<>();
                    map.put("times",assembleCodeScore==null?0:assembleCodeScore.getTimes().intValue());
                    map.put("grades",assembleCodeScore==null?0:assembleCodeScore.getAssembleCodeScore().intValue());
                    grades.put(simulateTask.toString(),map);
//                    System.out.println(assembleCodeScore.toString());
                }
                System.out.println("汇编题处理完毕");
                //verilog题
                for (Long verilogTask:verilogTaskList){
                    Score score=scoreService.findScoreByUserandTid(user.getUid(),verilogTask);
                    VerilogRunTimes verilogRunTimes=scoreService.findVerilogRunTimesByTidAndUid(verilogTask,user.getUid());
                    Map<String,Integer> map=new HashMap<>();
                    map.put("times",verilogRunTimes==null?0:verilogRunTimes.getTimes().intValue());
                    map.put("grades",score==null?0:score.getTscore().intValue());
                    grades.put(verilogTask.toString(),map);
//                    System.out.println(map);
                }
                System.out.println("verilog题处理完毕");
                userGrades.put(user.getUsername(),grades);
            }
        }

        System.out.println("开始写入文件");
        File file=new File("F:\\D_disk\\ustbdemo\\grade_detail_20210301.csv");

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            if (!file.exists()) {
                boolean hasFile = file.createNewFile();
                if(hasFile){
                    System.out.println("file not exists, create new file");
                }
                fos = new FileOutputStream(file);
            } else {
                System.out.println("file exists");
                fos = new FileOutputStream(file, false);  //不要追加
            }

            osw = new OutputStreamWriter(fos, "GBK");

            String content ="学号,";
            for (Long it:simulateTaskList) {
                content+=it.toString()+"提交次数,"+it.toString()+"成绩,";
                List<Assemble_Choose>  assembleChooseList=chooses.get(it.toString());
                for (Assemble_Choose assembleChoose:assembleChooseList) content+=assembleChoose.getTcid()+"提交次数,"+assembleChoose.getTcid()+"成绩,";
            }
            for (Long it:verilogTaskList) content+=it.toString()+"提交次数,"+it.toString()+"成绩,";
            osw.write(content); //写入内容
            osw.write("\r\n");  //换行

            for(User user: userList) {
                content = user.getUsername()+",";
                Map<String,Map<String,Integer>> oneUser=userGrades.get(user.getUsername());
                for (Long it:simulateTaskList) {
                    Map<String,Integer> timesAndGrades=oneUser.get(it.toString());
                    content+=timesAndGrades.get("times")+","+timesAndGrades.get("grades")+",";
                    List<Assemble_Choose>  assembleChooseList=chooses.get(it.toString());
                    for (Assemble_Choose assembleChoose:assembleChooseList) {
                        timesAndGrades=oneUser.get(assembleChoose.getTcid().toString());
                        content+=timesAndGrades.get("times")+","+timesAndGrades.get("grades")+",";
                    }
                }
                for (Long it:verilogTaskList) {
                    Map<String,Integer> timesAndGrades=oneUser.get(it.toString());
                    content+=timesAndGrades.get("times")+","+timesAndGrades.get("grades")+",";
                }
                osw.write(content); //写入内容
                osw.write("\r\n");  //换行xv
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {   //关闭流
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("写入结束");
        }

    }

    */
}
