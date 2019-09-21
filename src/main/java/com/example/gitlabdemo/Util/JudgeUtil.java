package com.example.gitlabdemo.Util;

import com.example.gitlabdemo.Model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.Null;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class JudgeUtil {
    static public JsonNode shell(String task_id, String user_id) {
        String command = "sudo docker run bearking/ojide:v7 /home/pythonfile/gitrun " + task_id + " " + user_id;
        System.out.println(command);
        try {
            Process p = Runtime.getRuntime().exec(command);
            InputStream is = p.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            p.waitFor();
            InputStream er = p.getErrorStream();
            BufferedReader erreader = new BufferedReader(new InputStreamReader(er));

            String s = null;
            if (p.exitValue() != 0) {
                System.out.println("非正常终止");

                System.out.println("错误信息");
                while ((s = erreader.readLine()) != null) {
                    System.out.println(s);
                }
                System.out.println("输出信息");
                while ((s = reader.readLine()) != null) {
                    System.out.println(s);
                }
                return null;
            }

            s = reader.readLine();
            s = s.replace("'", "\"");
            System.out.println(s);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(s);

            Map m1 = new HashMap();
            try{
                if (root.path("detail").toString().equals("\"\"")){
                    m1.put("detail", "");
                }
                else {
                    m1.put("detail", root.findValue("detail").toString());
                }

            } catch (Exception e){
                m1.put("detail", root.findValue("detail").toString());
            }

            m1.put("verdict", root.findValue("verdict"));
            m1.put("comment", root.findValue("comment"));
            m1.put("score", root.findValue("score"));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonStr = mapper.readTree(objectMapper.writeValueAsString(m1));
            System.out.println(jsonStr);
            return jsonStr;

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
