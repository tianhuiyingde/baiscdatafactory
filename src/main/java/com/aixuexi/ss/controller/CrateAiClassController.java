package com.aixuexi.ss.controller;

import com.aixuexi.ss.common.response.ResultData;
import com.aixuexi.ss.service.createclass.ApplyClassService;
import com.aixuexi.ss.service.createclass.CrateStreamingClassService;
import com.aixuexi.ss.service.createclass.CreateAIClassService;
import com.aixuexi.ss.service.operatesql.OperateClassMessageService;
import com.aixuexi.ss.service.operatesql.OperationLogSqlService;
import com.aixuexi.ss.tools.WeChatRobot;
import com.aixuexi.ss.vo.ClassMessageVo;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;

/**
 * 新建AI好课
 */
@Slf4j
@Controller
@CrossOrigin
@RequestMapping("CreateAIClass")
@Api(tags = "Ai好课创建")
public class CrateAiClassController {

    @Autowired
    CrateStreamingClassService crateStreamingClassService;
    @Autowired
    CreateAIClassService createAIClassService;
    @Autowired
    ApplyClassService applyClassService;
    @Autowired
    private OperationLogSqlService insertOperationLogService;
    @Autowired
    private OperateClassMessageService insertClassMessageService;

    /**
     * 创建AI好课
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/createAi", method = RequestMethod.POST)
    @ResponseBody
    public ResultData CreateAndBuy(@RequestBody JSONObject reqBody) throws URISyntaxException, IOException, ParseException {
        log.info("操作记录插入数据库");
        insertOperationLogService.insertoperationLogClass(reqBody);
        /**创建课程记录插入数据库*/
        log.info("AI好课创建自动化流程开始-----------------------------------主讲机构登录");
        /**2创建AI好课课*/
        log.info("AI好课创建开始");
        JSONObject repInfo = createAIClassService.createAiClass(reqBody);
        if (repInfo == null || "0".equals(repInfo.getString("status"))) {
            log.error(repInfo.getString("errorMessage"));
            return new ResultData("创建AI好课过程中发生错误！", 500);
        }
        /**获取大班ID*/
        Integer classId = repInfo.getInteger("classId");
        if(!createAIClassService.buyAIClass(reqBody,classId))
        {
            return new ResultData("购买AI好课过程中发生错误", 500);
        }
        JSONObject assistantInfo = reqBody.getJSONObject("assistantInfo");
        JSONObject applyClassInfo = reqBody.getJSONObject("applyClassInfo");
        /**企业微信通知*/
        StringBuilder weChatMsg  = new StringBuilder("班级创建完成，班级信息：");
        weChatMsg.append("班级名称<"+reqBody.getJSONObject("basicAiInfo").getString("className")+">");
        weChatMsg.append("班级数量<"+applyClassInfo.getInteger("subClassNum")+">" );
        weChatMsg.append("学生数量<"+applyClassInfo.getString("purchasedStuNumber")+">");
        weChatMsg.append("辅导老师<"+assistantInfo.getString("teacherName"));
        weChatMsg.append("（"+assistantInfo.getString("teacherAccount"));
        weChatMsg.append("）>已创建完成，请及时登录客户端查看！");
        WeChatRobot weChatRobot = new WeChatRobot();
        weChatRobot.senMsgDemo(weChatMsg.toString());
        log.info("AI好课创建自动化流程结束-----------------------------------创建完成");
        return new ResultData(0, "创建成功");
    }

    /**
     * 购买AI好课
     * @param reqBody
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/buyAi", method = RequestMethod.POST)
    @ResponseBody
    public ResultData BuyAIClass(@RequestBody JSONObject reqBody) throws URISyntaxException, IOException, ParseException
    {

        log.info("操作记录插入数据库");
        insertOperationLogService.insertoperationLogClass(reqBody);
        if(!createAIClassService.buyAIClass(reqBody,reqBody.getInteger("classId")))
        {
            return new ResultData("购买AI好课过程中发生错误", 500);
        }
        JSONObject assistantInfo = reqBody.getJSONObject("assistantInfo");
        JSONObject applyClassInfo = reqBody.getJSONObject("applyClassInfo");
        /**企业微信通知*/
        StringBuilder weChatMsgCopy  = new StringBuilder("班级复制完成，班级信息：");
        weChatMsgCopy.append("班级名称<"+reqBody.getJSONObject("basicAiInfo").getString("className")+">");
        weChatMsgCopy.append("班级数量<"+applyClassInfo.getInteger("subClassNum")+">" );
        weChatMsgCopy.append("学生数量<"+applyClassInfo.getString("purchasedStuNumber")+">");
        weChatMsgCopy.append("辅导老师<"+assistantInfo.getString("teacherName"));
        weChatMsgCopy.append("（"+assistantInfo.getString("teacherAccount"));
        weChatMsgCopy.append("）>已复制完成，请及时登录客户端查看！");
        WeChatRobot weChatRobot = new WeChatRobot();
        weChatRobot.senMsgDemo(weChatMsgCopy.toString());
        return new ResultData(0, "创建成功");
    }

}
