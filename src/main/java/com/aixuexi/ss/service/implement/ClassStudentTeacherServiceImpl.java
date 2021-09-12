package com.aixuexi.ss.service.implement;

import com.aixuexi.ss.entity.ClassStudentTeacher;
import com.aixuexi.ss.service.ClassStudentTeacherService;
import com.aixuexi.ss.vo.ClassStudentTeacherVo;
import com.aixuexi.ss.sql.dao.ClassStudentTeacherDao;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/13 11:33
 * @description
 **/
public class ClassStudentTeacherServiceImpl implements ClassStudentTeacherService {
    @Autowired
    private ClassStudentTeacherDao classStudentTeacherDao;

    /**
     * 获得ClassStudentTeacher列表
     */
    @Override
    public List<ClassStudentTeacherVo> listClassStudentTeachers() {
        return this.classStudentTeacherDao.listClassStudentTeachers();
    }

    /**
     * 根据id，获得某个ClassStudentTeacher
     */
    @Override
    public ClassStudentTeacherVo getClassStudentTeacherById(Integer id) {
        return this.classStudentTeacherDao.getClassStudentTeacherById(id);
    }

    @Override
    public int insertClass(ClassStudentTeacher classStudentTeacher) {
        System.out.print("你好ClassStudentTeacherDao\n");
        return this.classStudentTeacherDao.insertClass(classStudentTeacher);
    }

    @Override
    public void addProject(ClassStudentTeacher classStudentTeacher) {
        this.classStudentTeacherDao.addProject(classStudentTeacher);
    }
}
