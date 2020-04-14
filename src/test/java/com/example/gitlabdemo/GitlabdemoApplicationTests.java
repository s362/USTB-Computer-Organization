package com.example.gitlabdemo;

import com.example.gitlabdemo.Controller.TeacherController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitlabdemoApplicationTests {
    @Autowired
    TeacherController teacherController;
    @Test
    public void contextLoads() {
        System.out.println(teacherController.getGrade());
    }

}
