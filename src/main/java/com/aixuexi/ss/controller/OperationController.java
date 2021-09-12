package com.aixuexi.ss.controller;

import com.aixuexi.ss.common.util.JsonResult;
import com.aixuexi.ss.common.util.ResultCode;
import com.aixuexi.ss.entity.OperationLog;
import com.aixuexi.ss.service.OperationLogService;
import com.aixuexi.ss.service.operatesql.OperationLogSqlService;
import com.aixuexi.ss.vo.OperationLogVo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author wangyangyang
 * @date 2020/11/26 19:39
 * @description
 **/
@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class OperationController {
    @Autowired
    private OperationLogSqlService insertOperationLogService;
    @Autowired
    private OperationLogService operationLogService;

    /**
     * http://localhost:9098/listOperationLogs
     *
     * @return
     */
    @PostMapping("/listOperationLogs")
    public JsonResult listOperationLogs(@RequestBody JSONObject reqBody) {
        String time = reqBody.getString("selectDatetime");
        int getNmuDate = reqBody.getInteger("getNmuDate");
        List<OperationLogVo> list = this.operationLogService.listOperationLogs(time.substring(0, 7));

        List<List<OperationLogVo>> listAll = new ArrayList<>();
        List<OperationLogVo> listFirst = new ArrayList<>();
        List<OperationLogVo> listSecond = new ArrayList<>();
        List<OperationLogVo> listThird = new ArrayList<>();
        List<OperationLogVo> listFourth = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).getCreateTime();
            if (time.substring(0, 7).equals(str.substring(0, 7))) {
                if (list.get(i).getClassType() == 0) {
                    listFirst.add(list.get(i));
                } else {
                    listSecond.add(list.get(i));
                }
            }
        }

        if(listFirst== null){
            for (int j = 0; j < getNmuDate; ) {
                list.get(j).setCreateTime(time.substring(0, 7) + '-' + String.valueOf(j + 1));
                list.get(j).setNumber(0);
                list.get(j).setClassType(0);
                listFirst.add(list.get(j));
            }
        }else{
            int k=0;
            for (int j = 0; j < listFirst.size(); j++) {
                OperationLogVo operationLogTemp = listFirst.get(j);
                int num = Integer.valueOf(operationLogTemp.getCreateTime().substring(8, 10));
                while ((k+1) < num) {
                    OperationLogVo operationLog = new OperationLogVo();
                    if((k+1)<10)
                        operationLog.setCreateTime(time.substring(0, 7) + '-' + "0"+String.valueOf(k + 1));
                    else
                        operationLog.setCreateTime(time.substring(0, 7) + '-' +String.valueOf(k + 1));
                    operationLog.setNumber(0);
                    operationLog.setClassType(0);
                    listThird.add( operationLog);
                    k++;
                }
                if((k+1) == num) {
                    operationLogTemp.setCreateTime(operationLogTemp.getCreateTime().substring(0,10));
                    listThird.add(operationLogTemp);
                    k++;
                }
            }
            k=listThird.size();
            while ( k< getNmuDate) {
                OperationLogVo operationLog = new OperationLogVo();
                if((k+1)<10)
                    operationLog.setCreateTime(time.substring(0, 7) + '-' + "0"+String.valueOf(k + 1));
                else
                    operationLog.setCreateTime(time.substring(0, 7) + '-' +String.valueOf(k + 1));
                operationLog.setNumber(0);
                operationLog.setClassType(0);
                listThird.add( operationLog);
                k++;
            }
        }
        if(listSecond== null){
            for (int j = 0; j < getNmuDate; ) {
                list.get(j).setCreateTime(time.substring(0, 7) + '-' + String.valueOf(j + 1));
                list.get(j).setNumber(0);
                list.get(j).setClassType(1);
                listSecond.add(list.get(j));
            }
        }else{
            int k=0;
            for (int j = 0; j < listSecond.size(); j++) {
                OperationLogVo operationLogTemp = listSecond.get(j);
                int num = Integer.valueOf(operationLogTemp.getCreateTime().substring(8, 10));
                while ((k+1) < num) {
                    OperationLogVo operationLog = new OperationLogVo();
                    if((k+1)<10)
                        operationLog.setCreateTime(time.substring(0, 7) + '-' + "0"+String.valueOf(k + 1));
                    else
                        operationLog.setCreateTime(time.substring(0, 7) + '-' +String.valueOf(k + 1));
                    operationLog.setNumber(0);
                    operationLog.setClassType(1);
                    listFourth.add( operationLog);
                    k++;
                }
                if((k+1) == num) {
                    operationLogTemp.setCreateTime(operationLogTemp.getCreateTime().substring(0,10));
                    listFourth.add(operationLogTemp);
                    k++;
                }
            }
            k=listFourth.size();
            while ( k< getNmuDate) {
                OperationLogVo operationLog = new OperationLogVo();
                if((k+1)<10)
                    operationLog.setCreateTime(time.substring(0, 7) + '-' + "0"+String.valueOf(k + 1));
                else
                    operationLog.setCreateTime(time.substring(0, 7) + '-' +String.valueOf(k + 1));
                operationLog.setNumber(0);
                operationLog.setClassType(1);
                listFourth.add( operationLog);
                k++;
            }
        }

        listAll.add(listThird);
        listAll.add(listFourth);
        return new JsonResult(ResultCode.SUCCESS,listAll);
    }

/**
 * http://localhost:9098/insertoperationLogClass
 *
 * @return
 */

@RequestMapping("/insertoperationLogClass")
public String insertoperationLogClass()throws IOException,ParseException{
        int classType=0;
        String obj=this.insertOperationLogService.insertoperationLogClass(classType);
        return"insert result1:"+obj;
        }
        }
