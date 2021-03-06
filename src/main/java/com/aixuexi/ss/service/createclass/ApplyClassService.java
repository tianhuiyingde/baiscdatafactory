package com.aixuexi.ss.service.createclass;

import com.aixuexi.ss.entity.AssistantTeacherTokenInfo;
import com.aixuexi.ss.common.util.TimeUtil;

import com.aixuexi.ss.entity.ClassMessage;
import com.aixuexi.ss.entity.Student;
import com.aixuexi.ss.service.operatesql.OperateStudentService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;

@Service
@Slf4j
@Configuration
public class ApplyClassService {
    @Value("${liveclass.axgGetTokenUrl}")
    private String axgGetTokenUrl;
    @Value("${liveclass.applyClassUrl}")
    private String applyClassUrl;
    @Value("${liveclass.classLessonIdUrl}")
    private String classLessonIdUrl;
    @Value("${common.refTeacherUrl}")
    private String refTeacherUrl;
    @Value("${common.refStudentUrl}")
    private String refStudentUrl;
    @Value("${common.checkSchedulesUrl}")
    private String checkSchedulesUrl;
    @Value("${common.selectBySpGradeMust4GroupByAlphaUrl}")
    private String selectBySpGradeMust4GroupByAlphaUrl;
    @Value("${common.listStudentsUrl}")
    private String listStudentsUrl;
    @Value("${common.aiClassDetailUrl}")
    private String aiClassDetailUrl;
    @Value("${common.liveDetailUrl}")
    private String liveDetailUrl;

