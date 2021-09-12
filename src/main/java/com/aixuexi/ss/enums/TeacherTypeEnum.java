package com.aixuexi.ss.enums;

/**
 * @author wangyangyang
 * @date 2020/11/11 18:41
 * @description
 **/
public enum TeacherTypeEnum {
    TEACHER("teacher", "教师", 3),
    JIAOWU("jiaowu", "教务", 4),
    ADMISSIONSOFFICER("admissions_officer", "招生负责人", 5),
    KEFU("kefu", "前台", 6),
    SXTRAINER("sx_trainer", "培训教师", 10),
    TEACHERPRINT("teacher_print", "可打印的老师", 7),
    ASSISTANT("assistant", "助教", 9),
    DIY("diy_instructor", "教研员", 11),
    DTASSISTANT("dt_assistant", "双师助教", 12),
    MANAGE_ASSISTANT("manage_assistant", "校长助理", 14);


    TeacherTypeEnum(String roleCode, String detail, int roleId) {
        this.roleCode = roleCode;
        this.detail = detail;
        this.roleId = roleId;
    }

    private String roleCode;
    private String detail;
    private int roleId;

    public String getRoleCode() {
        return roleCode;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getDetail() {
        return detail;
    }
}
