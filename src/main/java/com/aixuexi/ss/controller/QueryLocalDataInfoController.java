package com.aixuexi.ss.controller;


import com.aixuexi.ss.common.util.*;
import com.aixuexi.ss.entity.SmallClassMsg;
import com.aixuexi.ss.service.*;
import com.aixuexi.ss.service.operatesql.OperationLogSqlService;
import com.aixuexi.ss.vo.*;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangyangyang
 * @Date: 2019/12/26 14:25
 */

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/QueryLocalDataInfo")
public class QueryLocalDataInfoController {
    @Autowired
    private ClassMessageService classMessageService;
    @Autowired
    private ClassStudentTeacherService classStudentTeacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private LecturerService lecturerService;
    @Autowired
    private AssistantService assistantService;


    /**
     * http://localhost:9098/listClassMessages
     *
     * @return
     */

    @GetMapping("/listClassMessages")
    public JsonResult listClassMessages() {
        List<ClassMessageVo> list = this.classMessageService.listClassMessages();
        log.info("ClassMessage消息"+list);
        return new JsonResult(ResultCode.SUCCESS, list);
    }

    /**
     * 查询小班列表
     * @param reqBody
     * @return
     */
    @PostMapping("/getSmallMsgList")
    public JsonResult getSmallMsgList(@RequestBody JSONObject reqBody) {
        String classType=reqBody.getString("classType");
        String bigClassId=reqBody.getString("bigClassId");
        String subClassId=reqBody.getString("subClassId");
        String className=reqBody.getString("className");
        Integer pageIndex = reqBody.getInteger("pageIndex");
        Integer pageSize = reqBody.getInteger("pageSize");
        List<SmallClassMsg> list = this.classMessageService.getSmallClassMsg(bigClassId,subClassId,classType,className,pageIndex-1,pageSize);
        JSONObject repObj = new JSONObject();
        if(!list.isEmpty())
        {
            Integer pageTotal = list.get(0).getCounts();
            repObj.put("body",list);
            repObj.put("pageTotal",pageTotal);
            log.info("ClassMessage消息"+repObj);
        }
        return new JsonResult(ResultCode.SUCCESS, repObj);
    }

    /**
     * http://localhost:9098/listLecturers
     *
     * @return
     */

    @GetMapping("/listLecturers")
    public JsonResult listLecturers() {
        List<LecturerVo> list = this.lecturerService.listLecturers();
        log.info("Teacher消息"+list);
        return new JsonResult(ResultCode.SUCCESS, list);
    }

    /**
     * http://localhost:9098/listAssistants
     *
     * @return
     */

    @GetMapping("/listAssistants")
    public JsonResult listAssistants() {
        List<AssistantVo> list = this.assistantService.listAssistants();
        log.info("Teacher消息"+list);
        return new JsonResult(ResultCode.SUCCESS, list);
    }

    /**
     * http://localhost:9098/listStudents
     *
     * @return
     */

    @GetMapping("/listStudents")
    public JsonResult listStudents() {
        List<StudentVo> list = this.studentService.listStudents();
        log.info("Student消息"+list);
        return new JsonResult(ResultCode.SUCCESS, list);
    }

    /**
     * http://localhost:9098/listSearchClassMessages
     *
     * @return
     */
    @PostMapping("/listSearchClassMessages")
    public PageInfo<ClassMessageVo> listSearchClassMessages(@RequestBody JSONObject reqBody) {
        //pageNum:表示第几页  pageSize:表示一页展示的数据
        int pageNum = reqBody.getInteger("pageNum");
        int pageSize = reqBody.getInteger("pageSize");
        int seachTag = reqBody.getInteger("seachTag");
        //设置当前页和每页数量
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("id ASC");
        if (seachTag == 1) {
            List<ClassMessageVo> list = this.classMessageService.listClassMessages();
            //将查询到的数据封装到PageInfo对象
            PageInfo<ClassMessageVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        } else if (seachTag == 0) {
            String className = reqBody.getString("className");//班级名
            String lTelephone = reqBody.getString("ltelephone");//手机
            String aTelephone = reqBody.getString("atelephone");//手机
            String environmentStr = reqBody.getString("environmentTag");//0表示线上，1表示线下
            Integer environmentTag = -1;
            String str1="蜂巢环境";
            String str2="线上环境";
            if(environmentStr!=null) {
                if (environmentStr.equals(str1))
                    environmentTag = 1;
                else if (environmentStr.equals(str2))
                    environmentTag = 0;
            }
            else
                environmentTag = null;
            List<ClassMessageVo> list = this.classMessageService.getSearchClassMessage(className, environmentTag, lTelephone,aTelephone);
            //将查询到的数据封装到PageInfo对象
            PageInfo<ClassMessageVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        }
        //分割数据成功
        return null;
    }

