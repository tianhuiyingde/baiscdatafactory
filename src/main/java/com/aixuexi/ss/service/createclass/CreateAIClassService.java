package com.aixuexi.ss.service.createclass;

import com.aixuexi.ss.common.response.ResultData;
import com.aixuexi.ss.common.util.TimeUtil;
import com.aixuexi.ss.service.operatesql.OperateClassMessageService;
import com.aixuexi.ss.service.operatesql.OperationLogSqlService;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
@Slf4j
@Service
@Configuration
public class CreateAIClassService {
    @Value("${liveclass.axgGetTokenUrl}")
    private String axgGetTokenUrl;
    @Value("${liveclass.createLiveUrl}")
    private String createLiveUrl;
    @Value("${liveclass.initScheduleUrl}")
    private String initScheduleUrl;
    @Value("${liveclass.updateClassReleaseUrl}")
    private String updateClassReleaseUrl;
    @Value("${liveclass.applyPassUrl}")
    private String applyPassUrl;
    @Value("${common.queryDetailsUrl}")
    private String queryDetailsUrl;
    @Value("${aiclass.createAiClassUrl}")
    private String createAiClassUrl;
    @Value("${aiclass.listByPeriodUrl}")
    private String listByPeriodUrl;
    @Value("${aiclass.listForCreateLiveClassUrl}")
    private String listForCreateLiveClassUrl;
    @Value("${aiclass.classTypeListUrl}")
    private String classTypeListUrl;
    @Value("${aiclass.marketSettingUrl}")
    private String marketSettingUrl;
    @Value("${aiclass.completionUrl}")
    private String completionUrl;
    @Value("${aiclass.dtMarketSettingUrl}")
    private String dtMarketSettingUrl;
    @Value("${aiclass.aiCourseListUrl}")
    private String  aiCourseListUrl;
    @Value("${aiclass.teacherListUrl}")
    private String teacherListUrl;
    @Value("${aiclass.assistantTeacherListUrl}")
    private String assistantTeacherListUrl;

