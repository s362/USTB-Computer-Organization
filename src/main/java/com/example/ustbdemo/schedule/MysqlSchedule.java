package com.example.ustbdemo.schedule;


import com.example.ustbdemo.Service.MysqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MysqlSchedule {

    private static final Logger log = LoggerFactory.getLogger(MysqlSchedule.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");

    @Autowired
    private MysqlService backUpDataBaseManager;

    /**
     * 每天下午4点50分30秒执行
     */
//    @Scheduled(cron = "30 50 16 * * ?")
//    @Scheduled(cron = "*/60 * * * * ?")
    @Scheduled(cron = "0 0 7 * * ?")//每天上午7:00进行备份
    public void reportCurrentTime() {
        String format = dateFormat.format(System.currentTimeMillis());
        log.info("The time is now {}", format);
        backUpDataBaseManager.exportSql(format);
    }
}