    @Autowired
    private OperateStudentService operateStudentService;
    @Autowired CrateStreamingClassService crateStreamingClassService;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * ?????????????????????token
     *
     * @param assistantInfo
     * @return
     */
    public AssistantTeacherTokenInfo getAXXAssistantTokenInfo(JSONObject assistantInfo) throws URISyntaxException {
        AssistantTeacherTokenInfo aXXAssistantTokenInfo = new AssistantTeacherTokenInfo();
        Integer loginType = 1;
        String loginSystem = "pc";
        JSONObject assistantObj = new JSONObject();
        assistantObj.put("username", assistantInfo.getString("userName"));
        assistantObj.put("password", assistantInfo.getString("passWord"));
        assistantObj.put("loginType", loginType);
        assistantObj.put("loginSystem", loginSystem);
        MultiValueMap headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        URI url = new URI(axgGetTokenUrl);
        log.info("??????????????????Token???????????????" + assistantObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(assistantObj, headers, POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("??????????????????Token???????????????" + repObj.toJSONString());
        JSONObject repBody = repObj.getJSONObject("body");
        aXXAssistantTokenInfo.setPtpc(repBody.getString("axxProof"));
        aXXAssistantTokenInfo.setUserId(repBody.getString("userId"));
        log.info("assistantInfo?????????" + assistantInfo.toString());
        return aXXAssistantTokenInfo;
    }

    /**
     * AI????????????????????????lessonIds??????
     *
     * @param classId
     * @returnapplyClass
     */
    public JSONObject aiClassDetail(Integer classId, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        String valueUrl = aiClassDetailUrl
                + "?classId=" + classId
                + "&doubleTeacherSource=" + "aihaoke"
                + "&localBuyDtLive=";
        String valueUrlDelOne = valueUrl.replace("[", "");
        String valueUrlDelTwo = valueUrlDelOne.replace("]", "");
        String valueUrlDelThree = valueUrlDelTwo.replaceAll("\\s*", "");
        log.info("AI????????????????????????lessonIds?????????url????????????" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("AI????????????????????????lessonIds?????????????????????????????????" + repObj.toJSONString());

        JSONObject messageObj = new JSONObject();
        /**???????????????json???????????????*/
        JSONObject detailObj = repObj.getJSONObject("body");
        JSONObject moneyObj = detailObj.getJSONObject("basicInfo");
        double moneyString = moneyObj.getDouble("oneMaxPrice");
        log.info("AI??????????????????" + moneyString);

        /**???????????????json?????????lessonIds*/
        JSONArray scheduleVosObj = detailObj.getJSONArray("scheduleVos");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < scheduleVosObj.size(); i++) {
            JSONObject jsonObj = scheduleVosObj.getJSONObject(i);
            Integer lessonId = Integer.valueOf(jsonObj.getString("lessonId"));
            list.add(lessonId);
        }

        messageObj.put("money", moneyString);
        messageObj.put("lessonIds", list.toString());
        messageObj.put("lessonIdsNumber", list.size());
        return messageObj;
    }

    /**
     * ??????AI?????????
     *
     * @param applyClassInfo
     * @param aXXAssistantTokenInfo
     * @param classId
     * @returnapplyClass
     */
    public JSONObject applyAIClass(JSONObject applyClassInfo, JSONObject aXXAssistantTokenInfo, Integer classId) throws URISyntaxException {
        JSONObject aiClassDetail = aiClassDetail(classId, aXXAssistantTokenInfo);
        //??????lessonIds
        String lessonIds = aiClassDetail.getString("lessonIds");
        //??????lessonIdsNumber
        Integer allLessonSize = aiClassDetail.getInteger("lessonIdsNumber");
        String valueUrl = applyClassUrl
                + "?mainClassId=" + classId
                + "&subClassNum=" + applyClassInfo.getString("subClassNum")
                + "&lessonIds=" + lessonIds
                + "&buyPassedLessonIds="
                + "&isLocal=" + applyClassInfo.getString("isLocal")
                + "&purchasedStuNumber=" + applyClassInfo.getString("purchasedStuNumber")
                + "&snapshotPrice=" + aiClassDetail(classId, aXXAssistantTokenInfo).getDouble("money") * (applyClassInfo.getInteger("purchasedStuNumber") - 3)
                + "&allLessonSize=" + allLessonSize;
        String valueUrlDelOne = valueUrl.replace("[", "");
        String valueUrlDelTwo = valueUrlDelOne.replace("]", "");
        String valueUrlDelThree = valueUrlDelTwo.replaceAll("\\s*", "");
        log.info("??????AI?????????url????????????" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("??????AI?????????????????????????????????" + repObj.toJSONString());
        return repObj;
    }


    /**
     * ??????????????????????????????lessonIds??????
     *
     * @param classId
     * @returnapplyClass
     */
    public JSONObject liveDetail(Integer classId, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        String valueUrl = liveDetailUrl
                + "?classId=" + classId
                + "&doubleTeacherSource=" + "aixuexi"
                + "&localBuyDtLive=";
        String valueUrlDelOne = valueUrl.replace("[", "");
        String valueUrlDelTwo = valueUrlDelOne.replace("]", "");
        String valueUrlDelThree = valueUrlDelTwo.replaceAll("\\s*", "");
        log.info("?????????????????????url????????????" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("?????????????????????????????????????????????" + repObj.toJSONString());

        JSONObject messageObj = new JSONObject();
        /**???????????????json???????????????*/
        JSONObject detailObj = repObj.getJSONObject("body");
        JSONObject moneyObj = detailObj.getJSONObject("basicInfo");
        double moneyString = moneyObj.getDouble("twoMaxPrice");
        log.info("????????????????????????" + moneyString);

        /**???????????????json?????????lessonIds*/
        JSONArray scheduleVosObj = detailObj.getJSONArray("scheduleVos");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < scheduleVosObj.size(); i++) {
            JSONObject jsonObj = scheduleVosObj.getJSONObject(i);
            Integer lessonId = Integer.valueOf(jsonObj.getString("lessonId"));
            list.add(lessonId);
        }

        messageObj.put("money", moneyString);
        messageObj.put("lessonIds", list.toString());
        messageObj.put("lessonIdsNumber", list.size());
        return messageObj;
    }

    /**
     * ??????????????????
     *
     * @param applyClassInfo
     * @param aXXAssistantTokenInfo
     * @param classId
     * @param lessonIds
     * @returnapplyClass
     */
    public JSONObject applyLiveClass(JSONObject applyClassInfo, JSONObject aXXAssistantTokenInfo, Integer classId, List<Integer> lessonIds) throws URISyntaxException {
        String valueUrl = applyClassUrl
                + "?mainClassId=" + classId
                + "&subClassNum=" + applyClassInfo.getString("subClassNum")
                + "&lessonIds=" + lessonIds.toString()
                + "&buyPassedLessonIds="
                + "&isLocal=" + applyClassInfo.getString("isLocal")
                + "&purchasedStuNumber=" + applyClassInfo.getString("purchasedStuNumber")
                + "&snapshotPrice=" + liveDetail(classId, aXXAssistantTokenInfo).getDouble("money") * (applyClassInfo.getInteger("purchasedStuNumber") - 3)
                + "&allLessonSize=" + lessonIds.size();
        String valueUrlDelOne = valueUrl.replace("[", "");
        String valueUrlDelTwo = valueUrlDelOne.replace("]", "");
        String valueUrlDelThree = valueUrlDelTwo.replaceAll("\\s*", "");
        log.info("?????????????????????url????????????" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("?????????????????????????????????????????????" + repObj.toJSONString());
        return repObj;
    }

    /**
     * ??????????????????
     *
     * @param classId
     * @return
     */
    public JSONObject selectBySpGradeMust4GroupByAlpha(Integer classId, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        String valueUrl = selectBySpGradeMust4GroupByAlphaUrl
                + "?subjectProductId=" + 22
                + "&gradeId=" + 2
                + "&classId=" + classId
                + "&dtType=" + 1
                + "&keywords=";

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("selectBySpGradeMust4GroupByAlpha???url????????????" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("?????????????????????selectBySpGradeMust4GroupByAlpha?????????????????????" + repObj.toJSONString());
        return repObj;
    }

    /**
     * ??????????????????
     *
     * @param classId
     * @param teacherId
     * @return
     */
    public JSONObject checkSchedules(Integer classId, Integer teacherId, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        JSONObject checkSchedulesObj = new JSONObject();
        checkSchedulesObj.put("classId", classId);
        List<Integer> teacherList = new ArrayList<Integer>();
        teacherList.add(teacherId);
        checkSchedulesObj.put("teacherId", teacherList);
        checkSchedulesObj.put("skipHolidays", false);
        checkSchedulesObj.put("againClassType", 1);
        checkSchedulesObj.put("judgeTypeStatus", 1);

        URI initUrl = new URI(checkSchedulesUrl);
        log.info("??????????????????checkSchedules???????????????" + checkSchedulesObj.toJSONString());

        RequestEntity requestEntity = new RequestEntity<>(checkSchedulesObj, crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("??????????????????checkSchedules?????????????????????" + repObj.toJSONString());
        return repObj;
    }

    /**
     * ??????????????????
     *
     * @param classId
     * @param teacherId
     * @return
     */
    public JSONObject refTeacher(Integer classId, Integer teacherId, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        MultiValueMap headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("ptpc", aXXAssistantTokenInfo.getString("ptpc"));
        headers.add("userId", aXXAssistantTokenInfo.getString("userId"));
        headers.add("token", "axx");

        String valueUrl = refTeacherUrl
                + "?classId=" + classId
                + "&teacherId=" + teacherId;

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("?????????????????????url????????????" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(headers, POST, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("??????????????????????????????????????????" + repObj.toJSONString());
        return repObj;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     *
     * @param applyClassInfo
     * @param aXXAssistantTokenInfo
     * @return
     */
    public HashMap<Integer,String> getStudentMessage(JSONObject aXXAssistantTokenInfo, JSONObject applyClassInfo, Integer classId, int environmentTag) throws URISyntaxException {
        //????????????
        int purchasedStuNumber = Integer.valueOf(applyClassInfo.getString("purchasedStuNumber"));
        log.info("purchasedStuNumber???????????????" + purchasedStuNumber);
        //?????????????????????????????????????????????????????????????????????
        HashMap<Integer,String> studentListObj = new HashMap<Integer,String>();
        String nowStudentTell;
        for (int i = 0; i < purchasedStuNumber; i++) {
            try {
                //??????????????????
                if (i < 10)
                    nowStudentTell = "13319999" + "00" + i;
                else
                    nowStudentTell = "13319999" + "0" + i;
                Student student = operateStudentService.getStudentByTelephone(nowStudentTell, environmentTag);
                studentListObj.put(student.getId(), nowStudentTell);
            }catch (Exception e)
            {
                log.error(e.getMessage());
                continue;
            }
        }
        //????????????
        refStudent(classId, aXXAssistantTokenInfo, purchasedStuNumber, studentListObj);
        return studentListObj;
    }

    /**
     * ????????????
     *
     * @param classId
     * @param aXXAssistantTokenInfo
     * @param purchasedStuNumber
     * @return
     */
    public JSONObject refStudent(Integer classId, JSONObject aXXAssistantTokenInfo, int purchasedStuNumber, HashMap<Integer,String> studentListObj) throws URISyntaxException {
        //?????????????????????refStudent??????json
        JSONObject refStudentObj = new JSONObject();
        refStudentObj.put("classId", classId);
        refStudentObj.put("source",1);
        JSONArray studentsArrayObj = new JSONArray();
        for (HashMap.Entry<Integer,String> entry : studentListObj.entrySet()) {
            JSONObject studentsObj = new JSONObject();
            studentsObj.put("id", entry.getKey());
            studentsObj.put("auditionStatus", -1);
            studentsArrayObj.add(studentsObj);
        }
        refStudentObj.put("students", studentsArrayObj);

        //post Url
        URI initUrl = new URI(refStudentUrl);
        log.info("???????????????????????????" + refStudentObj.toJSONString());

        //??????post??????
        RequestEntity requestEntity = new RequestEntity<>(refStudentObj, crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("?????????????????????????????????" + repObj.toJSONString());
        return repObj;
    }

    public static void main(String[] args) {
        Random random = new Random();
        int initStudent = random.nextInt(30);
    }
}
