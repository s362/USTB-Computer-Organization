package com.example.ustbdemo;


import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
