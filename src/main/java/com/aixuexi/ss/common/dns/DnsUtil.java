package com.aixuexi.ss.common.dns;

import io.leopard.javahost.JavaHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author wangyangyang
 * @date 2020/10/28 15:57
 * @description
 **/
public class DnsUtil {
    private final static Logger logger = LoggerFactory.getLogger(DnsUtil.class);

    private volatile static DnsUtil instance = null;
    private final static String PROPERTIES_NAME = "./src/main/resources/test_ghost/host.properties";

    private DnsUtil() {
        try {
            String env =  ConfigUtil.getString("env");
            logger.info("*********** 加载dns文件, 环境env:{}, 文件:{} ******** ",env,PROPERTIES_NAME);

            Properties properties = PropertiesUtil.getProperties(PROPERTIES_NAME);
            int count = JavaHost.updateVirtualDns(properties);

            logger.info("加载dns文件成功, 环境env:{}, 文件:{}, count:{}",env,PROPERTIES_NAME,count);
            JavaHost.printAllVirtualDns();// 打印所有虚拟DNS记录
        } catch (Exception e) {
            throw new IllegalStateException("dns 加载出错，请检查host配置");
        }
    }

    public static synchronized DnsUtil initDns(){
        if (instance == null ){
            synchronized (DnsUtil.class) {
                if(instance == null) {
                    instance = new DnsUtil();
                }
            }
        }
        return instance;
    }

}
