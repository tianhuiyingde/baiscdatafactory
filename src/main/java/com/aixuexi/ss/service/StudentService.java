package com.aixuexi.ss.service;

import com.aixuexi.ss.entity.Student;
import com.aixuexi.ss.vo.StudentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/13 11:25
 * @description
 **/
@Service
@Configuration
public interface StudentService {
    public List<StudentVo> listStudents();

    public StudentVo getStudentById(Integer id);

    public StudentVo getStudentByTelephone(@Param("telephone")String telephone, @Param("environmenttag")int environmentTag);

    public int insertClass(Student student);

    void addProject(@Param("projectDto") Student student); //这里一定要加这个注解，

    public List<StudentVo> getSearchStudent(@Param("telephone")String telephone, @Param("environmenttag")int environmentTag,@Param("studentname")String studentName);
}
