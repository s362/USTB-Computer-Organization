package com.example.ustbdemo.Service;


import com.example.ustbdemo.Controller.LoginController;
import com.example.ustbdemo.Util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MysqlService {

    public static final Logger logger = LoggerFactory.getLogger(MysqlService.class);

//    //定时对数据库进行备份
//    @Scheduled(cron = "*/120 * * * * ?")//每60秒一次  测试用
////    @Scheduled(cron = "0 0 7 * * ?")//每天上午7:00进行备份
////    @Scheduled(cron = "0 0 7 ? * MON")//每周一上午7:00进行备份
//    public void saveData(){
//        logger.info("开始备份数据库");
//        String backName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
//        try{
//            dataBaseDump("localhost", "3306", "root", "root", "ustbdemo", backName);
//        }catch (Exception e){
//            logger.info(e.getMessage());
//            logger.info("备份数据库失败");
//        }
//    }
//
//    //mysqldump -hlocalhost -P3306 -uroot -p123456 db > E:/back.sql
//    //备份
//    public static void dataBaseDump(String host, String port, String username, String password, String databasename, String sqlname) throws Exception {
//        File file = new File(OSUtil.isLinux()?"/home/ustbDemo/mysqldata":"E:\\mysqltest");
//        if (!file.exists()) {
//            if(!file.mkdir()) throw new Exception("新建文件夹失败");
//        }
//        File datafile = new File(file + File.separator + sqlname + "-ustbdemo.sql");
//        if (datafile.exists()) {
//            logger.info(sqlname + "文件名已存在，请更换");
//            return;
//        }
//        //拼接cmd命令
//        String windowsCmd="cmd /c  mysqldump -h" + host + " -P" + port + " -u " + username + " -p" + password + " " + databasename + " > " + datafile;
//        String linuxCmd="/bin/sh -c mysqldump -h" + host + " -P" + port + " -u " + username + " -p" + password + " " + databasename + " > " + datafile;
//        logger.info("数据库备份命令开始执行"+"当前运行环境为"+(OSUtil.isLinux()?"linux":"windows"));
//        logger.info(OSUtil.isLinux()?linuxCmd:windowsCmd);
//        Process exec = Runtime.getRuntime().exec(OSUtil.isLinux()?linuxCmd:windowsCmd);
//        int value=exec.waitFor();
//        if (value == 0) {
//            logger.info("数据库备份成功,备份路径为：" + datafile);
//        }else {
//            logger.info(Integer.toString(value));
//            throw new Exception("数据库备份命令执行失败");
//        }
//    }


    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;

//    @Value("${sqlbackup.path}")
    private String sqlPath=OSUtil.isLinux()?"/home/ustbDemo/mysqldata/":"E:\\test\\";

    /**
     * 获取数据库名
     */
    public String getDataBaseName() {
        return url.substring(url.indexOf("3306"), url.indexOf("?")).replaceAll("/", "").replaceAll("3306", "");
    }

    /**
     * 获取主机地址
     */
    private String getHost() {
        return url.substring(url.indexOf("mysql"), url.indexOf("3306")).replace(":", "").replace("//", "").replace("mysql", "");
    }

    /**
     * 导出 sql 并返回相关信息
     */
    public void exportSql(String time) {
        // 指定导出的 sql 存放的文件夹
        File saveFile = new File(sqlPath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        String host = getHost();
        String dataBaseName = getDataBaseName();
        String fileName = time + "_" + dataBaseName+".sql";

        StringBuilder sb = new StringBuilder();
        // 拼接备份命令
        sb.append("mysqldump").append(" --opt").append(" -h ").append(host).append(" --user=").append(userName).append(" --password=").append(password);
        sb.append(" --result-file=").append(sqlPath + fileName).append(" --default-character-set=utf8 ").append(dataBaseName);

        try {
//            logger.info(sb.toString());
            Process exec = Runtime.getRuntime().exec(sb.toString());
            if (exec.waitFor() == 0) {
                logger.info("数据库备份成功，保存路径：" + sqlPath);
            } else {
                System.out.println("process.waitFor()=" + exec.waitFor());
            }
        } catch (IOException e) {
            logger.error("备份 数据库 出现 IO异常 ", e);
        } catch (InterruptedException e) {
            logger.error("备份 数据库 出现 线程中断异常 ", e);
        } catch (Exception e) {
            logger.error("备份 数据库 出现 其他异常 ", e);
        }
    }
}
