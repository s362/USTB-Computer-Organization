package com.example.ustbdemo.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JudgeUtil {
//    接受用户id和待评测的题目id，调用docker，然后把返回结果保存为JsonNode格式。
    static public JsonNode shell(String task_id, String user_id) {
        String command = "sudo docker run --rm ustb/merge:v1 /home/docker/ide/gitrun " + task_id + " " + user_id;
        
        System.out.println(command);

        try {
            Process p = Runtime.getRuntime().exec(command);
            InputStream is = p.getInputStream();
            p.waitFor();

            if (p.exitValue() != 0) {
                String s;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                InputStream er = p.getErrorStream();
                BufferedReader erreader = new BufferedReader(new InputStreamReader(er));

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
            JsonNode jsonStr;

            jsonStr = dealOutput(is, "UTF-8");
//                jsonStr = dealOutput(is, "ISO-8859-1");
//            }
            if(jsonStr == null){
                throw new Exception("转换失败");
            }

            return jsonStr;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    static public JsonNode dealOutput(InputStream is, String charset){
        try {
            String s = null;
            ObjectMapper mapper = new ObjectMapper();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));

            s = reader.readLine();
            s= s.replace("\"", "\\\"").replace("'","\"").replace("\\x", "");

            JsonNode root = mapper.readTree(s);
            String detailStr = root.findValue("detail").asText();
            System.out.println(root.findValue("detail").asText());

            Map m1 = new HashMap();
            try{
                if (root.path("detail").toString().equals("\"\"")){
                    m1.put("detail", "");
                }
                else {
                    m1.put("detail", detailStr);
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
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    static public Map simulationOut(String code, Long cpu1, Long cpu2) throws Exception{
        String exep1 = "sudo docker run --rm mipssimulator:v9 /bin/bash /root/run.sh " + code + " " +cpu1+" "+cpu2;
        Process pro = null;

        pro = Runtime.getRuntime().exec(exep1);
        int status = pro.waitFor();
        if (status != 0)
        {
            System.out.println("Failed to call shell's command ");
            System.out.println("error messages");
            StringBuilder error = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getErrorStream(), "UTF-8"));
            while ((line = br.readLine()) != null) {
                error.append(line).append('\n');
            }
            System.out.println(error.toString());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        StringBuilder strbr = new StringBuilder();
        String line;
        while ((line = br.readLine())!= null)
        {
            strbr.append(line).append("\n");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map readValue = mapper.readValue(strbr.toString(), Map.class);
        return readValue;
    }
}
