package com.aixuexi.ss.service.operatesql;

import com.aixuexi.ss.common.util.Log_Exception;
import com.aixuexi.ss.entity.Assistant;
import com.aixuexi.ss.entity.AssistantTeacherTokenInfo;
import com.aixuexi.ss.service.AssistantService;
import com.aixuexi.ss.service.LecturerService;
import com.aixuexi.ss.service.StudentService;
import com.aixuexi.ss.service.createclass.ApplyClassService;
import com.aixuexi.ss.service.createclass.CrateStreamingClassService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * @author wangyangyang
 * @date 2020/9/25 11:57
 * @description
 **/
@Service
@Slf4j
@Configuration
public class OperateAssistantService {
    @Value("${common.teacherDetailUrl}")
    private String teacherDetailUrl;
    @Value("${common.teacherListUrl}")
    private String teacherListUrl;

    @Autowired
    private StudentService studentService;
    @Autowired
    private ApplyClassService applyClassService;
    @Autowired
    private CrateStreamingClassService crateStreamingClassService;
    @Autowired
    private AssistantService assistantService;
    @Autowired
    private LecturerService lecturerService;
    RestTemplate restTemplate = new RestTemplate();

    /**
     * 助教教师信息插入数据库
     *
     * @param telephone
     * @param aXXAssistantTokenInfo
     * @return
     */
    public String insertAssistantTeacherClass(String telephone, int teacherNumber, JSONObject aXXAssistantTokenInfo,int environmentTag)  throws IOException, ParseException, URISyntaxException {
        String errorContent = null;

        int obj1 = -2;
        for (int i = 0; i < teacherNumber; i++) {
            String newTelephone;
            int j=Integer.parseInt(telephone.substring(8, 11)) + i;
            if(j<10)
                newTelephone = telephone.substring(0, 8) + "00"+String.valueOf(j);
            else if(10<=j && j<100)
                newTelephone = telephone.substring(0, 8) + "0"+String.valueOf(j);
            else
                newTelephone = telephone.substring(0, 8) +String.valueOf(j);
            log.info("newTelephone" + newTelephone);

            Integer userId = queryAssistantTeacherList(newTelephone, aXXAssistantTokenInfo);
            JSONObject queryStudentObj = queryAssistantTeacher(userId, aXXAssistantTokenInfo);
            JSONObject bodyObj = queryStudentObj.getJSONObject("body");
            String name = bodyObj.getString("name");
            Integer id = bodyObj.getInteger("id");
            Integer institutionId = bodyObj.getInteger("institutionId");

            Assistant assistant = new Assistant();
            assistant.setUserId(userId);
            assistant.setId(id);
            assistant.setInstitutionId(institutionId);
            assistant.setPassport("ss123456");
            assistant.setNickname(name);
            assistant.setATelephone(newTelephone);
            assistant.setEnvironmentTag(environmentTag);

            try {
                log.info("助教教师信息" + assistant.toString());
                obj1 = this.assistantService.insertClass(assistant);
            } catch (Exception e) {
                log.info(" ", e);
                errorContent = e.getClass().getName() + "  error Info  " + e.getMessage() + e;
            }
            Log_Exception le = new Log_Exception();
        }
        return "insert result1:" + obj1;
    }

    /**
     * 助教list信息获取
     *
     * @param telephone
     * @param aXXAssistantTokenInfo
     * @return
     */
    public Integer queryAssistantTeacherList(String telephone, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        JSONObject teacherObj = new JSONObject();
        teacherObj.put("account", telephone);
        teacherObj.put("institutionId", 1591);
        teacherObj.put("name", "");
        teacherObj.put("pageNo", 1);
        teacherObj.put("pageSize", 10);
        teacherObj.put("roleId", "");
        teacherObj.put("status", 1);
        teacherObj.put("total", 1);

        log.info("助教接口请求信息：" + teacherObj.toJSONString());

        URI initUrl = new URI(teacherListUrl);

        RequestEntity requestEntity = new RequestEntity<>(teacherObj, crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();

        log.info("助教教师信息获取返回接口信息：" + repObj.toJSONString());

        JSONObject bodyObj = repObj.getJSONObject("body");
        JSONArray listArray = bodyObj.getJSONArray("list");
        Integer userID = listArray.getJSONObject(0).getInteger("id");

        return userID;
    }

    /**
     * 助教detail信息获取
     *
     * @param userId
     * @param aXXAssistantTokenInfo
     * @return
     */
    public JSONObject queryAssistantTeacher(Integer userId, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        String newQueryDetailUrl = teacherDetailUrl
                + "?userId=" + userId;

        URI initUrl = new URI(newQueryDetailUrl);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("主讲教师信息获取返回接口信息：" + repObj.toJSONString());

        return repObj;
    }
}
