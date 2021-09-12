package com.aixuexi.ss.entity;

import lombok.Data;

/**
 * @author wangyangyang
 * @date 2020/9/01 11：00
 * @description  主讲机构端账号信息
 **/
@Data
public class LecturerTeacherTokenInfo
{
    private String userId;//主讲的id

    private String ptpc;//主讲的token

    @Override
    public String toString() {
        return "LecturerTeacherTokenInfo {" +
                "userId=" + userId +
                ", ptpc='" + ptpc + '\'' +
                '}';
    }
}
