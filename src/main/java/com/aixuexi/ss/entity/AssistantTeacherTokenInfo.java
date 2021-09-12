package com.aixuexi.ss.entity;

import lombok.Data;

import java.lang.Override;

/**
 * @author wangyangyang
 * @date 2020/9/01 11：00
 * @description 助教机构端账号信息
 **/
@Data
public class AssistantTeacherTokenInfo {
    private String userId;//助教的id

    private String ptpc;//助教的token

    @Override
    public String toString() {
        return "AssistantTeacherTokenInfo {" +
                "userId=" + userId +
                ", ptpc=" + ptpc +
                '}';
    }
}
