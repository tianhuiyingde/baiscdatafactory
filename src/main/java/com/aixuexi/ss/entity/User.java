package com.aixuexi.ss.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wangyangyang
 * @date 2020/9/13 23:52
 * @description
 **/
@Data
public class User {
    private Integer userId;

    private String userName;

    private String passport;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date createTime;

    private Integer userType;

    @Override
    public String toString() {
        return "User {" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
