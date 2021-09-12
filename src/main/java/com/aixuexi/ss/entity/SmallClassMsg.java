package com.aixuexi.ss.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class SmallClassMsg {
    private Integer id;

    private Integer mainClassId;

    private String className;

    private Integer studentNumber;

    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date createTime;

    private Integer classType;

    private Integer userId;

    private Integer assistantId;
    private String assistantName;
    private String assistantPhone;
    private String lecturerid;
    private Integer counts;
    private Integer subClassId;

}
