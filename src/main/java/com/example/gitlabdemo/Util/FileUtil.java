package com.example.gitlabdemo.Util;
import com.example.gitlabdemo.Model.GitModel.TaskFile;
import com.example.gitlabdemo.Model.GitModel.TaskModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
    public static final String FILE_PATH_LINUX = "/home/ustb806/OjFiles/";
    public static final String FILE_PATH_WIN = "C:\\Users\\bearking\\Desktop\\lala\\";
    private static final int BUFFER_SIZE = 1024;

    public static String fileUpload(MultipartFile file, String question_id) throws Exception{
        String path;
        File dest;
        System.out.println(file);

        if (file.isEmpty()) {
            throw new Exception("请上传一个文件");
        }
        String filename = file.getOriginalFilename();
        long fileSize = file.getSize();
        System.out.println("文件名称" + filename + "-------文件大小" + fileSize);



        path = OSUtil.isLinux() ?  FILE_PATH_LINUX + question_id : FILE_PATH_WIN + question_id;
        System.out.println(path);
        dest = new File(path + ".zip");
        if (!dest.getParentFile().exists()) {
            //父目录不存在就创建一个
            dest.getParentFile().mkdir();
        }

        //保存文件
        try {
            file.transferTo(dest);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("传输文件失败");
        }
    }

    public static void createTaskModel(TaskModel taskModel, String path) throws Exception{
        System.out.println("isLinux?" + OSUtil.isLinux());
        String contentPath = OSUtil.isLinux() ? path + "/content.md" : path + "\\content.md";
        taskModel.setTask_content(handleImg(getContent(contentPath), path));
        taskModel.setTaskFiles(new LinkedList<TaskFile>());
        String files_path = OSUtil.isLinux() ? path + "/files" : path + "\\files";
        File f = new File(files_path);
        if (!f.exists()) {
            throw new Exception(path + " not exists");
        }
        File fa[] = f.listFiles();
        for(int i = 0; i < fa.length; i++){
            File fs = fa[i];
            FileInputStream in = new FileInputStream(fs);
            byte[] filecontent = new byte[(int)fs.length()];
            in.read(filecontent);
            String fcontent = new String(filecontent, "UTF-8");
            TaskFile taskFile = new TaskFile();
            taskFile.setTitle(fs.getName());
            taskFile.setContent(fcontent);
            taskModel.getTaskFiles().add(taskFile);
            in.close();
        }
    }

    public static String getContent(String path) throws Exception{
        File f = new File(path);
        if (!f.exists()) {
            throw new Exception(path + " not exists");
        }
        FileInputStream in = new FileInputStream(f);
        byte[] filecontent = new byte[(int)f.length()];
        in.read(filecontent);
        String fcontent = new String(filecontent, "UTF-8");
        in.close();
        return fcontent;
    }

    public static String getContent_Line(String path) throws Exception{
        File f = new File(path);
        if (!f.exists()) {
            throw new Exception(path + " not exists");
        }
        String fcontent = "";
        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(f));
        BufferedReader bf = new BufferedReader(inputReader);
        String str;
        while ((str = bf.readLine()) != null) {
            fcontent += str + "  <br />  ";
        }
        bf.close();
        inputReader.close();
        return fcontent;
    }

    public static String  handleImg(String str, String path) throws Exception{
        String regex = "!\\[]\\((.+?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        System.out.println(path);
        String qid;
        String taskid;
        if(OSUtil.isLinux()) {
            String []strings = path.split("/");
            qid = strings[strings.length-2];
            taskid = strings[strings.length-1];
            System.out.println(qid + "  " + taskid);
        } else {
            String []strings = path.split("\\\\");
            qid = strings[strings.length-2];
            taskid = strings[strings.length-1];
            System.out.println(qid + "  " + taskid);
        }

        while (matcher.find()) {
            String imgName = matcher.group(0).substring(11, matcher.group(0).length()-5);
            String imgPah = OSUtil.isLinux()? "/OjFiles/" + qid + "/" + taskid + "/images/" + imgName + ".png" : path + "\\images\\" + imgName + ".png";
            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
//            System.out.println(imgPah);
            imgPah =  "<div align=center><img src=" + "\"" + "https://ide.eustb.com" + imgPah + "\"" + " width = \"80%\"></div>";
            System.out.println(imgPah);
            matcher.appendReplacement(sb, imgPah);
        }
        matcher.appendTail(sb);
        return Base64Convert.strConvertBase(sb.toString());
    }

    public static void unZip(String path) throws RuntimeException{
        File srcFile = new File(path + ".zip");
//         文件不存在，抛出异常
        if(!srcFile.exists()){
            System.out.println("文件不存在");
            throw new RuntimeException(srcFile.getPath()+"文件不存在");
        }

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile, Charset.forName("GBK"));
//            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()){
                ZipEntry entry = (ZipEntry) entries.nextElement();

//                如果是文件夹
                if(entry.isDirectory()){
                    String dirPath  = OSUtil.isLinux()? path  + "/" + entry.getName() : path  + "\\" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                }else {
//                    如果是文件
                    String filePath = OSUtil.isLinux()? path + "/" + entry.getName():path + "\\" + entry.getName();
                    File targetFile = new File(filePath);
                    if(!targetFile.getParentFile().exists()){
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf))!= -1){
                        fos.write(buf,0,len);
                    }
                    fos.close();
                    is.close();
                }
            }
        } catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            throw new RuntimeException("errer", e);
        } finally {
            if(zipFile != null){
                try{
                    zipFile.close();
                    srcFile.delete();
                } catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        }
    }

    public static String codeString(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        bin.close();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }
}
