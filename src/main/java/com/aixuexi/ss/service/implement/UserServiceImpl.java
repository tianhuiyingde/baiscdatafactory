package com.aixuexi.ss.service.implement;

import com.aixuexi.ss.entity.User;
import com.aixuexi.ss.service.UserService;
import com.aixuexi.ss.sql.dao.UserDao;
import com.aixuexi.ss.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 23:58
 * @description
 **/
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao UserDao;

    /**
     * 获得User列表
     */
    @Override
    public List<UserVo> listUsers() {
        return this.UserDao.listUsers();
    }

    /**
     * 根据id，获得某个User
     */
    @Override
    public UserVo getUserByName(String userName) {
        return this.UserDao.getUserByName(userName);
    }

    @Override
    public int insertClass(User User) {
        System.out.print("你好UserDao\n");
        return this.UserDao.insertClass(User);
    }

    @Override
    public void addProject(User User) {
        this.UserDao.addProject(User);
    }

}