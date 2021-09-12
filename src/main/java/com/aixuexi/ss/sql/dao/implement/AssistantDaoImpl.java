package com.aixuexi.ss.sql.dao.implement;

import com.aixuexi.ss.entity.Assistant;
import com.aixuexi.ss.sql.dao.AssistantDao;
import com.aixuexi.ss.sql.dao.StudentDao;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.StudentVo;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/10 19:13
 * @description
 **/
public class AssistantDaoImpl implements AssistantDao {
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<AssistantVo> listAssistants() {
        return this.sqlSessionTemplate.selectList("listAssistants");
    }

    @Override
    public AssistantVo getAssistantById(Integer id) {
        return this.sqlSessionTemplate.selectOne("getAssistantById", id);
    }

    @Override
    public int insertClass(Assistant assistant) {
        return this.sqlSessionTemplate.insert("insertClass", assistant);
    }

    @Override
    public void addProject(@Param("Assistant") Assistant assistant) {
    }

    @Override
    public List<AssistantVo> getSearchAssistant(@Param("atelephone")String Atelephone, @Param("environmenttag")int environmentTag, @Param("nickname")String nickname) {
        AssistantDao assistantDao = sqlSessionTemplate.getMapper(AssistantDao.class);
        List<AssistantVo> assistantVo = assistantDao.getSearchAssistant(Atelephone, environmentTag,nickname);
        return assistantVo;
    }
}
