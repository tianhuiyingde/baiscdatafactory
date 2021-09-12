package com.aixuexi.ss.sql.dao.implement;

import com.aixuexi.ss.entity.OperationLog;
import com.aixuexi.ss.sql.dao.LecturerDao;
import com.aixuexi.ss.sql.dao.OperationLogDao;
import com.aixuexi.ss.vo.LecturerVo;
import com.aixuexi.ss.vo.OperationLogVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/10 19:12
 * @description
 **/
public class OperationLogDaoImpl implements OperationLogDao {
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<OperationLogVo> listOperationLogs(@Param("monthTime")String monthTime) {
        OperationLogDao operationLogDao = sqlSessionTemplate.getMapper(OperationLogDao.class);
        List<OperationLogVo> operationLogVo = operationLogDao.listOperationLogs(monthTime);
        return operationLogVo;
    }

    @Override
    public int insertClass(OperationLog operationLog) {
        return this.sqlSessionTemplate.insert("insertClass", operationLog);
    }

    @Override
    public void addProject(@Param("OperationLog") OperationLog operationLog) {

    }
}
