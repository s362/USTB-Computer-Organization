package com.example.ustbdemo;


import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Service.TaskService;
import com.example.ustbdemo.Service.UserService;
//import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
}
