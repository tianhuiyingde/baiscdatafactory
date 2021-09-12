package com.aixuexi.ss.common.dns;

import java.util.Properties;

/**
 * @author wangyangyang
 * @date 2020/10/27 11:33
 * @description
 **/
public class PropertiesUtils {


    public static Properties getProperties(String proName){
        return PropertiesHandler.getProperties(proName);
    }
}
