package com.aixuexi.ss.common.dns;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author wangyangyang
 * @date 2020/10/27 11:33
 * @description
 **/
@Slf4j
public class PropertiesHandler {
    synchronized public static Properties getProperties(String proName) {
        log.info("开始加载properties文件内容.......");
        Properties props = new Properties();
        InputStream in = null;
        try {
            //第一种，通过类加载器进行获取properties文件流
            in = PropertiesHandler.class.getResourceAsStream(proName);
            //第二种，通过类进行获取properties文件流
            //in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            log.error("jdbc.properties文件未找到");
        } catch (IOException e) {
            log.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("jdbc.properties文件流关闭出现异常");
            }
        }
        log.info("加载properties文件内容完成...........");
        log.info("properties文件内容：" + props);
        return props;
    }
    public PropertiesHandler() {
    }
}