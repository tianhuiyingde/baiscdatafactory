package com.aixuexi.ss.sql.dao.implement;

import com.aixuexi.ss.entity.Lecturer;
import com.aixuexi.ss.sql.dao.LecturerDao;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.LecturerVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/10 19:13
 * @description
 **/
public class LecturerDaoImpl implements LecturerDao {
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<LecturerVo> listLecturers() {
        return this.sqlSessionTemplate.selectList("listLecturers");
    }

    @Override
    public LecturerVo getLecturerById(Integer id) {
        return this.sqlSessionTemplate.selectOne("getLecturerById", id);
    }

    @Override
    public int insertClass(Lecturer lecturer) {
        return this.sqlSessionTemplate.insert("insertClass", lecturer);
    }

    @Override
    public void addProject(@Param("Lecturer") Lecturer lecturer) {
    }

    @Override
    public List<LecturerVo> getSearchLecturer(@Param("atelephone") String Atelephone, @Param("environmenttag") int environmentTag, @Param("nickname") String nickname) {
        LecturerDao lecturerDao = sqlSessionTemplate.getMapper(LecturerDao.class);
        List<LecturerVo> lecturerVo = lecturerDao.getSearchLecturer(Atelephone, environmentTag, nickname);
        return lecturerVo;
    }
}
