package com.aixuexi.ss.service.implement;

import com.aixuexi.ss.entity.ClassMessage;
import com.aixuexi.ss.entity.SmallClassMsg;
import com.aixuexi.ss.service.ClassMessageService;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.aixuexi.ss.sql.dao.ClassMessageDao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 11:33
 * @description
 **/
public class ClassMessageServiceImpl implements ClassMessageService {
    @Autowired
    private ClassMessageDao classMessageDao;

    /**
     * 获得ClassMessage列表
     */
    @Override
    public List<ClassMessageVo> listClassMessages() {
        return this.classMessageDao.listClassMessages();
    }

    @Override
    public List<SmallClassMsg> getSmallClassMsg(String bigClassId,String subClassId,String classType,String className,Integer pageIndex,Integer pageSize) {
        return classMessageDao.getSmallClassMsg(bigClassId,subClassId,classType,className,pageIndex-1,pageSize);
    }

    /**
     * 根据id，获得某个ClassMessage
     */
    @Override
    public ClassMessageVo getClassMessageById(Integer id) {
        return this.classMessageDao.getClassMessageById(id);
    }

    @Override
    public int insertClass(ClassMessage classMessage) {
        System.out.print("你好ClassMessageDao\n");
        return this.classMessageDao.insertClass(classMessage);
    }

    @Override
    public void addProject(ClassMessage classMessage) {
        this.classMessageDao.addProject(classMessage);
    }

    @Override
    public List<ClassMessageVo> getSearchClassMessage(@Param("classname")String className,
                                                      @Param("environmenttag")int environmentTag,
                                                      @Param("ltelephone")String lTelephone,
                                                      @Param("atelephone")String aTelephone) {
        return this.classMessageDao.getSearchClassMessage(className, environmentTag, lTelephone,aTelephone);
    }

    @Override
    public List<ClassMessageVo> listSearchClassById(@Param("bigclassid") Integer bigClassId) {
        return this.classMessageDao.listSearchClassById(bigClassId);
    }
}
