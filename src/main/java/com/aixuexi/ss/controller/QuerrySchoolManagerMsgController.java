package com.aixuexi.ss.controller;

import com.aixuexi.ss.common.dns.LoadDns;
import com.aixuexi.ss.common.response.ResultData;
import com.aixuexi.ss.entity.LecturerTeacherTokenInfo;
import com.aixuexi.ss.service.createclass.CrateStreamingClassService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

@Slf4j
@Controller
@RequestMapping("/QuerrySchoolManagerMsg")
@CrossOrigin
public class QuerrySchoolManagerMsgController {

    @Autowired
    CrateStreamingClassService crateStreamingClassService;

    /**
     * 获取学科信息
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    public ResultData getAllToken(@RequestBody String reqBody) throws URISyntaxException {
        JSONObject reqBodys = JSON.parseObject(reqBody);
        /**1、获取爱校管token信息-主教端*/
        JSONObject proTeacherInfo = reqBodys.getJSONObject("producerTeacherInfo");
        JSONObject AssTeacherInfo = reqBodys.getJSONObject("assistantTeacherInfo");
        LecturerTeacherTokenInfo proAxgTokenInfo = crateStreamingClassService.getAXGTokenInfo(proTeacherInfo);
        LecturerTeacherTokenInfo assAxgTokenInfo = crateStreamingClassService.getAXGTokenInfo(AssTeacherInfo);
        JSONObject repInfo = new JSONObject();
        repInfo.put("proAxgTokenInfo",proAxgTokenInfo);
        repInfo.put("assAxgTokenInfo",assAxgTokenInfo);
        return new ResultData(200, repInfo);
    }
    @RequestMapping(value = "/getAssToken", method = RequestMethod.POST)
    @ResponseBody
    public ResultData getToken(@RequestBody String reqBody) throws URISyntaxException {
        JSONObject reqBodys = JSON.parseObject(reqBody);
        /**1、获取爱校管token信息-主教端*/
        JSONObject AssTeacherInfo = reqBodys.getJSONObject("assistantTeacherInfo");
        LecturerTeacherTokenInfo assAxgTokenInfo = crateStreamingClassService.getAXGTokenInfo(AssTeacherInfo);
        JSONObject repInfo = new JSONObject();
        repInfo.put("assAxgTokenInfo",assAxgTokenInfo);
        return new ResultData(200, repInfo);
    }
    /**
     * 获取学科信息
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/listSubjectByPeriod", method = RequestMethod.POST)
    @ResponseBody
    public ResultData listSubjectByPeriod(@RequestBody JSONObject reqBody) throws URISyntaxException
    {
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        log.info("教师token:" + axgTokenInfo);

        int period=reqBody.getInteger("semesterSelected");
        JSONObject repInfo = crateStreamingClassService.listSubjectByPeriod(period,axgTokenInfo);
        if (repInfo == null) {
            return new ResultData("爱笑管-根据学期展现学科过程中发生错误！repsone null", 500);
        }
        return new ResultData(200, repInfo);
    }

    /**
     * 获取年级信息
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/listForCreateLiveClass", method = RequestMethod.POST)
    @ResponseBody
    public ResultData listForCreateLiveClass(@RequestBody JSONObject reqBody) throws URISyntaxException {
        /**1、获取爱校管token信息-主教端*/
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        log.info("教师token" + axgTokenInfo);
        int period=reqBody.getInteger("semesterSelected");
        int subjectProductId=reqBody.getInteger("subjectProductId");
        JSONObject repInfo = crateStreamingClassService.listForCreateLiveClass(period,subjectProductId,axgTokenInfo);
        if (repInfo == null) {
            return new ResultData("爱笑管-根据学科展现年级过程中发生错误！repsone null", 500);
        }
        return new ResultData(200, repInfo);
    }

    /**
     * 获取班型列表
     * @param reqBody
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/classTypeList", method = RequestMethod.POST)
    @ResponseBody
    public ResultData classTypeList(@RequestBody JSONObject reqBody) throws URISyntaxException {
        /**1、获取爱校管token信息-主教端*/
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        log.info("教师token" + axgTokenInfo);

        JSONObject repInfo = crateStreamingClassService.classTypeList(reqBody,axgTokenInfo);
        if (repInfo == null) {
            return new ResultData("爱笑管-根据年级课程类型展现班型过程中发生错误！repsone null", 500);
        }
        return new ResultData(200, repInfo);
    }

    /**
     * 查询爱笑管存储的课型信息（来源生产系统）
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/aicourseList", method = RequestMethod.POST)
    @ResponseBody
    public ResultData aicourseList(@RequestBody JSONObject reqBody) throws URISyntaxException {
        //1、获取爱校管token信息-主教端
        Integer classTypeId = reqBody.getInteger("classTypeId");
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        log.info("教师token" + axgTokenInfo);

        JSONObject repInfo = crateStreamingClassService.getAICourseLsit(classTypeId,axgTokenInfo);
        if (repInfo == null) {
            return new ResultData("爱笑管-根据班型ID获取AI课型ID出错！repsone null", 500);
        }
        return new ResultData(200, repInfo);
    }

    /**
     * 查询爱笑管主讲信息
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/teacherList", method = RequestMethod.POST)
    @ResponseBody
    public ResultData getProteacherList(@RequestBody JSONObject reqBody) throws URISyntaxException {
        //1、获取爱校管token信息-主教端
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        log.info("教师token" + axgTokenInfo);

        JSONObject repInfo = crateStreamingClassService.getTeacherList(reqBody,axgTokenInfo);
        if (repInfo == null) {
            return new ResultData("爱笑管-根据班型ID获取AI课型ID出错！repsone null", 500);
        }
        return new ResultData(200, repInfo);
    }

    /**
     * 查询爱笑管主讲信息
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/assTeacherList", method = RequestMethod.POST)
    @ResponseBody
    public ResultData getAssteacherList(@RequestBody JSONObject reqBody) throws URISyntaxException {

        //1、获取爱校管token信息-主教端
        JSONObject axgTokenInfo = reqBody.getJSONObject("assAxgTokenInfo");
        log.info("听课机构token" + axgTokenInfo);

        JSONArray repInfo = crateStreamingClassService.getAssistantTeacherList(reqBody,axgTokenInfo);
        if (repInfo == null) {
            return new ResultData("获取听课机构辅导老师列表出错！repsone null", 500);
        }
        return new ResultData(200, repInfo);
    }


}
