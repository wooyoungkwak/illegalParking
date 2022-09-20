package com.teraenergy.illegalparking.exception;

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

    /* 알 수 없는 오류입니다. */
    NULL("알 수 없는 오류입니다.")
    ;

    private String message;
}
