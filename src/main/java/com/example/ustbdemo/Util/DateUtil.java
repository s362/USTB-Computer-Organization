package com.example.ustbdemo.Util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date getNowDate(String stringDate){
        System.out.println(stringDate);
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowDate = formatter.parse(stringDate);
            return nowDate;
        } catch (Exception e){
            return null;
        }

    }
}
