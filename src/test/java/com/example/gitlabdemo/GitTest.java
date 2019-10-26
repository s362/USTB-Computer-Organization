package com.example.gitlabdemo;

import com.example.gitlabdemo.Util.GitProcess;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GitTest {

//    @Test
//    public void getAllproject() throws Exception{
//        GitProcess gitProcess = new GitProcess();
//        System.out.println(gitProcess.getGitLabApi().getProjectApi().getOwnedProjects());
//    }

//    @Test
//    public void testDate(){
//        String path = "C:\\Users\\bearking\\Desktop\\lala\\test";
//        File f = new File(path);
//        System.out.println(f.getName());
//        File fa[] = f.listFiles();
//        System.out.println(fa[0].getParentFile().getPath());
//        for(int i = 0; i < fa.length; i++) {
//            System.out.println(fa[i].getName());
//            fa[i].renameTo(new File(fa[i].getParentFile().getParentFile().getPath() + "\\" + fa[i].getName()));
//        }
//        System.out.println(new Date().toString());
//    }

//    @Test
//    public void deleteGroup()throws Exception{
//        GitProcess gitProcess = new GitProcess();
//        for(int i = 2; i < 50; i++){
//            try {
//                gitProcess.getGitLabApi().getGroupApi().deleteGroup("t" + i);
//                System.out.println("delete " + i  + " 成功");
//            }catch (Exception e){
//                System.out.println("t" + i + "工程不存在");
//            }
//        }
//    }


//    @Test
//    public void testJSON() throws Exception{
//        String str = "{\"detail\": \"{\\\"foot\\\": {\\\"tock\\\": \\\"1 2 3 4 5 6 7 \\\"}, \\\"config\\\": {\\\"hscale\\\": 1}, \\\"signal\\\": [[\\\"Input\\\", {\\\"name\\\": \\\"in\\\", \\\"wave\\\": \\\"x010101\\\"}], {}, [\\\"out\\\", {\\\"name\\\": \\\"REF\\\", \\\"wave\\\": \\\"x101010\\\"}, {\\\"name\\\": \\\"YOURS\\\", \\\"wave\\\": \\\"x101010\\\"}], {\\\"name\\\": \\\"mismatch\\\", \\\"wave\\\": \\\"0......\\\"}, {}]}\", \"verdict\": \"pass\", \"score\": \"100\", \"comment\": \"\"}";
//        String s = str;
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode root = mapper.readTree(s);
//        System.out.println(root.findValue("detail").asText());
//        System.out.println(root);
//
//        Map m1 = new HashMap();
//        try{
//            if (root.path("detail").toString().equals("\"\"")){
//                m1.put("detail", "");
//            }
//            else {
//                m1.put("detail", root.findValue("detail").toString().replace("\\", ""));
//            }
//
//        } catch (Exception e){
//            m1.put("detail", root.findValue("detail").toString());
//        }
//
//        m1.put("verdict", root.findValue("verdict"));
//        m1.put("comment", root.findValue("comment"));
//        m1.put("score", root.findValue("score"));
//
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonStr = mapper.readTree(objectMapper.writeValueAsString(m1));
//        System.out.println(jsonStr);    }

}
