package com.aixuexi.ss.sql.dao.implement;

import com.aixuexi.ss.entity.User;
import com.aixuexi.ss.sql.dao.UserDao;
import com.aixuexi.ss.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 23:54
 * @description
 **/

public class UserDaoImpl implements UserDao {
    @Autowired
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<UserVo> listUsers() {
        return this.sqlSessionTemplate.selectList("listUsers");
    }

    @Override
    public UserVo getUserByName(String userName)  {
        return this.sqlSessionTemplate.selectOne("getUserByName", userName);
    }

    @Override
    public int insertClass(User User) {
        System.out.print(User + "UserDaoImpl\n");
        //int obj=
        //this.sqlSessionTemplate.commit();
        return this.sqlSessionTemplate.insert("insertClass", User);
    }

    @Override
    public void addProject(@Param("User") User User) {

    }
}