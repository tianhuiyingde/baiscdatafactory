package com.aixuexi.ss.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyangyang
 * @date 2020/9/1018:22
 * @description  一个课程下面的学生、主讲老师、助教老师
 **/
@Data
public class ClassStudentTeacher implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;//id

    private String classId;//课程id

    private Integer assistantTeacherId;//助教老师id

    private Integer lecturerTeacherId;//主讲老师id

    private Integer stedentList;//学生id list

    private Integer environmentTag;//0表示线上，1表示线下

    @Override
    public String toString() {
        return "ClassStudentTeacher {" +
                "id=" + id +
                ", assistantTeacherId='" + assistantTeacherId + '\'' +
                ", classId='" + classId + '\'' +
                ", lecturerTeacherId='" + lecturerTeacherId + '\'' +
                ", stedentList='" + stedentList + '\'' +
                '}';
    }
}
