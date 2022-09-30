package com.teraenergy.illegalparking.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-03-07
 * Author : young
 * Project : sarangbang
 * Description :
 */
@AllArgsConstructor
@Getter
public enum TeraExceptionCode {

    /**
     * 알 수 없는 오류
     */
    UNKNOWN("알 수 없는 오류"),

    /* 부동산 중계소 정보가 잘 못 되었습니다. 다시 확인후 등록하세요. */
    WRONG_ESTATE_AGENCY_AGAIN_REGISTER(" 부동산 중계소 정보가 잘 못 되었습니다. \n 다시 확인 후 등록 하세요."),
    
    /* 부동산 정보가 잘 못 되었습니다. 다시 확인후 등록하세요. */
    WRONG_BANGINFO_AGAIN_REGISTER(" 부동산 정보가 잘 못 되었습니다. \n 다시 확인 후 등록 하세요."),

    /* 즐겨 찾기 변경이 되지 않았습니다. */
    NOT_CHANGE_IS_FAVORITES("즐겨 찾기 변경이 되지 않았습니다."),

    /* 존재 하지 않는 사용자입니다. */
    EXIST_NOT_USER("존재하지 않는 사용자 입니다."),

    /* 사용자 정보가 잘못 되었습니다. 다시 확인후 등록하세요. */
    WRONG_USER_AGAIN_REGISTER(" 사용자 정보가 잘 못 되었습니다. \n 다시 확인 후 등록 하세요."),

    /* 사용자 정보가 잘 못 되었거나 패스워드가 틀렸습니다. 다시 확인하여 입력 하세요. */
    WRONG_ID_PASSWORD_USER_AGAIN_CHECK(" 사용자 정보가 잘 못 되었거나 패스워드가 틀렸습니다. \n 다시 확인하여 입력 하세요."),


    CAST_FAILURE("cast 를 실패 하였습니다."),

    /**
     * 지원되지 않는 형식입니다.
     */
    UNSUPPORTED_FORMAT("지원되지 않는 형식입니다."),
    /**
     * %1(이)가 입력되지 않았습니다.
     */
    PARAMETER_EMPTY("%1(이)가 입력되지 않았습니다."),
    /**
     * %1(이)가 잘못 입력되었습니다.
     */
    PARAMETER_INVALID("%1(이)가 잘못 입력되었습니다."),

    /**
     * 파일을 찾을 수 없습니다.
     */
    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    /**
     * 파일 저장 중 오류가 발생하였습니다.
     */
    FILE_STORE_FAILURE("파일 저장 중 오류가 발생하였습니다."),
    /**
     * 파일을 불러오는 중 오류가 발생하였습니다.
     */
    FILE_READ_FAILURE("파일을 불러오는 중 오류가 발생하였습니다."),
    /**
     * 파일 삭제 중 오류가 발생하였습니다.
     */
    FILE_DELETE_FAILURE("파일 삭제 중 오류가 발생하였습니다."),
    /**
     * 디렉토리 생성 중 오류가 발생하였습니다.
     */
    DIRECTORY_MAKE_FAILURE("디렉토리 생성 중 오류가 발생하였습니다."),
    ;

    private String message;
}
