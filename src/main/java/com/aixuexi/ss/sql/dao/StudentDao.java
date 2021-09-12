package com.aixuexi.ss.sql.dao;

import com.aixuexi.ss.entity.Student;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.aixuexi.ss.vo.StudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/10 17:38
 * @description
 **/
public interface StudentDao {
    public List<StudentVo> listStudents();

    //根据主键搜索
    public StudentVo getStudentById(Integer id);

    //根据电话号码搜索
    public StudentVo getStudentByTelephone(@Param("telephone")String telephone, @Param("environmenttag")int environmentTag);

    //插入新建的学生
    public int insertClass(Student student);

    void addProject(@Param("dto") Student student); //这里一定要加这个注解，不然在配置文件中会找不到这个DTO对象

    public List<StudentVo> getSearchStudent(@Param("telephone")String telephone, @Param("environmenttag")int environmentTag,@Param("studentname")String studentName);

}
