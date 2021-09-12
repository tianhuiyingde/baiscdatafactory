package com.aixuexi.ss;

import com.aixuexi.ss.common.dns.LoadDns;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
//@MapperScan({"com.aixuexi.ss.sql.dao"})
@MapperScan({"com.aixuexi.ss.service"})
public class SsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsApplication.class, args);
    }

}
