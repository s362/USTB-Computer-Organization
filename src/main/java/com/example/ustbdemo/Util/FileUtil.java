package com.example.ustbdemo.Util;

import com.csvreader.CsvWriter;
import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Model.GitModel.TaskFile;
import com.example.ustbdemo.Model.GitModel.TaskModel;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
    public static final String FILE_PATH_LINUX = "/home/ustbDemo/taskFiles/";
    public static final String STATIC_PATH_LINUX = "/home/ustbDemo/static/";
    public static final String INITIAL_PATH_LINUX = "/home/ustbDemo/initialFiles/";
//    public static final String FILE_PATH_WIN = "C:\\Users\\bearking\\Desktop\\USTB_DEMO\\taskFiles\\";
//    public static final String STATIC_PATH_WIN = "C:\\Users\\bearking\\Desktop\\USTB_DEMO\\staticFiles\\";
//    public static final String INITIAL_PATH_WIN = "C:\\Users\\bearking\\Desktop\\USTB_DEMO\\initialFiles\\";
    public static final String FILE_PATH_WIN = "F:\\D_disk\\ustbdemo\\taskFiles\\";
    public static final String STATIC_PATH_WIN = "F:\\D_disk\\ustbdemo\\staticFiles\\";
    public static final String INITIAL_PATH_WIN = "F:\\D_disk\\ustbdemo\\initialFiles\\";
    private static final int BUFFER_SIZE = 1024;

