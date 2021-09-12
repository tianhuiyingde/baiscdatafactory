package com.aixuexi.ss.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author wangyangyang
 * @date 2020/9/10 17:20
 * @description 操作日志
 **/
@Data
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;//操作id

    private Integer userId;//用户名称

    private Integer classType;//课程类型

    private String createTime;//创建时间

    private Integer number;

    @Override
    public String toString() {
        return "OperationLog {" +
                "id=" + id +
                ", userName='" + userId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", classtype='" + classType + '\'' +
                ", num='" + number + '\'' +
                '}';
    }
}
