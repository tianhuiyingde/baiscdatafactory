package com.aixuexi.ss.common.conf;

import com.aixuexi.ss.common.dns.LoadDns;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitServerEnv implements ApplicationRunner {
    @Value("${serverEnv}")
    private  String env;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("加载当前环境为测试环境:"+env);
        if("dev".equals(env))
        { LoadDns.initDns(1); }
    }
}
