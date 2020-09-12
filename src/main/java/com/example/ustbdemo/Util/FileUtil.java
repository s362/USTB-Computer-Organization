package com.example.ustbdemo.Util;

import com.csvreader.CsvWriter;
import com.example.ustbdemo.Model.DataModel.Task;
import com.example.ustbdemo.Model.GitModel.TaskFile;
import com.example.ustbdemo.Model.GitModel.TaskModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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

        //创建父文件夹
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
        String regex = "!\\[]\\((.+?)\\)";   //![](images/image026.png)  要找出来的是这种格式的字符串
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String imgName = matcher.group(0).substring(11, matcher.group(0).length()-1);
//            String imgPah = OSUtil.isLinux()? STATIC_PATH_LINUX + tid + "/" + imgName : STATIC_PATH_WIN + tid + "\\" + imgName;
//            if (!OSUtil.isLinux())imgPah = imgPah.replace("\\", "\\\\");
//            imgPah =  "<div align=center><img src=" + "\"" + "https://49.232.207.151:8080" + imgPah + "\"" + " width = \"80%\"></div>";
            String imgPah = OSUtil.isLinux()? tid + "/" + imgName : tid + "\\" + imgName;
            if (!OSUtil.isLinux()) imgPah = imgPah.replace("\\", "\\\\");
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

