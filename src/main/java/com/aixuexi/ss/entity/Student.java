package com.aixuexi.ss.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyangyang
 * @date 2020/9/10 17:19
 * @description 学生信息
 **/
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;//学生id

    private String studentName;//用户名

    private Integer institutionId;//所属机构

    private String telephone;//手机

    private String passport;//密码

    private Integer environmentTag;//0表示线上，1表示线下

    @Override
    public String toString() {
        return "Student {" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", institutionId='" + institutionId + '\'' +
                ", telephone='" + telephone + '\'' +
                ", passport='" + passport + '\'' +
                ", environmentTag='" + environmentTag + '\'' +
                '}';
    }
}
