package com.teraenergy.illegalparking.exception;

import lombok.Getter;

/**
 * Date : 2022-03-07
 * Author : young
 * Project : sarangbang
 * Description :
 */

public class TeraException extends RuntimeException{

    @Getter
    private TeraExceptionCode teraExceptionCode;

    public TeraException(Throwable cause){
        super(cause);
    }

    public TeraException(TeraExceptionCode code) {
        super(code.getMessage());
        this.teraExceptionCode = code;
    }

    public TeraException(TeraExceptionCode code, Throwable cause){
        super(code.getMessage(), cause);
        this.teraExceptionCode = code;
    }

    public TeraException(String message, Throwable cause) {
        super(message, cause);
        this.teraExceptionCode = TeraExceptionCode.NULL;
    }
}
