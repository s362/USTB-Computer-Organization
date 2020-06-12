package com.example.ustbdemo.Util;

public class PathUtil {
    public static String toUrlPath(String originPath){
        String urlPath;
        if(OSUtil.isLinux()){
            urlPath =  originPath.replace(FileUtil.STATIC_PATH_LINUX, "");
        }
        else{
            urlPath = originPath.replace(FileUtil.STATIC_PATH_WIN, "");
        }
        urlPath = "123.56.0.67:8080/" + urlPath;
        return urlPath;
    }
}
