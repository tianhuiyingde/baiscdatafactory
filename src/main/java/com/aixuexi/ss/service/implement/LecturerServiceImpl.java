package com.aixuexi.ss.service.implement;

import com.aixuexi.ss.entity.Lecturer;
import com.aixuexi.ss.service.LecturerService;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.LecturerVo;
import com.aixuexi.ss.sql.dao.LecturerDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 11:33
 * @description
 **/
public class LecturerServiceImpl implements LecturerService {
    @Autowired
    private LecturerDao lecturerDao;

    /**
     * 获得Lecturer列表
     */
    @Override
    public List<LecturerVo> listLecturers() {
        return this.lecturerDao.listLecturers();
    }

    /**
     * 根据id，获得某个Lecturer
     */
    @Override
    public LecturerVo getLecturerById(Integer id) {
        return this.lecturerDao.getLecturerById(id);
    }

    @Override
    public int insertClass(Lecturer lecturer) {
        System.out.print("你好LecturerDao\n");
        return this.lecturerDao.insertClass(lecturer);
    }

    @Override
    public void addProject(Lecturer lecturer) {
        this.lecturerDao.addProject(lecturer);
    }

    @Override
    public List<LecturerVo> getSearchLecturer(@Param("ltelephone")String Ltelephone, @Param("environmenttag")int environmentTag, @Param("nickname")String nickname) {
        return this.lecturerDao.getSearchLecturer(Ltelephone, environmentTag, nickname);
    }
}
