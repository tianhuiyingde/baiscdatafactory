package com.aixuexi.ss.service.operatesql;

import com.aixuexi.ss.common.util.Log_Exception;
import com.aixuexi.ss.entity.*;
import com.aixuexi.ss.service.AssistantService;
import com.aixuexi.ss.service.LecturerService;
import com.aixuexi.ss.service.StudentService;
import com.aixuexi.ss.service.createclass.ApplyClassService;
import com.aixuexi.ss.service.createclass.CrateStreamingClassService;
import com.aixuexi.ss.vo.StudentVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * @author wangyangyang
 * @date 2020/9/13 21:13
 * @description
 **/
@Service
@Slf4j
@Configuration
public class OperateStudentService {
    @Value("${common.studentListUrl}")
    private String studentListUrl;
    @Value("${common.studentDetailUrl}")
    private String studentDetailUrl;
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
     * 学生信息插入数据库
     *
     * @param telephone
     * @param aXXAssistantTokenInfo
     * @return
     */
    public String insertStudentClass(String telephone, int studentNumber, JSONObject aXXAssistantTokenInfo,int environmentTag) throws IOException, ParseException, URISyntaxException {
        String errorContent = null;
        int obj1 = -2;
        for (int i = 0; i < studentNumber; i++) {
            String newTelephone;
            int j=Integer.parseInt(telephone.substring(8, 11)) + i;
            if(j<10)
                newTelephone = telephone.substring(0, 8) + "00"+String.valueOf(j);
            else if(10<=j && j<100)
                newTelephone = telephone.substring(0, 8) + "0"+String.valueOf(j);
            else
                newTelephone = telephone.substring(0, 8) +String.valueOf(j);
            log.info("newTelephone" + newTelephone);

            Integer studentId = queryStudentList(newTelephone, aXXAssistantTokenInfo);

            JSONObject queryStudentObj = queryStudent(studentId, aXXAssistantTokenInfo);
            JSONObject bodyObj = queryStudentObj.getJSONObject("body");
            JSONObject studentManageDtoObj = bodyObj.getJSONObject("studentManageDto");

            String name = studentManageDtoObj.getString("name");

            Student student = new Student();
            student.setId(studentId);
            student.setInstitutionId(1591);
            student.setPassport("123456");
            student.setStudentName(name);
            student.setTelephone(newTelephone);
            student.setEnvironmentTag(environmentTag);

            try {
                log.info("学生信息" + student.toString());
                obj1 = this.studentService.insertClass(student);
            } catch (Exception e) {
                log.info(" ", e);
                errorContent = e.getClass().getName() + "  error Info  " + e.getMessage() + e;
            }
            Log_Exception le = new Log_Exception();
        }
        return "insert result1:" + obj1;
    }

    /**
     * 学生信息获取
     *
     * @param studentId
     * @param aXXAssistantTokenInfo
     * @return
     */
    public JSONObject queryStudent(Integer studentId, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        JSONObject queryStudentObj = new JSONObject();
        queryStudentObj.put("studentId", studentId);
        queryStudentObj.put("modifyPage", true);
        queryStudentObj.put("studentClassType", 0);

        URI initUrl = new URI(studentDetailUrl);
        log.info("学生信息获取json信息：" + queryStudentObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(queryStudentObj, crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("学生信息获取返回接口信息：" + repObj.toJSONString());

        return repObj;
    }

    /**
     * 学生list信息获取
     *
     * @param telephone
     * @param aXXAssistantTokenInfo
     * @return
     */
    public Integer queryStudentList(String telephone, JSONObject aXXAssistantTokenInfo) throws URISyntaxException {
        JSONObject studentObj = new JSONObject();
        studentObj.put("filterParam", telephone);
        studentObj.put("gradeId", -1);
        studentObj.put("className", "");
        studentObj.put("pageNo", 1);
        studentObj.put("pageSize", 10);
        studentObj.put("sortRule", 0);
        studentObj.put("studentType", 0);

        log.info("学生接口请求信息：" + studentObj.toJSONString());

        URI initUrl = new URI(studentListUrl);

        RequestEntity requestEntity = new RequestEntity<>(studentObj, crateStreamingClassService.aXGtHttpHeaders(aXXAssistantTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();

        log.info("学生信息获取返回接口信息：" + repObj.toJSONString());

        JSONObject bodyObj = repObj.getJSONObject("body");
        JSONArray studentManageDtoObj = bodyObj.getJSONArray("studentManageDtoList");
        Integer studentId = studentManageDtoObj.getJSONObject(0).getInteger("id");

        return studentId;
    }

    /**
     * 通过学生手机号码获取学生信息
     *
     * @param telephone
     * @param environmentTag
     * @return
     */
    public Student getStudentByTelephone(String telephone, int environmentTag){
        String errorContent = null;
        StudentVo studentVoObj = new StudentVo();
        try {
            studentVoObj = this.studentService.getStudentByTelephone(telephone,environmentTag);
        } catch (Exception e) {
            log.info(" ", e);
            errorContent = e.getClass().getName() + "  error Info  " + e.getMessage() + e;
        }
        return studentVoObj;
    }
}
