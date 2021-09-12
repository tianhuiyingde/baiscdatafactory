package com.aixuexi.ss.common.response;

import com.aixuexi.ss.common.except.ExceptionCode;
import com.aixuexi.ss.common.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class RpcResultData<T> implements Serializable {
    private static final long serialVersionUID = 7806722831878085879L;
    private static Logger logger = LoggerFactory.getLogger(RpcResultData.class);
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_ERROR = 0;
    private int status = 1;
    private int errorCode;
    private String errorMessage = "";
    private T data;

    public static <T> RpcResultData successed() {
        RpcResultData<T> respData = new RpcResultData();
        return respData;
    }

    public static <T> RpcResultData successed(String message, T data) {
        RpcResultData<T> respData = new RpcResultData();
        respData.setErrorMessage(message);
        respData.setData(data);
        return respData;
    }

    public static <T> RpcResultData<T> successed(T data) {
        RpcResultData<T> respData = new RpcResultData();
        respData.setData(data);
        return respData;
    }

    public static <T> RpcResultData<T> failed(String message) {
        return new RpcResultData(0, message, ExceptionCode.UNKNOWN.getCode());
    }

    public static <T> RpcResultData<T> failed(int code, String message) {
        return new RpcResultData(0, message, code);
    }

    public RpcResultData() {
    }

    public RpcResultData(int status, String errorMessage, int errorCode) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public RpcResultData(String errorMessage, int errorCode) {
        this.status = 0;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public RpcResultData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
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

    public String toString() {
        return "RpcResultData{status=" + this.status + ", errorCode=" + this.errorCode + ", errorMessage='" + this.errorMessage + '\'' + ", data=" + JsonHelper.GetJsonResult(this.getData()) + '}';
    }
}
