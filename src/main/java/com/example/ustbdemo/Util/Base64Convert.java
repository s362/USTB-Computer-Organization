package com.example.ustbdemo.Util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Convert {

    public static String strConvertBase(String str) {
        if(null != str){
            try{
                return org.apache.commons.codec.binary.Base64.encodeBase64String(str.getBytes("UTF-8"));
            }
            catch (UnsupportedEncodingException e){
                return null;
            }

        }
        return null;
    }

    public static String baseConvertStr(String str) {
        if(null != str){
            try {
                return new String(Base64.decodeBase64(str));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