//    public static void

    /**
     *复制，如果是zip包就解压,适用于Verilog编程题的压缩包
     * @param srcFilePath 原始路径
     * @param task 目标题目
     * @return 最终路径
     */
    public static String copyAndUnzipFile(String srcFilePath,Task task){
        //父目录
        String dirPath=OSUtil.isLinux()?FileUtil.FILE_PATH_LINUX+'/'+task.getTid():FileUtil.FILE_PATH_WIN+"\\"+task.getTid();

        //文件目录
        String decFilePath=dirPath+(OSUtil.isLinux()?srcFilePath.substring(srcFilePath.lastIndexOf('/')):srcFilePath.substring(srcFilePath.lastIndexOf("\\")));

        File dirFile=new File(dirPath);
        if (!dirFile.exists()) dirFile.mkdirs();   //若父文件夹不存在，则创建

        copyFile(srcFilePath,decFilePath); //将原题复制一份作为新题
        task.setTaskFilePath(decFilePath);
        String finalPath;
        if(decFilePath.endsWith(".zip")){
            finalPath = OSUtil.isLinux() ? dirFile.getPath() + "/unzip/" : dirFile.getPath() + "\\unzip\\";
            unZip(srcFilePath, finalPath);
        } else {
            finalPath =srcFilePath;
        }
        return finalPath;
    }

    /**
     * 复制原题文件到新的位置，适用于汇编仿真题
     * @param srcFilePath 原文件路径
     * @param task 题目信息
     * @param fileType 文件类型
     * @return 新文件路径
     */
    public static String copySimulationFile(String srcFilePath,Task task,String fileType){

        String dirPath=OSUtil.isLinux()?FILE_PATH_LINUX+task.getTid()+"/"+fileType:FILE_PATH_WIN+task.getTid()+"\\"+fileType;

        File dirFile=new File(dirPath);
        if (!dirFile.exists()) dirFile.mkdirs();

        File srcFile=new File(srcFilePath);

        String decFilePath=dirPath+(OSUtil.isLinux()?"/":"\\")+srcFile.getName();
        copyFile(srcFilePath,decFilePath);
        if (fileType.equals("exampleFile"))
            task.setExampleFilePath(decFilePath);
        else
            task.setTaskFilePath(decFilePath);
        return decFilePath;
    }

    //在每次学生提交代码后将成绩写入csv文件中,便于CG平台取出
    public static void saveCSVFile(String name,Long tid,Integer grade){

        String dirs=OSUtil.isLinux()?"/home/ustbDemo/grades/t"+tid:"E:/GIT/t"+tid;
        File dir=new File(dirs);
        try {
            if (!dir.exists()){
                dir.mkdirs();
            }
        }catch (Exception e){
            System.out.println("新建文件夹失败");
        }
        String saveFile = dirs+"/grade.csv";
//        try {
//            // 创建CSV写对象
//            CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName("GBK"));
//            //CsvWriter csvWriter = new CsvWriter(filePath);
//
//            // 写表头
////            String[] headers = {"id","得分","评语"};
//            String[] content = {name,grade.toString(),null};
////            csvWriter.writeRecord(headers);
//            csvWriter.writeRecord(content);
//
//            csvWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String saveFile = filePath;
        File file = new File(saveFile);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            if (!file.exists()) {
                boolean hasFile = file.createNewFile();
                if(hasFile){
                    System.out.println("file not exists, create new file");
                }
                fos = new FileOutputStream(file);
            } else {
                System.out.println("file exists");
                fos = new FileOutputStream(file, true);
            }

            osw = new OutputStreamWriter(fos, "GBK");
            String content = name+','+grade.toString()+',';
            osw.write(content); //写入内容
            osw.write("\r\n");  //换行
        } catch (Exception e) {
            e.printStackTrace();
        }finally {   //关闭流
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量导出题目时使用，将题目信息对应的json文件保存到本地
     * @param tid 题目id
     * @param taskJson 题目信息对应的json文件
     */
    public static void saveTaskJson(Long tid, Map taskJson){
        String dirPath=OSUtil.isLinux()?FILE_PATH_LINUX+tid+"/":FILE_PATH_WIN+tid+"\\";
        File dirFile=new File(dirPath);
        if (!dirFile.exists()) dirFile.mkdirs();

        ObjectMapper objectMapper=new ObjectMapper();
        JsonNode content=objectMapper.convertValue(taskJson,JsonNode.class);

        String filePath=dirPath+"taskJson.json";
        try {
            File jsonFile=new File(filePath);
            FileOutputStream fos = null;
            OutputStreamWriter osw = null;
            if (!jsonFile.exists()) {
                boolean hasFile = jsonFile.createNewFile();
                if(hasFile){
                    System.out.println("file not exists, create new file");
                }
                fos = new FileOutputStream(jsonFile);
            } else {
                System.out.println("file exists");
                fos = new FileOutputStream(jsonFile);
            }
            osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            System.out.println(content.toString());
            osw.write(content.toString()); //写入内容
            osw.write("\r\n");  //换行
            osw.flush();
            osw.close();
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
//            throw new
        }
    }



    /**
     * 将pathlist中的路径所指向的文件都打成一个压缩包，包路径为/home/ustbDemo/taskFiles/fileName.zip,然后直接输出到客户端，下载完之后删除压缩包
     * @param resp 向客户端返回数据的数据流
     * @param pathList /home/ustbDemo/taskFiles/中需要打包的文件夹名称
     * @param fileName 压缩后的文件名称
     */
    public static void  fileToZip(HttpServletResponse resp,List<Long> pathList, String fileName){
        try {

            String zipFilePath=(OSUtil.isLinux()?FILE_PATH_LINUX:FILE_PATH_WIN)+File.separator;

            //创建zip输出流
            ZipOutputStream out = new ZipOutputStream( new FileOutputStream(zipFilePath+fileName+".zip"));

            //创建缓冲输出流
            BufferedOutputStream bos = new BufferedOutputStream(out);

            for (Long file:pathList) {
                File sourceFile = new File((OSUtil.isLinux()?FILE_PATH_LINUX:FILE_PATH_WIN)+File.separator+file);
                //调用函数遍历文件夹内容
                compress(out, bos, sourceFile, sourceFile.getName());
            }
            bos.close();
            out.close();
            System.out.println("压缩完成");

            // 指定文件的保存类型。
            resp.setContentType("application/zip;charset=utf-8");

            resp.setHeader("Content-disposition", "attachment; filename="+ new String( fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8) +".zip");
            ServletOutputStream oupstream = resp.getOutputStream();
            FileInputStream from = new FileInputStream(zipFilePath+fileName+".zip");
            byte[] buffer = new byte[1024];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1) {
                oupstream.write(buffer, 0, bytes_read);
            }

            //关掉输入输出流之后把压缩文件从系统中彻底删除
            //提示：如果输入输出流没关闭，那么文件会被占用无法删除
            oupstream.flush();
            oupstream.close();
            from.close();
            deleteDirectory(zipFilePath+fileName+".zip");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 将文件打包成压缩包
     * @param pathList 原文件路径或者文件夹路径
     * @param fileName 目标压缩文件完整路径
     */
    public static void zipFile(List<String> pathList,String fileName){
        try {
            //创建zip输出流
            ZipOutputStream out = new ZipOutputStream( new FileOutputStream(fileName));
            //创建缓冲输出流
            BufferedOutputStream bos = new BufferedOutputStream(out);
            for (String file:pathList) {
                File sourceFile = new File(file);
                if (!sourceFile.exists()) continue;
                //调用函数遍历文件夹内容
                compress(out, bos, sourceFile, sourceFile.getName());
            }
            bos.close();
            out.close();
            System.out.println("压缩完成"+fileName);
        }catch (Exception e){
            System.out.println("压缩失败");
        }
    }


    //递归遍历每个文件/文件夹，若是文件则打包，若不是则递归调用，将对应整个目录树到添加到zip文件中
    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if(sourceFile.isDirectory())
        {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            if(flist.length==0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                System.out.println(base+"/");
                out.putNextEntry(  new ZipEntry(base+"/") );
            }
            else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for(int i=0;i<flist.length;i++)
                {
                    compress(out,bos,flist[i],base+"/"+flist[i].getName());
                }
            }
        }
        else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry( new ZipEntry(base) );
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            //将源文件写入到zip文件中
            while((tag=bis.read())!=-1)
            {
                out.write(tag);
            }
            bis.close();
            fos.close();
        }
    }

    /**
     * 将上传的题目文件传送到taskFiles目录下，然后解压到taskFiles/tasks/目录下，将上传的压缩包删除
     * @param zipFile  上传的题目导出文件
     * @param destName  解压到的目的目录
     * @return  解压后的文件夹路径
     */
    public static String zipFileUploadAndUnzip(MultipartFile zipFile,String destName) throws Exception {
        //        如果文件是空的
        if (zipFile == null || zipFile.isEmpty()) {
            System.out.println("未上传" + "文件");
            return null;
        }
        String zipPath=(OSUtil.isLinux()?FILE_PATH_LINUX:FILE_PATH_WIN);  //压缩包上传路径
        String unzipPath=zipPath+destName+File.separator;  //压缩包解压文件夹
        File dirFile=new File(unzipPath);
        if (!dirFile.exists()) dirFile.mkdirs();  //若解压目录不存在则新建
        else {
            deleteDirectory(unzipPath);//若已存在则先删除，为了避免原文件目录中的残留文件导致问题产生
            dirFile.mkdirs();
        }

        String zipFilePath=zipPath+destName+".zip";  //压缩包的上传全路径

        try {
            File file=new File(zipFilePath);
            if (file.exists()) throw new Exception("目标文件已存在");

            zipFile.transferTo(new File(zipFilePath));
        }catch (Exception e){
            throw  new Exception("文件上传失败");
        }

        try {
            unZip(zipFilePath,unzipPath);
        }catch (Exception e){
            throw  new Exception("文件解压失败");
        }
        deleteDirectory(zipFilePath);  //解压完之后将压缩包删除
        return unzipPath;
    }

    /**
     * 对每个题目对应的taskJson文件进行提取，
     * @param taskFile 题目对应的文件夹
     * @return 读取出的json格式数据
     */
    public static JsonNode fetchTaskJson(File taskFile) throws Exception {
        if (!taskFile.exists()) throw new Exception("文件夹不存在");
        String taskJsonPath=taskFile.getPath()+File.separator+"taskJson.json";
        File jsonFile=new File(taskJsonPath);
        if (!jsonFile.exists()) throw new Exception("taskJson文件不存在");
        //将taskjsonfile中的内容读取成json格式
        FileInputStream in=new FileInputStream(jsonFile);
        byte[] filecontent = new byte[(int)jsonFile.length()];
        in.read(filecontent);
        String fcontent = new String(filecontent, StandardCharsets.UTF_8);
        in.close();
        return new ObjectMapper().readTree(fcontent);
    }

    /**
     * 用于对导入的压缩包中汇编仿真题的图片进行重命名并保存到静态文件夹
     * @param srcFilePath 原图片路径
     * @param task 对应题目的task
     * @param type 该图片是第一张还是第二张
     */
    public static void copyPicture(String srcFilePath, Task task, int type){
        UUID uuid = UUID.randomUUID();
        String uuidName = uuid.toString();
        String destFilePath=(OSUtil.isLinux()?STATIC_PATH_LINUX:STATIC_PATH_WIN)+uuidName+srcFilePath.substring(srcFilePath.lastIndexOf("."));
        copyFile(srcFilePath,destFilePath);
        if (type==1) task.setSimuPicPath1(destFilePath);
        else task.setSimuPicPath2(destFilePath);
    }
}
