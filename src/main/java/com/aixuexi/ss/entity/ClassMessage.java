package com.aixuexi.ss.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ClassMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String className;

    private Integer bigClassId;
    private Integer subClassId;

    private Integer studentNumber;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date beginTime;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date endTime;

    private Integer classType;

    private Integer userId;

    private Integer lecturerId;

    private Integer assistantId;

    private String studentIds;

    private List<Assistant> assistants;

    private List<Lecturer> lecturers;

    private List<User> users;

    private Integer environmentTag;//0表示线上，1表示线下
    private String assistantName;
    private String assistantPhone;

}