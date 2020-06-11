package com.example.gitlabdemo;

import org.junit.Test;

import java.io.File;

public class GitTest {
//    @Test
//    public void testFile(){
//        String dirNmae =  "C:\\Users\\bearking\\Desktop\\USTB_DEMO\\";
////        File file = new File("")
//    }


//    @Test
//    public void deleteProject() throws  Exception{
//        GitProcess gitProcess = new GitProcess();
//        gitProcess.getGitLabApi().getProjectApi().deleteProject(7483);
//    }

    @Test
    public void createFold() throws Exception{
//        GitProcess gitProcess = new GitProcess();
//        RepositoryFile repositoryFile = new RepositoryFile();
//        repositoryFile.setFilePath("example/task.config");
//        repositoryFile.setFileName("task.config");
//        repositoryFile.setContent("lalala");
//        gitProcess.getGitLabApi().getRepositoryFileApi().createFile(7479, repositoryFile, "master", "update");
//        GitProcess gitProcess = new GitProcess();
//        RepositoryFile repositoryFile =  gitProcess.getGitLabApi().getRepositoryFileApi().getFile(7481, "example/b.v", "master");
//        System.out.println(repositoryFile.getFileName());
//        gitProcess.getGitLabApi().getProjectApi().getProject(7481);
//        System.out.println(gitProcess.getGitLabApi().getRepositoryApi().getTree(7481));;
//        System.out.println(gitProcess.getGitLabApi().getProjectApi().getProject(7481));
//        System.out.println(gitProcess.getGitLabApi().getRepositoryApi().getTree(7481, "example", "master"));
    }

//    @Test
//    public void getVoidProject() throws Exception{
//        GitProcess gitProcess = new GitProcess();
//        System.out.println(gitProcess.getRepositoryFiles(2491));
//        if(gitProcess.getRepositoryFiles(2491).isEmpty()){
//            System.out.println("lala");
//        }
//        if(gitProcess.getRepositoryFiles(2491) == null){
//            System.out.println(234242);
//        } else {
//            System.out.println(56565);
//        }
//    }

//    @Test
//    public void getContent() throws Exception{
//        String path = "C:\\Users\\bearking\\Desktop\\ide作业备份\\01_vector介绍\\content.txt";
//        File f = new File(path);
//        FileInputStream in = new FileInputStream(f);
//        byte[] filecontent = new byte[(int)f.length()];
//        in.read(filecontent);
//        String fcontent = new String(filecontent, "UTF-8");
//        in.close();
////        System.out.println(fcontent);
////        System.out.println();
//        System.out.println(Base64Convert.strConvertBase(fcontent));
//        Map m1 = new HashMap();
//        m1.put("task_title", "lala");
//        m1.put("task_content", Base64Convert.strConvertBase(fcontent));
//        System.out.println(m1);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String taskInfo = objectMapper.writeValueAsString(m1);
//        System.out.println(taskInfo);
//
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode root = mapper.readTree(taskInfo);
//        System.out.println(root.findValue("task_content").asText());
//        System.out.println(Base64Convert.baseConvertStr(root.findValue("task_content").asText()));
//    }
//
//    public  String  handleImg(String str, String path) throws Exception{
//        String regex = "\\*#\\[(.+?)]#\\*";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(str);
//        StringBuffer sb = new StringBuffer();
//        System.out.println(path);
//        String qid;
//        String taskid;
//        if(OSUtil.isLinux()) {
//            String []strings = path.split("/");
//            qid = strings[strings.length-2];
//            taskid = strings[strings.length-1];
//            System.out.println(qid + "  " + taskid);
//        } else {
//            qid = "33";
//            taskid = "343";
//            System.out.println(qid + "  " + taskid);
//        }
//
//        while (matcher.find()) {
//            String imgName = matcher.group(0).substring(3, matcher.group(0).length()-3);
//            String imgPah = OSUtil.isLinux()? "/OjFiles/" + qid + "/" + taskid + "/images/" + imgName + ".png" : path + "\\images\\" + imgName + ".png";
//            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
////            System.out.println(imgPah);
//            imgPah =  "<div align=center><img src=" + "\"" + "https://ide.eustb.com" + imgPah + "\"" + " width = \"80%\"></div>";
//            System.out.println(imgPah);
//            matcher.appendReplacement(sb, imgPah);
//        }
//        matcher.appendTail(sb);
//        return sb.toString();
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

//    @Test
//    public void deleteProject() throws  Exception{
//        GitProcess gitProcess = new GitProcess();
//        int []ids = new int[]{6843,6844,6848,6849,6853,6854};
//        for(int i = 0; i < 6; i++){
//            gitProcess.getGitLabApi().getGroupApi().deleteGroup("t" + ids[i]);
//            System.out.println("delete "  + ids[i] + "success");
//        }
//
//    }

}
