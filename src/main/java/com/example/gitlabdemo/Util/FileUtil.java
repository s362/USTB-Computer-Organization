package com.example.gitlabdemo.Util;
import com.example.gitlabdemo.Model.GitModel.TaskFile;
import com.example.gitlabdemo.Model.GitModel.TaskModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
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

    /**
     * 接收文件,文件名为 当前时间.zip
     * @param file
     * @return 接收的文件路径
     * @throws Exception
     */
    public static String fileUpload(MultipartFile file) throws Exception{
        String path;
        File dest;
        System.out.println(file);

        if (file.isEmpty()) {
            throw new Exception("文件为空");
        }
        String filename = file.getOriginalFilename();
        long fileSize = file.getSize();
        System.out.println("文件名称" + filename + "-------文件大小" + fileSize);

        path = OSUtil.isLinux() ?  FILE_PATH_LINUX + new Date().getTime() : FILE_PATH_WIN  + new Date().getTime();
        System.out.println(path);
        dest = new File(path + ".zip");
        if (!dest.getParentFile().exists()) {
            //父目录不存在就创建一个
            dest.getParentFile().mkdir();
        }

        //开始传输
        try {
            file.transferTo(dest);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("传输文件失败");
        }
    }


    /**
     * 解压接收的文件
     * @param path 文件路径
     * @throws RuntimeException
     */
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

    /**
     * 创建题目对象
     * @param taskModel 引用传递，题目对象
     * @param path 上传的题目的路径
     * @throws Exception
     */
    public static void createTaskModel(TaskModel taskModel, String path) throws Exception{
        System.out.println("isLinux?" + OSUtil.isLinux());
//        读取content.txt文件内容
        String contentPath = OSUtil.isLinux() ? path + "/content.txt" : path + "\\content.txt";
//        将图片路径转换为服务器下的绝对路径
        String contentImg = handleImg(getContent_Line(contentPath), path);
        taskModel.setTask_content(contentImg);
        taskModel.setTaskFiles(new LinkedList<TaskFile>());
        String files_path = OSUtil.isLinux() ? path + "/files" : path + "\\files";
        File f = new File(files_path);
        if (!f.exists()) {
            throw new Exception(path + " not exists");
        }
//        遍历文件夹下的所有文件（包括.v和config.json）
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

    /**
     * 获取文件内容
     * @param path
     * @return
     * @throws Exception
     */
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

    /**
     * 获取文件内容，并将空格和回车进行替换
     * TODO
     * 测试这里如果不替换可不可以
     * @param path
     * @return
     * @throws Exception
     */
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
            if (str!= ""){
                str = str.replace(" ", "&nbsp;");
                fcontent += str + "  <br />  ";
            }
        }
        bf.close();
        inputReader.close();
        return fcontent;
    }

    /**
     * 处理图片，将content中的*#[]#*宏替换成图片路径
     * @param str content内容
     * @param path 图片路径
     * @return
     * @throws Exception
     */
    public static String  handleImg(String str, String path) throws Exception{
        String regex = "\\*#\\[(.+?)]#\\*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        System.out.println(path);
        String qid;
        String tid;
        if(OSUtil.isLinux()) {
            String []strings = path.split("/");
            qid = strings[strings.length-2];
            tid = strings[strings.length-1];
            System.out.println(qid + "  " + tid);
        } else {
            String []strings = path.split("\\\\");
//            qid = strings[strings.length-2];
            tid = strings[strings.length-1];
            System.out.println(tid);
        }

        while (matcher.find()) {

            String imgName = matcher.group(0).substring(1, matcher.group(0).length()-1);
            String imgPah = OSUtil.isLinux()? "/OjFiles/" + tid + "/images/" + imgName + ".png" : path + "\\images\\" + imgName + ".png";
            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
//            System.out.println(imgPah);
            imgPah =  "<div align=\"center\"><img src=" + "\"" + "https://ide.eustb.com" + imgPah + "\"" + " width=\"100%\"></div>";
            System.out.println(imgPah);
            matcher.appendReplacement(sb, imgPah);
        }
        matcher.appendTail(sb);
        return Base64Convert.strConvertBase(sb.toString());
    }


    /**
     * 根据文件来判断文件的编码方式
     * @param fileName 文件名称
     * @return
     * @throws Exception
     */
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
