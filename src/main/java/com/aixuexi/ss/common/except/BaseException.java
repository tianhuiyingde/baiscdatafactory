package com.aixuexi.ss.common.except;

public abstract class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public abstract ExceptionCode getExceptionCode();
}