    RestTemplate restTemplate = new RestTemplate();
    TimeUtil timeUtil = new TimeUtil();
    @Autowired
    ApplyClassService applyClassService;
    @Autowired
    private OperationLogSqlService insertOperationLogService;
    @Autowired
    private OperateClassMessageService insertClassMessageService;
    @Autowired
    CrateStreamingClassService crateStreamingClassService;
    /**
     * ??????Ai??????
     * @return
     */
    public JSONObject createAiClass(JSONObject reqBody) throws URISyntaxException {
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        JSONObject basicAiInfo = reqBody.getJSONObject("basicAiInfo");
        URI initUrl = new URI(createAiClassUrl);
        log.info("???????????????AI??????????????????"+basicAiInfo.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(basicAiInfo, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("???????????????AI??????????????????"+repObj.toJSONString());
        Integer classId = repObj.getInteger("body");
        //????????????Ai??????
        marketSetting(classId,axgTokenInfo);
        completionUrl(classId,axgTokenInfo);
        dtMarketSetting(classId,axgTokenInfo);
        log.info("Ai?????????????????????,??????ID:{},??????????????????" , classId);
        /**????????????*/
        repObj = updateClassRelease(classId, reqBody);
        if (repObj == null || repObj.getInteger("status") == 0) {
            log.error("????????????????????????????????????errorMessage:{}",repObj.getString("errorMessage"));
        }
        repObj.put("classId",classId);
        log.info("???????????????lessonId:{},???????????????????????????" , repObj.toJSONString());
        return repObj;
    }

    /**
     *
     * @param reqBody
     * @param classId
     * @return
     */
    public boolean buyAIClass(JSONObject reqBody,Integer classId) throws URISyntaxException, IOException, ParseException {
        /**5??????????????????token??????-?????????*/
        JSONObject assistantTeacherInfo = reqBody.getJSONObject("assAxgTokenInfo");
        /**6????????????????????????*/
        JSONObject applyClassInfo = reqBody.getJSONObject("applyClassInfo");
        JSONObject applyObj = applyClassService.applyAIClass(applyClassInfo, assistantTeacherInfo, classId);
        if("0".equals(applyObj.getString("status")))
        {
            return false;
        }
        /**6????????????????????? ????????????????????????????????????*/
        int applyId = applyObj.getInteger("body");
        log.info("applyId:" + applyId);
        /*log.info("applyId:" + applyId);
        JSONObject applyPassObj = crateStreamingClassService.applyPass(applyId, axgTokenInfo);
        if (applyPassObj == null) {
            return new ResultData("?????????????????????????????????", 400);
        }*/
        /**7?????????????????????*/
        //??????????????????????????????
        JSONObject assistantInfo = reqBody.getJSONObject("assistantInfo");
        Integer teacherId = assistantInfo.getInteger("teacherId");
        int subClassNum = applyClassInfo.getInteger("subClassNum");
        List<List<ClassMessageVo>> listSearchClassByIds = new ArrayList<>();
        JSONObject courseInfo = reqBody.getJSONObject("basicAiInfo");
        /**1??????????????????token??????-?????????*/
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        for(int i=0;i<subClassNum;i++) {
            /**7?????????????????????subClassId*/
            Integer subClassIdStr = crateStreamingClassService.queryDetails(applyId, axgTokenInfo,i);
            JSONObject selectBySpGradeMust4GroupByAlphaObj = applyClassService.selectBySpGradeMust4GroupByAlpha(subClassIdStr, assistantTeacherInfo);
            JSONObject checkSchedulesObj = applyClassService.checkSchedules(subClassIdStr, teacherId, assistantTeacherInfo);
            JSONObject refTeacherObj = applyClassService.refTeacher(subClassIdStr, teacherId, assistantTeacherInfo);
            if (refTeacherObj == null && checkSchedulesObj == null && selectBySpGradeMust4GroupByAlphaObj == null) {
                log.error("??????????????????:"+teacherId+"???????????????");
                continue;
            }
            /**7???????????????*/
            HashMap<Integer, String> refStudentObj = applyClassService.getStudentMessage(assistantTeacherInfo, applyClassInfo, subClassIdStr, 1);
            if (refStudentObj == null) {
                log.error("??????ID??????"+subClassIdStr+"???????????????????????????????????????");
                continue;
            }
            /**?????????????????????????????????*/
            Collection<String> values = refStudentObj.values();
            Iterator<String> iterator2 = values.iterator();
            StringBuffer refStudentStr = new StringBuffer();
            while (iterator2.hasNext()) {
                refStudentStr.append(iterator2.next() + ",");
            }
            String refStudentStrNew = refStudentStr.toString().substring(0, refStudentStr.toString().length() - 1);
            log.info(refStudentStrNew + "refStudentStrNew ");

            /**8??????????????????????????????*/
            //????????????json
            JSONObject myClassCreateObj = new JSONObject();
            myClassCreateObj.put("bigClassId", classId);
            myClassCreateObj.put("className", courseInfo.getString("className"));
            myClassCreateObj.put("studentNumber", applyClassInfo.getString("purchasedStuNumber"));
            myClassCreateObj.put("classType", courseInfo.getString("classType"));
            myClassCreateObj.put("lecturerId", courseInfo.getJSONArray("teachers").getJSONObject(0).getString("teacherId"));
            myClassCreateObj.put("assistantId", teacherId);
            myClassCreateObj.put("userId", 1);
            myClassCreateObj.put("studentIds", refStudentStrNew);
            myClassCreateObj.put("assistantName",assistantInfo.getString("teacherName"));
            myClassCreateObj.put("assistantPhone",assistantInfo.getString("teacherAccount"));
            myClassCreateObj.put("subClassId",subClassIdStr);
            insertClassMessageService.insertclassMessageClass(myClassCreateObj, 1);
            List<ClassMessageVo> listSearchClassById = insertClassMessageService.listSearchClassById(classId);
            listSearchClassByIds.add(listSearchClassById);
        }
        return true;
    }
    /**
     * http??????headers
     * @param
     * @param axgTokenInfo
     * @returnapplyClass
     */
    public MultiValueMap aXGtHttpHeaders(JSONObject axgTokenInfo)  {
        MultiValueMap headers  = new LinkedMultiValueMap<>();
        headers.add("Content-Type","application/json");
        headers.add("ptpc",axgTokenInfo.getString("ptpc"));
        headers.add("userId",axgTokenInfo.getString("userId"));
        headers.add("token","axx");
        return headers;
    }
    /**
     * ????????????Ai??????,??????????????????
     * @param classId
     * @param axgTokenInfo
     * @return
     */
    public JSONObject marketSetting(Integer classId, JSONObject axgTokenInfo) throws URISyntaxException {
        String newQueryDetailUrl=marketSettingUrl
                +"?classId="+classId;
        log.info("????????????Ai???????????????"+newQueryDetailUrl);
        URI initUrl = new URI(newQueryDetailUrl);
        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("????????????Ai???????????????????????????"+repObj.toJSONString());
        return repObj;
    }
    /**
     * ?????????????????????
     * @param classId
     * @param axgTokenInfo
     * @return
     */
    public JSONObject completionUrl(Integer classId, JSONObject axgTokenInfo) throws URISyntaxException {
        String newQueryDetailUrl=completionUrl
                +"?classId="+classId;
        log.info("??????????????????????????????"+newQueryDetailUrl);
        URI initUrl = new URI(newQueryDetailUrl);
        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("????????????????????????????????????"+repObj.toJSONString());
        return repObj;
    }
    /**
     * ??????????????????
     * @param classId
     * @param axgTokenInfo
     * @return
     */
    public JSONObject dtMarketSetting(Integer classId, JSONObject axgTokenInfo) throws URISyntaxException {
        JSONObject settingObj = new JSONObject();
        settingObj.put("access",0);
        settingObj.put("accessLimit",false);
        settingObj.put("availableTime",timeUtil.dateAdd(timeUtil.getCurrentDateString(),-1)+"T16:00:00.000Z");
        settingObj.put("classId",classId);
        settingObj.put("countNum",50);
        settingObj.put("hotSaleFlag","0");
        settingObj.put("mallLink","");
        settingObj.put("qrCode","");
        settingObj.put("saleDate","");
        URI initUrl = new URI(dtMarketSettingUrl);
        log.info("??????????????????????????????"+settingObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(settingObj, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("??????????????????????????????"+repObj.toJSONString());
        return repObj;
    }
    /**
     * ????????????
     * @param classId
     * @param reqBody
     * @return
     */
    public JSONObject updateClassRelease(Integer classId, JSONObject reqBody) throws URISyntaxException {
        JSONObject axgTokenInfo = reqBody.getJSONObject("proAxgTokenInfo");
        JSONObject updateClassObj = new JSONObject();
        updateClassObj.put("dtMainClassId",classId);
        updateClassObj.put("classRelease",1);
        URI updateClassUrl = new URI(updateClassReleaseUrl);
        log.info("?????????????????????????????????"+updateClassObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(updateClassObj, aXGtHttpHeaders(axgTokenInfo), POST, updateClassUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("?????????????????????????????????"+repObj.toJSONString());
        return repObj;
    }

    public static void main(String[] args) {
        TimeUtil timeUtil = new TimeUtil();
        System.out.println(timeUtil.dateAdd(timeUtil.getCurrentDateString(),-1)+"T16:00:00.000Z");
    }

}
