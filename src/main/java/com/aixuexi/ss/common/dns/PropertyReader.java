package com.aixuexi.ss.common.dns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author wangyangyang
 * @date 2020/10/28 15:58
 * @description
 **/
public class  PropertyReader {

    private final Properties props;

    private static Logger logger = LoggerFactory.getLogger(PropertyReader.class);

    public PropertyReader(String propFile) {
        this.props = new Properties();
        try {
            InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(propFile);
            props.load(inputStream);
        } catch (Exception e) {
            logger.error("Not found "+propFile);
            logger.error(e.getMessage(), e);
        }
    }

    public PropertyReader(Properties props) {
        this.props = props;
    }

    public int getValueAsInt(String key) {
        int value = 0;
        String v = props.getProperty(key);
        if (v != null) {
            try {
                value = Integer.parseInt(v.trim());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }

    public boolean getValueAsBool(String key) {
        boolean value = false ;
        String v = props.getProperty(key);
        if (v != null) {
            try {
                value = Boolean.parseBoolean(v.trim());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }

    public String getValueAsString(String key) {
        return props.getProperty(key);
    }

    public long getValueAsLong(String key) {
        long value = 0;
        String v = props.getProperty(key);
        if (v != null) {
            try {
                value = Long.parseLong(v.trim());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }
}