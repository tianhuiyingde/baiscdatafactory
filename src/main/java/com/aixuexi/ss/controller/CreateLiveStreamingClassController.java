package com.aixuexi.ss.controller;

import com.aixuexi.ss.common.dns.LoadDns;
import com.aixuexi.ss.common.response.ResultData;
import com.aixuexi.ss.entity.AssistantTeacherTokenInfo;
import com.aixuexi.ss.entity.ClassMessage;
import com.aixuexi.ss.entity.LecturerTeacherTokenInfo;
import com.aixuexi.ss.service.operatesql.OperateClassMessageService;
import com.aixuexi.ss.service.operatesql.OperationLogSqlService;
import com.aixuexi.ss.service.createclass.CrateStreamingClassService;
import com.aixuexi.ss.service.createclass.ApplyClassService;

import com.aixuexi.ss.vo.ClassMessageVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 新建直播課
 */
@Slf4j
@Controller
@CrossOrigin
@RequestMapping("CreateLiveStreaming")
@Api(tags = "直播课创建")
public class CreateLiveStreamingClassController {

    @Autowired
    CrateStreamingClassService crateStreamingClassService;
    @Autowired
    ApplyClassService applyClassService;
    @Autowired
    private OperationLogSqlService insertOperationLogService;
    @Autowired
    private OperateClassMessageService insertClassMessageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultData Create(@RequestBody JSONObject reqBody) throws URISyntaxException, IOException, ParseException {
        /**创建课程环境*/
        log.info("建课环境开始：");
        int environmentTag = reqBody.getInteger("environment");
        if (environmentTag == 1) {
            LoadDns.initDns(1);
        } else {
            LoadDns.initDns(0);
        }

        //创建课程记录插入数据库
        log.info("操作记录插入数据库");
        int classType = 0;
        this.insertOperationLogService.insertoperationLogClass(classType);
        log.info("reqBody:" + reqBody.toJSONString());

        log.info("直播课创建自动化流程开始-----------------------------------主讲机构登录");
        /**1、获取爱校管token信息-主教端*/
        JSONObject teacherInfo = reqBody.getJSONObject("producerTeacherInfo");
        if (teacherInfo == null) {
            return new ResultData("入参主讲机构信息有误！", 400);
        }
        log.info("教师teacherInfo" + teacherInfo);
        JSONObject axgTokenInfo = reqBody.getJSONObject("producerTeacherInfo");
        log.info("教师token" + axgTokenInfo);

        /**2创建直播课*/
        JSONObject courseInfo = reqBody.getJSONObject("courseInfo");
        if (teacherInfo == null) {
            return new ResultData("入参课程信息有误！", 400);
        }
        log.info("直播课创建开始");
        log.info("创建直播课请求信息：" + courseInfo.toJSONString());

        JSONObject repInfo = crateStreamingClassService.createLive(courseInfo, axgTokenInfo);
        if (repInfo == null) {
            return new ResultData("创建直播课过程中发生错误！repsone null", 500);
        }
        if (repInfo.getInteger("status") == 0) {
            log.error(repInfo.getString("errorMessage"));
            return new ResultData("创建直播课过程中发生错误！", 500);
        }
        Integer classId = repInfo.getInteger("body");
        log.info("直播课已创建成功,classId:" + classId);

        /** 3、直播课排课*/
        log.info("开始排课：选中时间");
        Date dateTimeInfo = reqBody.getTimestamp("selectDatetime");

        Integer classTypeId = courseInfo.getInteger("classType");
        JSONObject repObj = crateStreamingClassService.initSchedule(classId, classTypeId, axgTokenInfo,dateTimeInfo);
        if (repObj == null) {
            return new ResultData("排课过程中发生错误！repsone null", 500);
        }
        if (repObj.getInteger("status") == 0) {
            log.error(repObj.getString("errorMessage"));
            return new ResultData("排课过程中发生错误！", 500);
        }
        log.info("排课成功");

        /**4上架直播课程*/
        log.info("开始上架直播课程");
        JSONObject repUpdateObj = crateStreamingClassService.updateClassRelease(classId, axgTokenInfo);
        if (repUpdateObj == null) {
            return new ResultData("上架Ai好课程过程中发生错误！repsone null", 500);
        }
        if (repUpdateObj.getInteger("status") == 0) {
            log.error(repUpdateObj.getString("errorMessage"));
            return new ResultData("上架Ai好课程过程中发生错误！", 500);
        }
        log.info("Ai好课程已上架");

        /**5、获取爱校管token信息-助教端*/
        JSONObject assistantTeacherInfo = reqBody.getJSONObject("assistantTeacherInfo");
        if (assistantTeacherInfo == null) {
            return new ResultData("入参助教构信息有误！", 400);
        }
        log.info("assistantTeacherInfo" + assistantTeacherInfo);
        JSONObject aXXAssistantTokenInfo = reqBody.getJSONObject("assistantTeacherInfo");
        log.info("教师token" + axgTokenInfo);

        /**6、助教端购买课程*/
        JSONObject applyClassInfo = reqBody.getJSONObject("applyClassInfo");
        if (applyClassInfo == null) {
            return new ResultData("购买课程信息有误！", 400);
        }
        //获取lessonIds
        List<Integer> lessonSb = crateStreamingClassService.getClassLessonid(repObj);

        JSONObject applyObj = applyClassService.applyLiveClass(applyClassInfo, aXXAssistantTokenInfo, classId, lessonSb);
        if (applyObj == null) {
            return new ResultData("助教机构登录发生错误！", 400);
        }

       /**6、主讲审核课程*/
        log.info("applyId:" + applyObj.toJSONString());
        int applyId = Integer.valueOf(applyObj.getString("body"));
/*        JSONObject applyPassObj = crateStreamingClassService.applyPass(applyId, axgTokenInfo);
        if (applyPassObj == null) {
            return new ResultData("主讲审核课程发生错误！", 400);
        }*/

        /**7、添加辅导老师*/
        JSONObject assistantInfo = reqBody.getJSONObject("assistantInfo");
        JSONArray teacherArray = assistantInfo.getJSONArray("teacherId");
        int subClassNum = applyClassInfo.getInteger("subClassNum");
        List<List<ClassMessageVo>> listSearchClassByIds = new ArrayList<>();
        for (int i = 0; i < subClassNum; i++) {
            log.info("循环第几轮:" + (i+1));
            /**7、查询子课程的subClassId*/
            Integer subClassIdStr = crateStreamingClassService.queryDetails(applyId, axgTokenInfo,i);

            Integer teacherId = 0;
            if(teacherArray.size() == 1){
                teacherId = teacherArray.getInteger(0);//如果助教只传递了一个，则都显示第一个
            }else{
                teacherId = teacherArray.getInteger(i);
            }

            log.info("辅导老师的teacherID:" + teacherId+"   "+teacherArray.toJSONString());

            JSONObject selectBySpGradeMust4GroupByAlphaObj = applyClassService.selectBySpGradeMust4GroupByAlpha(subClassIdStr, aXXAssistantTokenInfo);
            JSONObject checkSchedulesObj = applyClassService.checkSchedules(subClassIdStr, teacherId, aXXAssistantTokenInfo);
            JSONObject refTeacherObj = applyClassService.refTeacher(subClassIdStr, teacherId, aXXAssistantTokenInfo);
            if (refTeacherObj == null && checkSchedulesObj == null && selectBySpGradeMust4GroupByAlphaObj == null) {
                return new ResultData("添加辅导老师发生错误！", 400);
            }

            /**7、添加学生*/
            HashMap<Integer, String> refStudentObj = applyClassService.getStudentMessage(aXXAssistantTokenInfo, applyClassInfo, subClassIdStr, environmentTag);
            if (refStudentObj == null) {
                return new ResultData("添加学生发生错误！", 400);
            }

            /**获取所有学生的电话号码*/
            Collection<String> values = refStudentObj.values();
            Iterator<String> iterator2 = values.iterator();
            StringBuffer refStudentStr = new StringBuffer();
            while (iterator2.hasNext()) {
                refStudentStr.append(iterator2.next() + ",");
            }
            String refStudentStrNew = refStudentStr.toString().substring(0, refStudentStr.toString().length() - 1);
            System.out.print(refStudentStrNew + "refStudentStrNew ");

            /**8、课程信息保存数据库*/
            //课程信息json
            JSONObject myClassCreateObj = new JSONObject();
            myClassCreateObj.put("bigClassId", classId);
            myClassCreateObj.put("className", courseInfo.getString("className"));
            myClassCreateObj.put("studentNumber", applyClassInfo.getString("purchasedStuNumber"));
            myClassCreateObj.put("classType", 0);
            myClassCreateObj.put("lecturerId", courseInfo.getJSONArray("teachers").getJSONObject(0).getString("teacherId"));
            myClassCreateObj.put("assistantId", teacherId);
            myClassCreateObj.put("userId", 1);
            myClassCreateObj.put("studentIds", refStudentStrNew);
            this.insertClassMessageService.insertclassMessageClass(myClassCreateObj, environmentTag);

            List<ClassMessageVo> listSearchClassById = this.insertClassMessageService.listSearchClassById(classId);
            listSearchClassByIds.add(listSearchClassById);
        }
        return new ResultData(200, listSearchClassByIds);
    }

}
