package com.aixuexi.ss.sql.dao.implement;

import com.aixuexi.ss.entity.ClassMessage;
import com.aixuexi.ss.entity.SmallClassMsg;
import com.aixuexi.ss.sql.dao.AssistantDao;
import com.aixuexi.ss.sql.dao.ClassMessageDao;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/10 19:12
 * @description
 **/

public class ClassMessageDaoImpl implements ClassMessageDao {
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<ClassMessageVo> listSearchClassById(@Param("bigclassid") Integer bigClassId) {
        ClassMessageDao classMessageDao = sqlSessionTemplate.getMapper(ClassMessageDao.class);
        List<ClassMessageVo> classMessageVo =  classMessageDao.listSearchClassById(bigClassId);
        return classMessageVo;
    }


    @Override
    public List<ClassMessageVo> listClassMessages() {
        return this.sqlSessionTemplate.selectList("listClassMessages");
    }

    @Override
    public List<SmallClassMsg> getSmallClassMsg(String bigClassId,String subClassId,String classType,String className,Integer pageIndex,Integer pageSize) {
        return  this.sqlSessionTemplate.selectList("getSmallClassMsg");
    }

    @Override
    public ClassMessageVo getClassMessageById(Integer id) {
        return this.sqlSessionTemplate.selectOne("getClassMessageById", id);
    }

    @Override
    public int insertClass(ClassMessage classMessage) {
        System.out.print(classMessage+"爱你ClassMessageDaoImpl\n");
        //int obj=
        //this.sqlSessionTemplate.commit();
        return this.sqlSessionTemplate.insert("insertClass", classMessage);
    }

    @Override
    public void addProject(@Param("ClassMessage") ClassMessage classMessage) {

    }

    @Override
    public List<ClassMessageVo> getSearchClassMessage(@Param("classname")String className,
                                                      @Param("environmenttag")int environmentTag,
                                                      @Param("ltelephone")String lTelephone,
                                                      @Param("atelephone")String aTelephone) {
        ClassMessageDao classMessageDao = sqlSessionTemplate.getMapper(ClassMessageDao.class);
        List<ClassMessageVo> classMessageVo = classMessageDao.getSearchClassMessage(className, environmentTag, lTelephone,aTelephone);
        return classMessageVo;
    }
}
