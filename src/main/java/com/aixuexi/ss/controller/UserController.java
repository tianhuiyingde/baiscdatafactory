package com.aixuexi.ss.controller;

import com.aixuexi.ss.common.util.JsonResult;
import com.aixuexi.ss.common.util.ResultCode;
import com.aixuexi.ss.service.UserService;
import com.aixuexi.ss.vo.UserVo;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/16 16:21
 * @description 用户登录，注册相关
 **/
@Slf4j
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@Api(tags = "用户")
public class UserController {
    @Autowired
    private UserService userService;


    /**
     * http://localhost:9098/listUsers
     *
     * @return
     */
    @GetMapping("/listUsers")
    public JsonResult listUsers() {
        List<UserVo> list = this.userService.listUsers();
        log.info("User消息" + list);
        return new JsonResult(ResultCode.SUCCESS, list);
    }

    /**
     * http://localhost:9098/login
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(@RequestBody JSONObject reqBody) {
        String userName = reqBody.getString("userName");
        String password = reqBody.getString("password");
        UserVo userVo = this.userService.getUserByName(userName);
        if (userVo == null) {
            return new JsonResult(ResultCode.SUCCESS_NO_USER,"用户不存在");
        } else if (userVo.getPassport().equals(password)) {
            return new JsonResult(ResultCode.SUCCESS,"成功");
        } else {
            return new JsonResult(ResultCode.SUCCESS_FAIL_PASSPORT,"密码错误");
        }
    }
}
