package com.aixuexi.ss.sql.dao;

import com.aixuexi.ss.entity.OperationLog;
import com.aixuexi.ss.vo.OperationLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/10 17:37
 * @description
 **/

public interface OperationLogDao {
    public List<OperationLogVo> listOperationLogs(@Param("monthTime")String monthTime);
    //插入建课操作日志
    public int insertClass(OperationLog operationLog);

    void addProject(@Param("OperationLog") OperationLog operationLog); //这里一定要加这个注解，不然在配置文件中会找不到这个DTO对象
}
