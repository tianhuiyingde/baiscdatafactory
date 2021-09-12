package com.aixuexi.ss.common.except;

public enum ExceptionCode {
    GET_CACHE_KEY_ERROR(100110001, "生成缓存key失败", (String) null),
    PUT_RESPONSE_DATA_ERROR(100120001, "生成ResultData异常", (String) null),
    RPC_INVOKE_ERROR(100200001, "远程调用异常", (String) null),
    ARGUMENT_IS_EMPTY_ERROR(100000001, "参数为空", (String) null),
    ARGUMENT_IS_ERROR_ERROR(100000002, "参数不正确", (String) null),
    NOT_LOGIN_ERROR(100300001, "用户未登录", (String) null),
    FILE_UPLOAD_ERROR(100300002, "文件上传失败", (String) null),
    UNKNOWN(100000000, "未知异常", (String) null);

    private static final int BASE_HEADER_CODE_SYSTEM_ERROR = 100000000;
    private static final int ILLEGAL_ARGUMENT_ERROR = 0;
    private static final int HEADER_CODE_CACHE_ERROR = 110000;
    private static final int HEADER_CODE_RESPONSE_ERROR = 120000;
    private static final int HEADER_CODE_RPC_INVOKE_ERROR = 200000;
    private static final int HTTP_REQUEST_ERROR = 300000;
    private int code;
    private String message;
    private String alter = "系统内部错误";

    private ExceptionCode(int code, String message, String alter) {
        this.code = code;
        this.message = message;
        if (alter != null && alter.length() > 0) {
            this.alter = alter;
        }

    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlter() {
        return this.alter;
    }

    public void setAlter(String alter) {
        this.alter = alter;
    }
}