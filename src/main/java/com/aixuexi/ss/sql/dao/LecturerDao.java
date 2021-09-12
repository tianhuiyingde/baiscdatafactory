package com.aixuexi.ss.sql.dao;

import com.aixuexi.ss.entity.Lecturer;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.LecturerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/10 17:39
 * @description
 **/
public interface LecturerDao {
    public List<LecturerVo> listLecturers();

    //根据主键搜索
    public LecturerVo getLecturerById(Integer id);

    //插入教师
    public int insertClass(Lecturer lecturer);

    void addProject(@Param("Lecturer") Lecturer lecturer); //这里一定要加这个注解，不然在配置文件中会找不到这个DTO对象
    public List<LecturerVo> getSearchLecturer(@Param("ltelephone")String Atelephone, @Param("environmenttag")int environmentTag, @Param("nickname")String nickname);

}
