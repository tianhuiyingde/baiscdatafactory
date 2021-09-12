package com.aixuexi.ss.common.response;

import com.aixuexi.ss.common.except.ResponseDataException;
import com.aixuexi.ss.common.util.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ResultData
{
    private static final long serialVersionUID = 5419750602572069009L;
    private static Logger logger = LoggerFactory.getLogger(ResultData.class);
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_ERROR = 0;
    public static final int STATUS_DEPRECATED = -1;
    public static final int STATUS_HYSTRIX_FALLBACK = -2;
    private int status = 1;
    private int errorCode;
    private String errorMessage = "";
    private Object body;

    public ResultData() {
    }

    public ResultData(int status, String errorMessage, int errorCode) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ResultData(String errorMessage, int errorCode) {
        this.status = 0;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ResultData(int status,Object body) {
        this.body = body;
//        log.info("ResultData"+body.toString());
        if (this.body == null) {
            logger.error("ResultData.body is null.");
        }

    }

    public Object getBody() {
        return this.body == null ? new HashMap() : this.body;
    }

    public ResultData setBody(Object body) {
        this.body = body;
        if (this.body == null) {
            logger.error("ResultData.body is null.");
        }

        return this;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean putKV(String key, Object value) {
        if (key != null && value != null) {
            if (this.body == null) {
                this.body = new HashMap();
            }

            if (this.body instanceof Map) {
                ((Map)this.body).put(key, value);
                return true;
            } else {
                throw new ResponseDataException();
            }
        } else {
            return false;
        }
    }

    public Object tetKV(String key) {
        return this.body == null ? null : ((Map)this.body).get(key);
    }

    public String tetKVString(String key) {
        return (String)this.tetKV(key);
    }

    public Integer tetKVInteger(String key) {
        return (Integer)this.tetKV(key);
    }

    public Long tetKVLong(String key) {
        return (Long)this.tetKV(key);
    }

    public boolean itMapBody() {
        return this.body != null && this.body instanceof Map;
    }

    public boolean itSuccess() {
        return this.status == 1;
    }

    public boolean itNotSuccess() {
        return this.status != 1;
    }

    public boolean itNotDeprecated() {
        return this.status != -1;
    }

    public String toString() {
        return "ResultData{status=" + this.status + ", errorCode=" + this.errorCode + ", errorMessage='" + this.errorMessage + '\'' + ", body=" + JsonHelper.GetJsonResult(this.getBody()) + '}';
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public static ResultData successed() {
        return new ResultData();
    }

    public static ResultData successed(Object data) {
        ResultData resultData = new ResultData();
        resultData.setBody(data);
        return resultData;
    }

    public static ResultData successed(String mesg, Object data) {
        ResultData resultData = new ResultData();
        resultData.setErrorMessage(mesg);
        resultData.setBody(data);
        return resultData;
    }

    public static ResultData failed(String mesg) {
        ResultData resultData = new ResultData();
        resultData.setErrorMessage(mesg);
        resultData.setStatus(0);
        return resultData;
    }

    public static ResultData failed(int code, String mesg) {
        logger.error("code = {} | message = {}", code, mesg);
        ResultData resultData = new ResultData();
        resultData.setErrorCode(code);
        resultData.setErrorMessage(mesg);
        resultData.setStatus(0);
        return resultData;
    }

    public static ResultData deprecated(Object data) {
        ResultData resultData = new ResultData();
        resultData.setBody(data);
        resultData.setStatus(-1);
        return resultData;
    }

    public static ResultData fallback() {
        ResultData resultData = new ResultData();
        resultData.setStatus(-2);
        return resultData;
    }

    public static ResultData convert(RpcResultData rpcResultData) {
        ResultData resultData = new ResultData(rpcResultData.getStatus(), rpcResultData.getErrorMessage(), rpcResultData.getErrorCode());
        resultData.setBody(rpcResultData.getData());
        return resultData;
    }

}
