package com.teraenergy.illegalparking.exception.enums;

/**
 * Date : 2022-03-14
 * Author : young
 * Project : sarangbang
 * Description :
 */
public enum EncryptedExceptionCode {

    ENCRYPT_FAILURE("암호화 생성 실패"),

    ENCODE_FAILURE("암호화 하기 위한 코드 생성 실패"),

    DECODE_FAILURE("복호화 하기 위한 코드 생성 실패"),

    DECRYPT_FAILURE("복호화 생성 실패");


    private String message;

    EncryptedExceptionCode(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

}
