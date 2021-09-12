package com.aixuexi.ss.common.dns;

/**
 * @author wangyangyang
 * @date 2020/10/28 15:58
 * @description
 **/
public class ConfigUtil {
    private static PropertyReader propertyReader = null;
    private static ConfigUtil ins = new ConfigUtil();

    ConfigUtil() {
        propertyReader = new PropertyReader("env.properties");
    }

    public static String getString(String key) {
        return propertyReader.getValueAsString(key);
    }

    public static int getInt(String key) {
        return propertyReader.getValueAsInt(key);
    }
}