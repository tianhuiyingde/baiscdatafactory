package com.aixuexi.ss.service;

import com.aixuexi.ss.entity.Lecturer;
import com.aixuexi.ss.vo.AssistantVo;
import com.aixuexi.ss.vo.LecturerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/13 11:25
 * @description
 **/
public interface LecturerService {
    public List<LecturerVo> listLecturers();

    public LecturerVo getLecturerById(Integer id);

    public int insertClass(Lecturer lecturer);

    void addProject(@Param("projectDto") Lecturer lecturer); //这里一定要加这个注解，

    public List<LecturerVo> getSearchLecturer(@Param("ltelephone")String Atelephone, @Param("environmenttag")int environmentTag, @Param("nickname")String nickname);

}
