package com.aixuexi.ss.service;

import com.aixuexi.ss.entity.Assistant;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.StudentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 11:25
 * @description
 **/
public interface AssistantService {
    public List<AssistantVo> listAssistants();

    public AssistantVo getAssistantById(Integer id);

    public int insertClass(Assistant assistant);

    void addProject(@Param("projectDto") Assistant assistant); //这里一定要加这个注解，

    public List<AssistantVo> getSearchAssistant(@Param("atelephone")String Atelephone, @Param("environmenttag")int environmentTag, @Param("nickname")String nickname);

}
