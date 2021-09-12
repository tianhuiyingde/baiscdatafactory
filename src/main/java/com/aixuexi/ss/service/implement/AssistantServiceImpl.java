package com.aixuexi.ss.service.implement;

import com.aixuexi.ss.entity.Assistant;
import com.aixuexi.ss.service.AssistantService;
import com.aixuexi.ss.sql.dao.AssistantDao;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.StudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 11:33
 * @description
 **/
public class AssistantServiceImpl implements AssistantService {
    @Autowired
    private AssistantDao AssistantDao;

    /**
     * 获得Assistant列表
     */
    @Override
    public List<AssistantVo> listAssistants() {
        return this.AssistantDao.listAssistants();
    }

    /**
     * 根据id，获得某个Assistant
     */
    @Override
    public AssistantVo getAssistantById(Integer id) {
        return this.AssistantDao.getAssistantById(id);
    }

    @Override
    public int insertClass(Assistant assistant) {
        System.out.print("你好AssistantDao\n");
        return this.AssistantDao.insertClass(assistant);
    }

    @Override
    public void addProject(Assistant assistant) {
        this.AssistantDao.addProject(assistant);
    }

    @Override
    public List<AssistantVo> getSearchAssistant(@Param("atelephone")String Atelephone, @Param("environmenttag")int environmentTag, @Param("nickname")String nickname) {
        return this.AssistantDao.getSearchAssistant(Atelephone, environmentTag, nickname);
    }

}