//    接受上传的文件，文件夹名称为question_id
    public static String fileUpload(MultipartFile file, Task task, String fileType, String destFilename) throws Exception{
        File dirFile, destFile;
        String dirpath = "";
        dirpath = OSUtil.isLinux() ?  FILE_PATH_LINUX + task.getTid().toString() + "/" + fileType : FILE_PATH_WIN + task.getTid().toString() + "\\" + fileType;

        dirFile = new File(dirpath);
        if(!dirFile.getParentFile().getParentFile().exists()) dirFile.getParentFile().getParentFile().mkdir();
        if(!dirFile.getParentFile().exists()) dirFile.getParentFile().mkdir();
        dirFile.mkdir();

//        如果文件是空的
        if (file == null || file.isEmpty()) {
            System.out.println("未上传" + "文件");
            return null;
        }
        String filename = "";
        if(destFilename.equals("")){
            filename = file.getOriginalFilename();
        } else{
            filename = destFilename;
        }

        String destPath = OSUtil.isLinux() ? dirFile.getPath() + "/" + filename : dirFile.getPath() + "\\" + filename;
        destFile = new File(destPath);

        //保存文件
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("传输文件失败");
        }
        if (fileType.equals("exampleFile"))
            task.setExampleFilePath(destFile.getPath());
        else
            task.setTaskFilePath(destFile.getPath());

        String finalPath;
        if(filename.endsWith(".zip")){
            finalPath = OSUtil.isLinux() ? dirFile.getPath() + "/unzip/" : dirFile.getPath() + "\\unzip\\";
            unZip(destFile.getPath(), finalPath);
        } else {
            finalPath = destFile.getPath();
        }
        return finalPath;
    }

    public static String saveStaticUploadFile(MultipartFile file){
        if(file == null || file.isEmpty()) return null;
        UUID uuid = UUID.randomUUID();
        String uuidName = uuid.toString();
        String fileNameEx = file.getOriginalFilename();
        if(fileNameEx.lastIndexOf(".") != -1){
            fileNameEx = fileNameEx.substring(fileNameEx.lastIndexOf("."), fileNameEx.length());
            uuidName += fileNameEx;
        }
        String filePath = OSUtil.isLinux()? STATIC_PATH_LINUX + uuidName : STATIC_PATH_WIN + uuidName;
        File destFile = new File(filePath);
        try {
            file.transferTo(destFile);
            return destFile.getPath();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void setTaskModelFiles(List<TaskFile> taskFileList, String files_path) throws  Exception{
        File f = new File(files_path);
        if (!f.exists()) {
            return;
        }
        if(f.isDirectory()){
            File fa[] = f.listFiles();
            for(int i = 0; i < fa.length; i++){
                File fs = fa[i];
                taskFileList.add(getTaskFile(fs));
            }
        }else{
            taskFileList.add(getTaskFile(f));
        }
    }

    public static TaskFile getTaskFile(File file) throws Exception{
        FileInputStream in = new FileInputStream(file);
        byte[] filecontent = new byte[(int)file.length()];
        in.read(filecontent);
        String fcontent = new String(filecontent, "UTF-8");
        TaskFile taskFile = new TaskFile();
        taskFile.setTitle(file.getName());
        taskFile.setContent(fcontent);
        in.close();
        return taskFile;
    }

    public static String setMdContent(Long tid, String mdPath) throws Exception{
        TaskFile taskFile = getTaskFile(new File(mdPath));
//        System.out.println("处理 md 文件");
        return handleImg(tid, taskFile.getContent());
    }

    public static void moveTaskImg(Long tid, String imgPath){
        File file = new File(imgPath);
        file.renameTo(new File(OSUtil.isLinux()? STATIC_PATH_LINUX + tid : STATIC_PATH_WIN + tid));
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

    public static String saveStaticLocalFile(File file){
        UUID uuid = UUID.randomUUID();
        String uuidName = uuid.toString();
        String fileNameEx = file.getName();
        if(fileNameEx.lastIndexOf(".") != -1){
            fileNameEx = fileNameEx.substring(fileNameEx.lastIndexOf("."), fileNameEx.length());
            uuidName += fileNameEx;
        }
        String filePath = OSUtil.isLinux()? STATIC_PATH_LINUX + uuidName : STATIC_PATH_WIN + uuidName;
        file.renameTo(new File(filePath));
        System.out.println(uuidName);
        return uuidName;
    }
    //    处理题目描述中的图片信息
//    因为后端储存题目描述用的是json，所以这里把字符进行了base64加密，便于存储。
    public static String  handleImg(Long tid, String str) throws Exception{
        String regex = "!\\[]\\((.+?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String imgName = matcher.group(0).substring(11, matcher.group(0).length()-1);
//            String imgPah = OSUtil.isLinux()? STATIC_PATH_LINUX + tid + "/" + imgName : STATIC_PATH_WIN + tid + "\\" + imgName;
//            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
//            imgPah =  "<div align=center><img src=" + "\"" + "https://49.232.207.151:8080" + imgPah + "\"" + " width = \"80%\"></div>";
            String imgPah = OSUtil.isLinux()? tid + "/" + imgName : tid + "\\" + imgName;
            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
            imgPah =  "<div align=center><img src=" + "\"" + "http://49.232.207.151:8080/" + imgPah + "\"" + " width = \"80%\"></div>";
            matcher.appendReplacement(sb, imgPah);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

//    解压zip文件
    public static void unZip(String filePath, String destPath) throws RuntimeException{
        File destFile = new File(destPath);
        if(!destFile.exists()) destFile.mkdir();

        File srcFile = new File(filePath);

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
                    String dirPath  = OSUtil.isLinux()? destPath  + "/" + entry.getName() : destPath  + "\\" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                }else {
//                    如果是文件
                    String infilePath = OSUtil.isLinux()? destPath + "/" + entry.getName():destPath + "\\" + entry.getName();
                    File targetFile = new File(infilePath);
//                    System.out.println(infilePath);
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
//                    srcFile.delete();
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

    public static void deleteFileByTid(Long tid){
        String filePath = OSUtil.isLinux() ?  FILE_PATH_LINUX + tid.toString(): FILE_PATH_WIN + tid.toString();
        deleteDirectory(filePath);
        String imgPath = OSUtil.isLinux() ?  STATIC_PATH_LINUX + tid.toString(): STATIC_PATH_WIN + tid.toString();
        deleteDirectory(imgPath);
        System.out.println("deleteFileByTid  " + filePath + "   ");
    }

    public static void deleteDirectory(String dirPath) {
        try{
            File file = new File(dirPath);
            if(file.isFile())
            {
                file.delete();
            }else
            {
                File[] files = file.listFiles();
                if(files == null)
                {
                    file.delete();
                }else
                {
                    for (int i = 0; i < files.length; i++)
                    {
                        deleteDirectory(files[i].getAbsolutePath());
                    }
                    file.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.toString());
        }

    }

    public static void copyFile(String srcPathStr, String desPathStr)
    {
        try
        {
            FileInputStream fis = new FileInputStream(srcPathStr);//创建输入流对象
            FileOutputStream fos = new FileOutputStream(desPathStr); //创建输出流对象
            byte datas[] = new byte[1024*8];//创建搬运工具
            int len = 0;//创建长度
            while((len = fis.read(datas))!=-1)//循环读取数据
            {
                fos.write(datas,0,len);
            }
            fis.close();//释放资源
            fis.close();//释放资源
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //在每次学生提交代码后将成绩写入csv文件中,便于CG平台取出
    public static void saveCSVFile(String name,Integer grade){
        String filePath = OSUtil.isLinux()?"/home/ustbDemo/grade.csv":"E:/GIT/test.csv";
        try {
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName("GBK"));
            //CsvWriter csvWriter = new CsvWriter(filePath);

            // 写表头
//            String[] headers = {"id","得分","评语"};
            String[] content = {name,grade.toString(),null};
//            csvWriter.writeRecord(headers);
            csvWriter.writeRecord(content);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
