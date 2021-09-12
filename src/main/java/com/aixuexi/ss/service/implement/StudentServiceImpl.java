package com.aixuexi.ss.service.implement;

import com.aixuexi.ss.entity.Student;
import com.aixuexi.ss.service.StudentService;
import com.aixuexi.ss.vo.StudentVo;
import com.aixuexi.ss.sql.dao.StudentDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 11:33
 * @description
 **/
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;

    /**
     * 获得Student列表
     */
    @Override
    public List<StudentVo> listStudents() {
        return this.studentDao.listStudents();
    }

    /**
     * 根据id，获得某个Student
     */
    @Override
    public StudentVo getStudentById(Integer id) {
        return this.studentDao.getStudentById(id);
    }

    /**
     * telephone，environmenttag获得某个Student
     */
    @Override
    public StudentVo getStudentByTelephone(@Param("telephone") String telephone, @Param("environmenttag") int environmentTag) {
        return this.studentDao.getStudentByTelephone(telephone, environmentTag);
    }

    /**
     * telephone，environmenttag,studentName获得某个Student
     */
    @Override
    public List<StudentVo> getSearchStudent(@Param("telephone") String telephone, @Param("environmenttag") int environmentTag, @Param("studentname") String studentName) {
        return this.studentDao.getSearchStudent(telephone, environmentTag, studentName);
    }


    @Override
    public int insertClass(Student student) {
        System.out.print("你好StudentDao\n");
        return this.studentDao.insertClass(student);
    }

    @Override
    public void addProject(Student student) {
        this.studentDao.addProject(student);
    }

}
