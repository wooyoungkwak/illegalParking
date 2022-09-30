package com.teraenergy.illegalparking.exception;

import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import lombok.Getter;

/**
 * Date : 2022-03-07
 * Author : young
 * Project : sarangbang
 * Description :
 */

public class TeraException extends Exception{

    String code;

    String message;

    @Getter
    private TeraExceptionCode teraExceptionCode;

    public TeraException(String message) {
        super(message);
        this.message = message;
    }

    public TeraException(TeraExceptionCode teraExceptionCode) {
        this(teraExceptionCode.name(), teraExceptionCode.getMessage());
    }

    public TeraException(TeraExceptionCode teraExceptionCode, Throwable e) {
        this(teraExceptionCode.name(), teraExceptionCode.getMessage());
    }

    public TeraException(TeraExceptionCode teraExceptionCode, String message) {
        this(teraExceptionCode.name(), teraExceptionCode.getMessage());
    }

    public TeraException(TeraExceptionCode teraExceptionCode, Throwable e, String... args) {
        this(teraExceptionCode.name(), TeraErrCodeUtil.parseMessage(args[0], args) );
    }



    public TeraException(String code, String message) {
        this(message);
        this.code = code;
    }

    public TeraException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    
}
