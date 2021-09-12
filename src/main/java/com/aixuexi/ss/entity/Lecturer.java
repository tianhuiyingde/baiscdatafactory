package com.aixuexi.ss.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyangyang
 * @date 2020/9/10 17:20
 * @description 老师信息
 **/
@Data
public class Lecturer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;//老师teacher id

    private String nickname;//老师姓名

    private Integer userId;//用户ID

    private String passport;//密码

    private String LTelephone;//电话号码

    private Integer institutionId;//机构ID

    private String subjectName;//老师所在的科目

    private Integer environmentTag;//0表示线上，1表示线下
}
