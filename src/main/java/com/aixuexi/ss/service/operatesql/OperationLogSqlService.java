package com.aixuexi.ss.service.operatesql;

import com.aixuexi.ss.common.util.Log_Exception;
import com.aixuexi.ss.common.util.TimeUtil;
import com.aixuexi.ss.entity.OperationLog;
import com.aixuexi.ss.service.OperationLogService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyangyang
 * @date 2020/9/13 21:13
 * @description
 **/
@Service
@Slf4j
@Configuration
public class OperationLogSqlService {
    @Autowired
    private OperationLogService operationLogService;

    public String insertoperationLogClass(int classtype) throws IOException, ParseException {
        OperationLog operationLog = new OperationLog();
        String path = System.getProperty("user.dir") + "\\file.txt";
        operationLog.setClassType(classtype);
        TimeUtil timeUtil = new TimeUtil();
        operationLog.setCreateTime(timeUtil.getCurrentTimeString());
        operationLog.setUserId(1);
        String errorContent = null;
        int obj1 = -2;
        try {
            log.info("ceshi" + operationLog.toString());
            obj1 = this.operationLogService.insertClass(operationLog);
        } catch (Exception e) {
            log.info(" ", e);
            errorContent = e.getClass().getName() + "  error Info  " + e.getMessage() + e;
        }
        Log_Exception le = new Log_Exception();
        le.writeEror_to_txt(path, errorContent);

        return "insert result1:" + obj1;
    }
    /**
     * 重载记录日志接口 简化主干代码
     * @param reqBody
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public String insertoperationLogClass(JSONObject reqBody) throws IOException, ParseException {
        OperationLog operationLog = new OperationLog();
        String path = System.getProperty("user.dir") + "\\file.txt";
        Integer classtype = reqBody.getJSONObject("basicAiInfo").getInteger("classType");
        operationLog.setClassType(classtype);
        TimeUtil timeUtil = new TimeUtil();
        operationLog.setCreateTime(timeUtil.getCurrentTimeString());
        operationLog.setUserId(1);
        String errorContent = null;
        int obj1 = -2;
        try {
            log.info("ceshi" + operationLog.toString());
            obj1 = this.operationLogService.insertClass(operationLog);
        } catch (Exception e) {
            log.info(" ", e);
            errorContent = e.getClass().getName() + "  error Info  " + e.getMessage() + e;
        }
        Log_Exception le = new Log_Exception();
        le.writeEror_to_txt(path, errorContent);

        return "insert result1:" + obj1;
    }
}
