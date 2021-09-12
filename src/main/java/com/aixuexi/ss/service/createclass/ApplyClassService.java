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
     * 获取爱笑管登录token
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
        log.info("获取助教机构Token请求信息：" + assistantObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(assistantObj, headers, POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("获取助教机构Token返回信息：" + repObj.toJSONString());
        JSONObject repBody = repObj.getJSONObject("body");
        aXXAssistantTokenInfo.setPtpc(repBody.getString("axxProof"));
        aXXAssistantTokenInfo.setUserId(repBody.getString("userId"));
        log.info("assistantInfo信息：" + assistantInfo.toString());
        return aXXAssistantTokenInfo;
    }

    /**
     * AI课程单价获取以及lessonIds获取
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
        log.info("AI课程单价获取以及lessonIds获取的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("AI课程单价获取以及lessonIds获取接口返回的信息是：" + repObj.toJSONString());

        JSONObject messageObj = new JSONObject();
        /**根据返回的json，获取单价*/
        JSONObject detailObj = repObj.getJSONObject("body");
        JSONObject moneyObj = detailObj.getJSONObject("basicInfo");
        double moneyString = moneyObj.getDouble("oneMaxPrice");
        log.info("AI好课的单价：" + moneyString);

        /**根据返回的json，获取lessonIds*/
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
     * 购买AI好课程
     *
     * @param applyClassInfo
     * @param aXXAssistantTokenInfo
     * @param classId
     * @returnapplyClass
     */
    public JSONObject applyAIClass(JSONObject applyClassInfo, JSONObject aXXAssistantTokenInfo, Integer classId) throws URISyntaxException {
        JSONObject aiClassDetail = aiClassDetail(classId, aXXAssistantTokenInfo);
        //获取lessonIds
        String lessonIds = aiClassDetail.getString("lessonIds");
        //获取lessonIdsNumber
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
        log.info("购买AI课程的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("购买AI课程接口返回的信息是：" + repObj.toJSONString());
        return repObj;
    }


    /**
     * 直播课程单价获取以及lessonIds获取
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
        log.info("购买直播课程的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("购买直播课程接口返回的信息是：" + repObj.toJSONString());

        JSONObject messageObj = new JSONObject();
        /**根据返回的json，获取单价*/
        JSONObject detailObj = repObj.getJSONObject("body");
        JSONObject moneyObj = detailObj.getJSONObject("basicInfo");
        double moneyString = moneyObj.getDouble("twoMaxPrice");
        log.info("直播好课的单价：" + moneyString);

        /**根据返回的json，获取lessonIds*/
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
     * 购买直播课程
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
        log.info("购买直播课程的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("购买直播课程接口返回的信息是：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 添加辅导老师
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
        log.info("selectBySpGradeMust4GroupByAlpha的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("添加辅导老师的selectBySpGradeMust4GroupByAlpha接口返回信息：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 添加辅导老师
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
        log.info("添加辅导老师checkSchedules请求信息：" + checkSchedulesObj.toJSONString());

        RequestEntity requestEntity = new RequestEntity<>(checkSchedulesObj, crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("添加辅导老师checkSchedules接口返回信息：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 添加辅导老师
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
        log.info("添加辅导老师的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(headers, POST, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("添加辅导老师的接口返回信息：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 将学生信息插入数据库，并获取学生手机号码集合信息
     *
     * @param applyClassInfo
     * @param aXXAssistantTokenInfo
     * @return
     */
    public HashMap<Integer,String> getStudentMessage(JSONObject aXXAssistantTokenInfo, JSONObject applyClassInfo, Integer classId, int environmentTag) throws URISyntaxException {
        //获取班额
        int purchasedStuNumber = Integer.valueOf(applyClassInfo.getString("purchasedStuNumber"));
        log.info("purchasedStuNumber请求信息：" + purchasedStuNumber);
        //设置一个初始的学生的手机号码，按照班额添加学生
        HashMap<Integer,String> studentListObj = new HashMap<Integer,String>();
        String nowStudentTell;
        for (int i = 0; i < purchasedStuNumber; i++) {
            try {
                //随机选取学生
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
        //添加学生
        refStudent(classId, aXXAssistantTokenInfo, purchasedStuNumber, studentListObj);
        return studentListObj;
    }

    /**
     * 添加学生
     *
     * @param classId
     * @param aXXAssistantTokenInfo
     * @param purchasedStuNumber
     * @return
     */
    public JSONObject refStudent(Integer classId, JSONObject aXXAssistantTokenInfo, int purchasedStuNumber, HashMap<Integer,String> studentListObj) throws URISyntaxException {
        //给添加学生接口refStudent添加json
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
        log.info("添加学生请求信息：" + refStudentObj.toJSONString());

        //发送post请求
        RequestEntity requestEntity = new RequestEntity<>(refStudentObj, crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("添加学生接口返回信息：" + repObj.toJSONString());
        return repObj;
    }

    public static void main(String[] args) {
        Random random = new Random();
        int initStudent = random.nextInt(30);
    }
}
