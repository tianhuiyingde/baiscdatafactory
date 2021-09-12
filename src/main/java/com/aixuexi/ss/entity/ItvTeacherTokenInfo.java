package com.aixuexi.ss.entity;

import lombok.Data;

/**
 * @author wangyangyang
 * @date 2020/9/14 22:40
 * @description 生产系统用户登录
 **/
@Data
public class ItvTeacherTokenInfo {
    private String userId;//主讲的id

    private String ptpc;//主讲的token
}
