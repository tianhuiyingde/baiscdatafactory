package com.aixuexi.ss.service.implement;

import com.aixuexi.ss.entity.OperationLog;
import com.aixuexi.ss.service.OperationLogService;
import com.aixuexi.ss.vo.OperationLogVo;
import com.aixuexi.ss.sql.dao.OperationLogDao;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 11:33
 * @description
 **/
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * 获得OperationLog列表
     */
    @Override
    public List<OperationLogVo> listOperationLogs(@Param("monthTime")String monthTime) {
        return this.operationLogDao.listOperationLogs(monthTime);
    }

    @Override
    public int insertClass(OperationLog operationLog) throws IOException, ParseException {

        return this.operationLogDao.insertClass(operationLog);
    }

    @Override
    public void addProject(OperationLog operationLog) {
        this.operationLogDao.addProject(operationLog);
    }
}
