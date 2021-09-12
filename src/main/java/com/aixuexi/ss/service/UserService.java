package com.aixuexi.ss.service;

import com.aixuexi.ss.entity.User;
import com.aixuexi.ss.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 23:58
 * @description
 **/
public interface UserService {
    public List<UserVo> listUsers();

    public UserVo getUserByName(String userName);

    public int insertClass(User User);

    void addProject(@Param("projectDto") User User); //这里一定要加这个注解

}
