package com.aixuexi.ss.common.util;

import java.util.UUID;

/**
 * @Author: wangyangyang
 * @Date: 2019/12/30 16:12
 */
public class MethodHelper {
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }
    public static String className(String name){
        String imgUrl = "http://127.0.0.1:8080/cms/ReadAddress/1479805098158.jpg";
        String image = imgUrl.substring(imgUrl.lastIndexOf(".")+1);
        System.out.println(image);
        return name;
    }
    public static void main(String [] args){
        className("ni");
    }
}
