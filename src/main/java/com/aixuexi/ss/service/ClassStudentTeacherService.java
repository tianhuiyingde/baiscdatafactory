package com.aixuexi.ss.service;

import com.aixuexi.ss.entity.ClassStudentTeacher;
import com.aixuexi.ss.vo.ClassStudentTeacherVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/13 11:23
 * @description
 **/
public interface ClassStudentTeacherService {
    public List<ClassStudentTeacherVo> listClassStudentTeachers();

    public ClassStudentTeacherVo getClassStudentTeacherById(Integer id);

    public int insertClass(ClassStudentTeacher classStudentTeacher);

    void addProject(@Param("projectDto") ClassStudentTeacher classStudentTeacher); //这里一定要加这个注解，
}
