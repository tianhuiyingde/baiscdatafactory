package com.aixuexi.ss.common.dns;

import com.aixuexi.ss.controller.CrateAiClassController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.exception.ExceptionUtils;
/**
 * @author wangyangyang
 * @date 2020/10/28 15:59
 * @description
 **/
public class PropertiesUtil {
    private final static Logger logger = LoggerFactory.getLogger(CrateAiClassController.class);
    public static Properties properties = new Properties();

    public static Properties getProperties(String proName) {
        String path = PropertiesUtil.class.getClassLoader().getResource(proName).getPath();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            properties.load(inputStream);

        } catch (FileNotFoundException e) {
            logger.error("环境配置文件读取异常,请检查配置文件,FileNotFoundException,error:{}", ExceptionUtils.getRootCauseStackTrace(e));
        } catch (IOException e) {
            logger.error("环境配置文件读取异常,请检查配置文件,IOException,error:{}", ExceptionUtils.getRootCauseStackTrace(e));
        }
        return properties;
    }
}

