package com.aixuexi.ss.service.operatesql;

import com.aixuexi.ss.common.util.Log_Exception;
import com.aixuexi.ss.entity.Assistant;
import com.aixuexi.ss.entity.AssistantTeacherTokenInfo;
import com.aixuexi.ss.entity.Lecturer;
import com.aixuexi.ss.entity.LecturerTeacherTokenInfo;
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
public class OperateLecturerService {
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
     * 主讲教师信息插入数据库
     *
     * @param telephone
     * @param lecturerTeacherTokenInfo
     * @return
     */
    public String insertLecturerTeacherClass(String telephone, int teacherNumber, LecturerTeacherTokenInfo lecturerTeacherTokenInfo,int environmentTag)  throws IOException, ParseException, URISyntaxException {
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

            Integer userId = queryLecturerTeacherList(newTelephone, lecturerTeacherTokenInfo);
            JSONObject queryStudentObj = queryLecturerTeacher(userId, lecturerTeacherTokenInfo);
            JSONObject bodyObj = queryStudentObj.getJSONObject("body");
            String name = bodyObj.getString("name");
            Integer id = bodyObj.getInteger("id");
            Integer institutionId = bodyObj.getInteger("institutionId");

            JSONArray insSubjectsObj = bodyObj.getJSONArray("insSubjects");
            String subjectName = null;
            for (int k=0;k<insSubjectsObj.size();k++){
                if(insSubjectsObj.getJSONObject(k).getBoolean("checked") == true) {
                    subjectName = insSubjectsObj.getJSONObject(k).getString("subjectName");
                }
            }

            Lecturer lecturer = new Lecturer();
            lecturer.setUserId(userId);
            lecturer.setId(id);
            lecturer.setInstitutionId(institutionId);
            lecturer.setPassport("ss123456");
            lecturer.setNickname(name);
            lecturer.setLTelephone(newTelephone);
            lecturer.setEnvironmentTag(environmentTag);
            lecturer.setSubjectName(subjectName);

            try {
                log.info("主讲教师信息" + lecturer.toString());
                obj1 = this.lecturerService.insertClass(lecturer);
            } catch (Exception e) {
                log.info(" ", e);
                errorContent = e.getClass().getName() + "  error Info  " + e.getMessage() + e;
            }
            Log_Exception le = new Log_Exception();
        }
        return "insert result1:" + obj1;
    }

    /**
     * 主讲list信息获取
     *
     * @param telephone
     * @param lecturerTeacherTokenInfo
     * @return
     */
    public Integer queryLecturerTeacherList(String telephone, LecturerTeacherTokenInfo lecturerTeacherTokenInfo) throws URISyntaxException {
        JSONObject teacherObj = new JSONObject();
        teacherObj.put("account", telephone);
        teacherObj.put("institutionId", 2867);
        teacherObj.put("name", "");
        teacherObj.put("pageNo", 1);
        teacherObj.put("pageSize", 15);
        teacherObj.put("roleId", "");
        teacherObj.put("status", 1);
        teacherObj.put("total", 1);

        log.info("主讲接口请求信息：" + teacherObj.toJSONString());

        URI initUrl = new URI(teacherListUrl);

        RequestEntity requestEntity = new RequestEntity<>(teacherObj, crateStreamingClassService.aXGtHttpHeaders(lecturerTeacherTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();

        log.info("主讲教师信息获取返回接口信息：" + repObj.toJSONString());

        JSONObject bodyObj = repObj.getJSONObject("body");
        JSONArray listArray = bodyObj.getJSONArray("list");
        Integer userID = listArray.getJSONObject(0).getInteger("id");

        return userID;
    }

    /**
     * 主讲detail信息获取
     *
     * @param userId
     * @param lecturerTeacherTokenInfo
     * @return
     */
    public JSONObject queryLecturerTeacher(Integer userId, LecturerTeacherTokenInfo lecturerTeacherTokenInfo) throws URISyntaxException {
        String newQueryDetailUrl = teacherDetailUrl
                + "?userId=" + userId;

        URI initUrl = new URI(newQueryDetailUrl);

        RequestEntity requestEntity = new RequestEntity<>(crateStreamingClassService.aXGtHttpHeaders(lecturerTeacherTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("助教教师信息获取返回接口信息：" + repObj.toJSONString());

        return repObj;
    }
}
