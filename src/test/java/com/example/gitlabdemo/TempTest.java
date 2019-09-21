package com.example.gitlabdemo;

import com.example.gitlabdemo.Model.GitFile;
import com.example.gitlabdemo.Model.GitProject;
import com.example.gitlabdemo.Util.GitProcess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.LinkedList;

//import redis.clients.jedis.Jedis;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TempTest {
    @Autowired
    DataSource dataSource;
    @Test
    public void contextLoads() {
        try{
            System.out.println(dataSource.getConnection());
            System.out.println("lala");
        } catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            System.out.println("falsle");
        }
    }

    @Test
    public void getFile(){
        GitProcess gitProcess = new GitProcess();
        try {
            Integer project_id = gitProcess.getGitLabApi().getProjectApi().getProject("t20190820002", "teacher").getId();
            System.out.println(project_id);
        } catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

        Integer teacher_id = gitProcess.getProjectId("t20190820002", "teacher");
        GitProject gitProject = new GitProject();
        gitProject.setModules(new LinkedList<GitFile>());
        gitProject = gitProcess.setTeacherInfo(gitProject, teacher_id);
        System.out.println(gitProject.getModules());

    }

//    @Test
//    public void redisTest(){
//        Jedis jedis = new Jedis("202.204.62.155", 6379);
//        jedis.set("lala", "1212");
//        System.out.println(jedis.get("lala"));System.out.println(jedis.get("25"));
//        System.out.println(jedis.ping());
//    }
}