    /**
     * http://localhost:9098/listSearchLecturers
     *
     * @return
     */
    @PostMapping("/listSearchLecturers")
    public PageInfo<LecturerVo> listSearchLecturers(@RequestBody JSONObject reqBody) {
        //pageNum:表示第几页  pageSize:表示一页展示的数据
        int pageNum = reqBody.getInteger("pageNum");
        int pageSize = reqBody.getInteger("pageSize");
        int seachTag = reqBody.getInteger("seachTag");
        //设置当前页和每页数量
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("id ASC");
        if (seachTag == 1) {
            List<LecturerVo> list = this.lecturerService.listLecturers();
            //将查询到的数据封装到PageInfo对象
            PageInfo<LecturerVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        } else if (seachTag == 0) {
            String nickname = reqBody.getString("nickname");//用户名
            String LTelephone = reqBody.getString("LTelephone");//手机
            String environmentStr = reqBody.getString("environmentTag");//0表示线上，1表示线下
            Integer environmentTag = -1;
            String str1="蜂巢环境";
            String str2="线上环境";
            if(environmentStr!=null) {
                if (environmentStr.equals(str1))
                    environmentTag = 1;
                else if (environmentStr.equals(str2))
                    environmentTag = 0;
            }
            else
                environmentTag = null;
            List<LecturerVo> list = this.lecturerService.getSearchLecturer(LTelephone, environmentTag, nickname);
            //将查询到的数据封装到PageInfo对象
            PageInfo<LecturerVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        }
        //分割数据成功
        return null;
    }


    /**
     * http://localhost:9098/listSearchAssistants
     *
     * @return
     */
    @PostMapping("/listSearchAssistants")
    public PageInfo<AssistantVo> listSearchAssistants(@RequestBody JSONObject reqBody) {
        //pageNum:表示第几页  pageSize:表示一页展示的数据
        int pageNum = reqBody.getInteger("pageNum");
        int pageSize = reqBody.getInteger("pageSize");
        int seachTag = reqBody.getInteger("seachTag");
        //设置当前页和每页数量
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("id ASC");
        if (seachTag == 1) {
            List<AssistantVo> list = this.assistantService.listAssistants();
            //将查询到的数据封装到PageInfo对象
            PageInfo<AssistantVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        } else if (seachTag == 0) {
            String nickname = reqBody.getString("nickname");//用户名
            String ATelephone = reqBody.getString("ATelephone");//手机
            String environmentStr = reqBody.getString("environmentTag");//0表示线上，1表示线下
            Integer environmentTag = -1;
            String str1="蜂巢环境";
            String str2="线上环境";
            if(environmentStr!=null) {
                if (environmentStr.equals(str1))
                    environmentTag = 1;
                else if (environmentStr.equals(str2))
                    environmentTag = 0;
            }
            else
                environmentTag = null;
            List<AssistantVo> list = this.assistantService.getSearchAssistant(ATelephone, environmentTag, nickname);
            //将查询到的数据封装到PageInfo对象
            PageInfo<AssistantVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        }
        //分割数据成功
        return null;
    }

    /**
     * http://localhost:9098/listSearchStudents
     *
     * @return
     */
    @PostMapping(value = "/listSearchStudents")
    public PageInfo<StudentVo> listSearchStudents(@RequestBody JSONObject reqBody) {
        //pageNum:表示第几页  pageSize:表示一页展示的数据
        int pageNum = reqBody.getInteger("pageNum");
        int pageSize = reqBody.getInteger("pageSize");
        int seachTag = reqBody.getInteger("seachTag");
            //设置当前页和每页数量
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("id ASC");
        if (seachTag == 1) {
            List<StudentVo> list = this.studentService.listStudents();
            //将查询到的数据封装到PageInfo对象
            PageInfo<StudentVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        } else if (seachTag == 0) {
            String studentName = reqBody.getString("studentName");//用户名
            String telephone = reqBody.getString("telephone");//手机
            String environmentStr = reqBody.getString("environmentTag");//0表示线上，1表示线下
            Integer environmentTag = -1;
            String str1="蜂巢环境";
            String str2="线上环境";
            if(environmentStr!=null) {
                if (environmentStr.equals(str1))
                    environmentTag = 1;
                else if (environmentStr.equals(str2))
                    environmentTag = 0;
            }
            else
                environmentTag = null;
            List<StudentVo> list = this.studentService.getSearchStudent(telephone, environmentTag, studentName);
            //将查询到的数据封装到PageInfo对象
            PageInfo<StudentVo> pageInfo = new PageInfo(list, 3);
            log.info("pageInfo" + pageInfo);
            return pageInfo;
        }
        //分割数据成功
        return null;
    }

    /**
     * http://localhost:9098/listClassStudentTeachers
     *
     * @return
     */

    @GetMapping("/listClassStudentTeachers")
    public JsonResult listClassStudentTeachers() {
        List<ClassStudentTeacherVo> list = this.classStudentTeacherService.listClassStudentTeachers();
        log.info("ClassStudentTeacher消息" + list);
        return new JsonResult(ResultCode.SUCCESS, list);
    }

    /**
     * http://localhost:9098/getClassMessageById?id=1
     *
     * @return
     */

    @GetMapping("/getClassMessageById")
    public JsonResult listClassMessage(Integer id) {
        ClassMessageVo obj = this.classMessageService.getClassMessageById(id);
        return new JsonResult(ResultCode.SUCCESS, obj);
    }

    /**
     * http://localhost:9098/getClassStudentTeacherById?id=1
     *
     * @return
     */

    @GetMapping("/getClassStudentTeacherById")
    public JsonResult listClassStudentTeachers(Integer id) {
        ClassStudentTeacherVo obj = this.classStudentTeacherService.getClassStudentTeacherById(id);
        return new JsonResult(ResultCode.SUCCESS, obj);
    }

}

