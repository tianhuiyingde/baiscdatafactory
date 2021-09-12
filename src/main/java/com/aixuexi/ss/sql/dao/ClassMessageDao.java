package com.aixuexi.ss.sql.dao;

import com.aixuexi.ss.entity.ClassMessage;
import com.aixuexi.ss.entity.SmallClassMsg;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/10 17:38
 * @description
 **/
public interface ClassMessageDao {
    public List<ClassMessageVo> listClassMessages();

    public List<SmallClassMsg>  getSmallClassMsg(String bigClassId,String subClassId,String classType,String className,Integer pageIndex,Integer pageSize);


    public List<ClassMessageVo> listSearchClassById(@Param("bigclassid") Integer bigClassId);

    //根据主键搜索
    public ClassMessageVo getClassMessageById(Integer id);

    //插入课程信息对象ClassMessage
    public int insertClass(ClassMessage classMessage);

    void addProject(@Param("ClassMessage") ClassMessage classMessage); //这里一定要加这个注解，不然在配置文件中会找不到这个DTO对象
    public List<ClassMessageVo> getSearchClassMessage(@Param("classname")String className,
                                                      @Param("environmenttag")int environmentTag,
                                                      @Param("ltelephone")String lTelephone,
                                                      @Param("atelephone")String aTelephone);
}
