package com.aixuexi.ss.service.createclass;

import com.aixuexi.ss.entity.ItvTeacherTokenInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;

/**
 * @author wangyangyang
 * @date 2020/9/14 22:35
 * @description
 **/
@Service
@Slf4j
@Configuration
public class ItvCreateClassService {
    @Value("${itv.itvLoginUrl}")
    private String itvLoginUrl;
    @Value("${itv.itvCreateAiUrl}")
    private String createLiveUrl;
    @Value("${itv.itvCreateAiUrlList}")
    private String itvCreateAiUrlList;
    @Value("${itv.itvCreateAiPublish}")
    private String itvCreateAiPublish;
    @Value("${itv.itvCreateAiLessonList}")
    private String itvCreateAiLessonList;
    @Value("${itv.itvupdateLessonAuditStatus}")
    private String itvupdateLessonAuditStatus;
    @Value("${itv.classTypeById}")
    private String classTypeById;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * 获取爱笑管登录token
     * @param teacherInfo
     * @return
     */
    public ItvTeacherTokenInfo getitvTokenInfo(JSONObject teacherInfo) throws URISyntaxException {
        ItvTeacherTokenInfo itvTokenInfo = new ItvTeacherTokenInfo();
        JSONObject reqObj = new JSONObject();
        reqObj.put("username",teacherInfo.getString("userName"));
        reqObj.put("password",teacherInfo.getString("passWord"));
        reqObj.put("loginType",1);
        reqObj.put("loginSystem","ptaxxitvpc");
        reqObj.put("userType",3);
        MultiValueMap headers  = new LinkedMultiValueMap<>();
        headers.add("Content-Type","application/json");
        URI url = new URI(itvLoginUrl);
        log.info("获取AXGToken请求信息："+reqObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(reqObj, headers, POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        JSONObject repBody = repObj.getJSONObject("body");
        itvTokenInfo.setPtpc(repBody.getString("axxProof"));
        itvTokenInfo.setUserId(repBody.getString("userId"));
        return  itvTokenInfo;
    }

    /**
     * http请求headers
     * @param
     * @param itvTokenInfo
     * @returnapplyClass
     */
    public MultiValueMap aXGtHttpHeaders(ItvTeacherTokenInfo itvTokenInfo) throws URISyntaxException {
        MultiValueMap headers  = new LinkedMultiValueMap<>();
        headers.add("Content-Type","application/json");
        headers.add("ptaxxitvpc",itvTokenInfo.getPtpc());
        headers.add("ptaxxitvpcUserId",itvTokenInfo.getUserId());
        log.info("头部信息："+"ptaxxitvpc:"+itvTokenInfo.getPtpc()+" " +"ptaxxitvpcUserId:"+itvTokenInfo.getUserId());
        return headers;
    }

    /**
     * 新建AI好课课程
     * @param classTypeId ，itvTokenInfo
     * @return
     */
    public String  getClassTypeById(String classTypeId, ItvTeacherTokenInfo itvTokenInfo) throws URISyntaxException {
        String valueUrl = classTypeById
                + "?classTypeId=" + classTypeId;

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("itvCreateAiUrlList的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(itvTokenInfo), GET, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();

        String classTypeName = repObj.getString("name");
        return classTypeName;
    }

    /**
     * 新建AI好课课程
     * @param itvCourseInfo，itvTokenInfo
     * @return
     */
    public JSONObject createAiClass(JSONObject itvCourseInfo,ItvTeacherTokenInfo itvTokenInfo) throws URISyntaxException {
        JSONObject aiClassObj = new JSONObject();
        aiClassObj.put("classStyle",itvCourseInfo.getInteger("classStyle"));
        aiClassObj.put("classTypeId",itvCourseInfo.getString("classTypeId"));
        aiClassObj.put("classTypeName",getClassTypeById(itvCourseInfo.getString("classTypeId"), itvTokenInfo));
        aiClassObj.put("code",itvCourseInfo.getString("code"));
        aiClassObj.put("courseName",itvCourseInfo.getString("courseName"));

        URI url = new URI(createLiveUrl);
        log.info("创建AI好课请求信息："+aiClassObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(aiClassObj, aXGtHttpHeaders(itvTokenInfo), POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("创建AI好课返回信息："+repObj.toJSONString());
        return repObj;
    }

    /**
     * 新建AI好课课程的ID获取
     * @param itvTokenInfo，subjectProductId
     * @return
     */
    public Integer getAiClassId(Integer  subjectProductId, ItvTeacherTokenInfo itvTokenInfo) throws URISyntaxException {
        String valueUrl = itvCreateAiUrlList
                + "?pageNum=" + 1
                + "&pageSize=" + 10
                + "&subjectProductId=" + subjectProductId
                + "&publishStatus="
                + "&courseName="
                + "&bookVersion="
                + "&gradeId="
                + "&period="
                + "&classStyle="+ 0
                + "&scheme="
                + "&teacherName=";

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("itvCreateAiUrlList的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(itvTokenInfo), GET, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("AI生产系统的itvCreateAiUrlList的返回：" + repObj.toJSONString());
        JSONObject bodyObj = repObj.getJSONObject("body");
        Integer courseId =  bodyObj.getJSONArray("list").getJSONObject(0).getInteger("courseId");
        return courseId;
    }

    /**
     * 新建AI好课课程的发布
     * @param itvTokenInfo
     */
    public Integer getAiClassPublish(int  courseId, ItvTeacherTokenInfo itvTokenInfo) throws URISyntaxException {
        String valueUrl = itvCreateAiPublish
                + "?courseId=" + courseId;

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("itvCreateAiPublish的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(itvTokenInfo), GET, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("itvCreateAiPublish的返回：" + repObj.toJSONString());

        return courseId;
    }

    /**
     * 展示Ai好课的lesson列表,并审核讲次
     * @param itvTokenInfo
     * @return
     */
    public void createAiLessonList(int  courseId, ItvTeacherTokenInfo itvTokenInfo) throws URISyntaxException {
        String valueUrl = itvCreateAiLessonList
                + "?pageNum=1&pageSize=10&auditStatus=4&enableStatus=2" +
                "&courseId=" + courseId;

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("itvCreateAiPublish的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(itvTokenInfo), GET, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("itvCreateAiPublish的返回：" + repObj.toJSONString());

        JSONArray bodyArray = repObj.getJSONArray("body");
        for(int i=0;i<bodyArray.size();i++){
            Integer aiLessonId = bodyArray.getJSONObject(i).getInteger("aiLessonId");
            updateLessonAuditStatus(aiLessonId,  itvTokenInfo);
        }
    }

    /**
     * 审核讲次
     * @param itvTokenInfo
     * @return
     */
    public void updateLessonAuditStatus(Integer  aiLessonId, ItvTeacherTokenInfo itvTokenInfo) throws URISyntaxException {
        String valueUrl = itvupdateLessonAuditStatus
                + "?type=1&aiLessonId=" + aiLessonId;

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("itvupdateLessonAuditStatus的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(itvTokenInfo), GET, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("itvupdateLessonAuditStatus的返回：" + repObj.toJSONString());
    }
    public Integer CreateAiItv(JSONObject reqBody) throws URISyntaxException {
        log.info("生产系统登录开始-----------------------------------主讲机构登录");
        /**1、获取生产系统的登录信息*/
        JSONObject teacherInfo = reqBody.getJSONObject("producerTeacherInfo");
        if (teacherInfo == null) {
            log.info("入参生产系统的登录信息有误！");
        }
        log.info("教师teacherInfo" + teacherInfo);
        ItvTeacherTokenInfo itvTokenInfo = getitvTokenInfo(teacherInfo);
        log.info("教师token" + itvTokenInfo);

        log.info("新建AI好课课程开始-----------------------------------");
        JSONObject itvCourseInfo = reqBody.getJSONObject("itvCourseInfo");
        createAiClass(itvCourseInfo, itvTokenInfo);

        log.info("新建AI好课课程发布-----------------------------------");
        Integer  subjectProductId=reqBody.getInteger("subjectProductId");
        Integer courseId = getAiClassPublish(getAiClassId(subjectProductId, itvTokenInfo), itvTokenInfo);

        log.info("新建AI好课课程审核-----------------------------------");
        createAiLessonList(getAiClassId(subjectProductId, itvTokenInfo), itvTokenInfo);

        return courseId;
    }
}
