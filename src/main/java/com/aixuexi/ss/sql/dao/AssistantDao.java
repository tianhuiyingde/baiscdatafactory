package com.aixuexi.ss.sql.dao;

import com.aixuexi.ss.entity.Assistant;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.StudentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/10 17:39
 * @description
 **/
public interface AssistantDao {
    public List<AssistantVo> listAssistants();

    //根据主键搜索
    public AssistantVo getAssistantById(Integer id);

    //插入教师
    public int insertClass(Assistant assistant);

    void addProject(@Param("Assistant") Assistant assistant); //这里一定要加这个注解，不然在配置文件中会找不到这个DTO对象

    public List<AssistantVo> getSearchAssistant(@Param("atelephone")String Atelephone, @Param("environmenttag")int environmentTag, @Param("nickname")String nickname) ;

}
