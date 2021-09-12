package com.aixuexi.ss.service.operatesql;

import com.aixuexi.ss.common.util.Log_Exception;
import com.aixuexi.ss.entity.ClassMessage;
import com.aixuexi.ss.service.ClassMessageService;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author wangyangyang
 * @date 2020/9/13 21:13
 * @description
 **/
@Service
@Slf4j
@Component
public class OperateClassMessageService {
    @Autowired
    private ClassMessageService classMessageService;

    public List<ClassMessageVo> listSearchClassById(Integer bigClassId) {
        List<ClassMessageVo> classMessageVo= this.classMessageService.listSearchClassById(bigClassId);
        return classMessageVo;
    }

    public String insertclassMessageClass(JSONObject classObj, int environmentTag) throws IOException, ParseException {
        log.info("classObj" + classObj.toJSONString());
        ClassMessage classMessage = new ClassMessage();

        String path = System.getProperty("user.dir") + "\\file.txt";
        classMessage.setBigClassId(classObj.getInteger("bigClassId"));
        classMessage.setClassName(classObj.getString("className"));
        classMessage.setStudentNumber(classObj.getInteger("studentNumber"));
        Date day = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(day);
        Date dateNow = simpleDateFormat.parse(dateStr);
        classMessage.setCreateTime(dateNow);

        classMessage.setBeginTime(dateNow);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        calendar.add(Calendar.DAY_OF_MONTH, 7 * 15);
        dateNow = calendar.getTime();
        classMessage.setEndTime(dateNow);

        classMessage.setClassType(classObj.getInteger("classType"));
        classMessage.setLecturerId(classObj.getInteger("lecturerId"));
        classMessage.setAssistantId(classObj.getInteger("assistantId"));
        classMessage.setUserId(classObj.getInteger("userId"));
        classMessage.setStudentIds(classObj.getString("studentIds"));
        classMessage.setEnvironmentTag(environmentTag);
        classMessage.setAssistantName(classObj.getString("assistantName"));
        classMessage.setAssistantPhone(classObj.getString("assistantPhone"));
        classMessage.setSubClassId(classObj.getInteger("subClassId"));


        String errorContent = null;
        int obj1 = -2;
        try {
            log.info("课程收集信息" + classMessage.toString());
            obj1 = this.classMessageService.insertClass(classMessage);
        } catch (Exception e) {
            log.info(" ", e);
            errorContent = e.getClass().getName() + "  error Info  " + e.getMessage() + e;
        }
        Log_Exception le = new Log_Exception();
        le.writeEror_to_txt(path, errorContent);

        return "insert result1:" + obj1;
    }
}
