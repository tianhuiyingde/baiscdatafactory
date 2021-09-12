package com.aixuexi.ss.sql.dao.implement;

import com.aixuexi.ss.entity.Student;
import com.aixuexi.ss.sql.dao.StudentDao;
import com.aixuexi.ss.vo.StudentVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/10 19:13
 * @description
 **/
public class StudentDaoImpl implements StudentDao {
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<StudentVo> listStudents() {
        return this.sqlSessionTemplate.selectList("listStudents");
    }

    @Override
    public StudentVo getStudentById(Integer id) {
        return this.sqlSessionTemplate.selectOne("getStudentById", id);
    }

    @Override
    public StudentVo getStudentByTelephone(@Param("telephone")String telephone, @Param("environmenttag")int environmentTag)  {
        StudentDao studentDao = sqlSessionTemplate.getMapper(StudentDao.class);
        StudentVo studentVo = studentDao.getStudentByTelephone(telephone, environmentTag);
        return studentVo;
    }
    public List<StudentVo> getSearchStudent(@Param("telephone")String telephone, @Param("environmenttag")int environmentTag,@Param("studentname")String studentName){
        StudentDao studentDao = sqlSessionTemplate.getMapper(StudentDao.class);
        List<StudentVo> studentVo = studentDao.getSearchStudent(telephone, environmentTag,studentName);
        return studentVo;
    }
    @Override
    public int insertClass(Student student) {
        System.out.print(student + "爱你StudentDaoImpl\n");
        //int obj=
        //this.sqlSessionTemplate.commit();
        return this.sqlSessionTemplate.insert("insertClass", student);
    }

    @Override
    public void addProject(@Param("student") Student student) {

    }
}
