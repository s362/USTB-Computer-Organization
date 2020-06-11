package com.example.ustbdemo.Util;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
    public static final String FILE_PATH_LINUX = "/home/ustbDemo/taskFiles/";
    public static final String FILE_PATH_WIN = "C:\\Users\\bearking\\Desktop\\USTB_DEMO\\";
    private static final int BUFFER_SIZE = 1024;

//    接受上传的文件，文件夹名称为question_id
    public static String fileUpload(MultipartFile file, String filetype, Long tid) throws Exception{
        File dirFile, destFile;
        String dirpath = OSUtil.isLinux() ?  FILE_PATH_LINUX + tid.toString() + "/" + filetype + "/": FILE_PATH_WIN + tid.toString() + "\\" + filetype + "\\";
        dirFile = new File(dirpath);
        if(!dirFile.getParentFile().exists()) dirFile.getParentFile().mkdir();
        dirFile.mkdir();

//        如果文件是空的
        if (file == null) {
            System.out.println("未上传" + filetype + "文件");
            return null;
        }
        String filename = file.getOriginalFilename();
//        System.out.println(dirFile.getPath());
        String destPath = OSUtil.isLinux() ? dirFile.getPath() + "/" + filename : dirFile.getPath() + "\\" + filename;
        destFile = new File(destPath);

        //保存文件
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("传输文件失败");
        }
        String finalPath;
        if(filename.endsWith(".zip")){
            finalPath = OSUtil.isLinux() ? dirFile.getPath() + "/unzip/" : dirFile.getPath() + "\\unzip\\";
            unZip(destFile.getPath(), finalPath);
        } else {
            finalPath = destFile.getPath();
        }
        return finalPath;
    }

    public static void setTaskModelFiles(List<TaskFile> taskFileList, String files_path) throws  Exception{
        File f = new File(files_path);
        if (!f.exists()) {
//            throw new Exception(files_path + " not exists");
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
        System.out.println("deleteFileByTid  " + filePath + "   ");
    }

    public static void deleteDirectory(String dirPath)
    {
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
    }
}
