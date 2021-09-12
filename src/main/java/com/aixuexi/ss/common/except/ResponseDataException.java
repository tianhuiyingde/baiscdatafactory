package com.aixuexi.ss.common.except;

import static com.aixuexi.ss.common.except.ExceptionCode.PUT_RESPONSE_DATA_ERROR;

public class ResponseDataException extends BaseException {
    private ExceptionCode exceptionCode;

    public ResponseDataException() {
        super(PUT_RESPONSE_DATA_ERROR.getMessage());
        this.exceptionCode = PUT_RESPONSE_DATA_ERROR;
    }

    public ExceptionCode getExceptionCode() {
        return this.exceptionCode;
    }
}