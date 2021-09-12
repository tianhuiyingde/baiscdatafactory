package com.aixuexi.ss.sql.dao;

import com.aixuexi.ss.entity.User;
import com.aixuexi.ss.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 23:56
 * @description
 **/
public interface UserDao {
    public List<UserVo> listUsers();

    //根据主键搜索
    public UserVo getUserByName(String userName);

    //插入教师
    public int insertClass(User User);

    void addProject(@Param("User") User User); //这里一定要加这个注解，不然在配置文件中会找不到这个DTO对象
}
