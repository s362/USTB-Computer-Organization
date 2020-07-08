package com.example.ustbdemo.Util;

public class PathUtil {
    public static String toUrlPath(String originPath){
        String urlPath;
        if(originPath == null) return null;
        if(OSUtil.isLinux()){
            urlPath =  originPath.replace(FileUtil.STATIC_PATH_LINUX, "");
            urlPath =  urlPath.replace(FileUtil.FILE_PATH_LINUX, "");
        }
        else{
            urlPath = originPath.replace(FileUtil.STATIC_PATH_WIN, "");
            urlPath = urlPath.replace(FileUtil.FILE_PATH_WIN, "");
        }
        urlPath = "49.232.207.151:8080/" + urlPath;
        return urlPath;
    }
}
