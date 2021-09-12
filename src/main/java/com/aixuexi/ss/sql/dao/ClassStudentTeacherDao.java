package com.aixuexi.ss.sql.dao;

import com.aixuexi.ss.entity.ClassStudentTeacher;
import com.aixuexi.ss.vo.ClassStudentTeacherVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/10 18:34
 * @description
 **/
public interface ClassStudentTeacherDao {
    public List<ClassStudentTeacherVo> listClassStudentTeachers();

    //根据主键搜索
    public ClassStudentTeacherVo getClassStudentTeacherById(Integer id);

    //插入课程、老师、学生的关系
    public int insertClass(ClassStudentTeacher classStudentTeacher);

    void addProject(@Param("ClassStudentTeacher") ClassStudentTeacher classStudentTeacher); //这里一定要加这个注解，不然在配置文件中会找不到这个DTO对象
}
