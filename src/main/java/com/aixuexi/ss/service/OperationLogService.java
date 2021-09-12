package com.aixuexi.ss.service;

import com.aixuexi.ss.entity.OperationLog;
import com.aixuexi.ss.vo.OperationLogVo;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/13 11:24
 * @description
 **/
public interface OperationLogService {
    public List<OperationLogVo> listOperationLogs(@Param("monthTime")String monthTime);

    public int insertClass(OperationLog operationLog) throws IOException, ParseException;

    void addProject(@Param("projectDto") OperationLog operationLog); //这里一定要加这个注解，
}
