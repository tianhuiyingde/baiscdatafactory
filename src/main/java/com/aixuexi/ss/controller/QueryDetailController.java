package com.aixuexi.ss.controller;

import com.aixuexi.ss.common.response.ResultData;
import com.aixuexi.ss.entity.AssistantTeacherTokenInfo;
import com.aixuexi.ss.entity.LecturerTeacherTokenInfo;
import com.aixuexi.ss.service.createclass.ApplyClassService;
import com.aixuexi.ss.service.createclass.CrateStreamingClassService;
import com.aixuexi.ss.service.operatesql.OperateAssistantService;
import com.aixuexi.ss.service.operatesql.OperateLecturerService;
import com.aixuexi.ss.service.operatesql.OperateStudentService;
import com.aixuexi.ss.vo.StudentVo;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author wangyangyang
 * @date 2020/9/16 20:49
 * @description  学生和老师信息查询
 **/
@Slf4j
@RestController
@CrossOrigin
@Api(tags = "学生和老师信息查询")
public class QueryDetailController {
    @Autowired
    private OperateStudentService operateStudentService;
    @Autowired
    private OperateAssistantService operateAssistantService;
    @Autowired
    private OperateLecturerService operateLecturerService;

    @Autowired
    ApplyClassService applyClassService;
    @Autowired
    CrateStreamingClassService crateStreamingClassService;

    /**
     * http://localhost:9098/queryStudent
     *
     * @return
     */
    @RequestMapping(value = "/queryStudent", method = RequestMethod.POST)
    @ResponseBody
    public ResultData queryStudent(@RequestBody JSONObject reqBody) throws URISyntaxException, IOException, ParseException {
        //导入环境
        int environmentTag = reqBody.getInteger("environment");

        /**5、获取爱校管token信息-助教端*/
        JSONObject assistantTeacherInfo = reqBody.getJSONObject("assistantTeacherInfo");
        if (assistantTeacherInfo == null) {
            return new ResultData("入参助教构信息有误！", 400);
        }
        log.info("assistantTeacherInfo" + assistantTeacherInfo);
        JSONObject aXXAssistantTokenInfo = reqBody.getJSONObject("assistantTeacherInfo");

        JSONObject informInfo = reqBody.getJSONObject("inputForm");
        String telephone=informInfo.getString("studentTelephone");
        Integer number=informInfo.getInteger("studentNumber");

        operateStudentService.insertStudentClass(telephone,number,aXXAssistantTokenInfo,environmentTag);
        return new ResultData("学生信息同步成功", 200);
    }

    /**
     * http://localhost:9098/queryAssistant
     *
     * @return
     */
    @RequestMapping(value = "/queryAssistant", method = RequestMethod.POST)
    @ResponseBody
    public ResultData queryAssistant(@RequestBody JSONObject reqBody) throws URISyntaxException, IOException, ParseException {
        //导入环境
        int environmentTag = reqBody.getInteger("environment");

        /**5、获取爱校管token信息-助教端*/
        JSONObject assistantTeacherInfo = reqBody.getJSONObject("assistantTeacherInfo");
        if (assistantTeacherInfo == null) {
            return new ResultData("入参助教构信息有误！", 400);
        }
        log.info("assistantTeacherInfo" + assistantTeacherInfo);
        JSONObject aXXAssistantTokenInfo = reqBody.getJSONObject("assistantTeacherInfo");

        JSONObject informInfo = reqBody.getJSONObject("inputForm");
        String telephone=informInfo.getString("assistantTelephone");
        Integer number=informInfo.getInteger("assistantNumber");
        operateAssistantService.insertAssistantTeacherClass(telephone,number,aXXAssistantTokenInfo,environmentTag);
        return new ResultData("助教信息同步成功", 200);
    }
    /**
     * http://localhost:9098/queryLecturer
     *
     * @return
     */
    @RequestMapping(value = "/queryLecturer", method = RequestMethod.POST)
    @ResponseBody
    public ResultData queryLecturer(@RequestBody JSONObject reqBody) throws URISyntaxException, IOException, ParseException {
        //导入环境
        int environmentTag = reqBody.getInteger("environment");

        /**1、获取爱校管token信息-主教端*/
        JSONObject teacherInfo = reqBody.getJSONObject("producerTeacherInfo");
        if (teacherInfo == null) {
            return new ResultData("入参主讲机构信息有误！", 400);
        }
        log.info("教师teacherInfo" + teacherInfo);
        LecturerTeacherTokenInfo axgTokenInfo = crateStreamingClassService.getAXGTokenInfo(teacherInfo);
        log.info("教师token" + axgTokenInfo);

        JSONObject informInfo = reqBody.getJSONObject("inputForm");
        String telephone=informInfo.getString("lectureTelephone");
        Integer number=informInfo.getInteger("lectureNumber");
        operateLecturerService.insertLecturerTeacherClass(telephone,number,axgTokenInfo,environmentTag);
        return new ResultData("主讲信息同步成功", 200);
    }
}
