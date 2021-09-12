package com.aixuexi.ss.service;

import com.aixuexi.ss.entity.ClassMessage;
import com.aixuexi.ss.entity.SmallClassMsg;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/13 11:23
 * @description
 **/
public interface ClassMessageService {
    public List<ClassMessageVo> listClassMessages();

    public List<SmallClassMsg>  getSmallClassMsg(String bigClassId,String subClassId,String classType,String className,Integer pageIndex,Integer pageSize);

    public List<ClassMessageVo> listSearchClassById(@Param("bigclassid") Integer bigClassId);

    public ClassMessageVo getClassMessageById(Integer id);

    public int insertClass(ClassMessage classMessage);

    void addProject(@Param("projectDto") ClassMessage classMessage); //这里一定要加这个注解，
    public List<ClassMessageVo> getSearchClassMessage(@Param("classname")String className,
                                                      @Param("environmenttag")int environmentTag,
                                                      @Param("ltelephone")String lTelephone,
                                                      @Param("atelephone")String aTelephone);

}
