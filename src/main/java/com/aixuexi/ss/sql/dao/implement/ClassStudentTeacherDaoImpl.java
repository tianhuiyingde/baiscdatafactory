package com.aixuexi.ss.sql.dao.implement;

import com.aixuexi.ss.entity.ClassStudentTeacher;
import com.aixuexi.ss.sql.dao.ClassStudentTeacherDao;
import com.aixuexi.ss.vo.ClassStudentTeacherVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author wangyangyang
 * @date 2020/9/10 19:12
 * @description
 **/
public class ClassStudentTeacherDaoImpl implements ClassStudentTeacherDao{
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<ClassStudentTeacherVo> listClassStudentTeachers() {
        return this.sqlSessionTemplate.selectList("listClassStudentTeachers");
    }

    @Override
    public ClassStudentTeacherVo getClassStudentTeacherById(Integer id) {
        return this.sqlSessionTemplate.selectOne("getClassStudentTeacherById", id);
    }

    @Override
    public int insertClass(ClassStudentTeacher classStudentTeacher) {
        System.out.print(classStudentTeacher+"爱你ClassStudentTeacherDaoImpl\n");
        //int obj=
        //this.sqlSessionTemplate.commit();
        return this.sqlSessionTemplate.insert("insertClass", classStudentTeacher);
    }

    @Override
    public void addProject(@Param("ClassStudentTeacher") ClassStudentTeacher classStudentTeacher) {

    }
}
