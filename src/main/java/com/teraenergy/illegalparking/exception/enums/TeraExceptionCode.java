package com.teraenergy.illegalparking.exception.enums;

import com.teraenergy.illegalparking.exception.TeraErrCode;
import com.teraenergy.illegalparking.exception.TeraErrCodeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-03-07
 * Author : young
 * Project : sarangbang
 * Description :
 */
@Getter
public enum TeraExceptionCode implements TeraErrCode {

    /**
     * 알 수 없는 오류
     */

    UNKNOWN("알 수 없는 오류"),

    CAST_FAILURE("cast 를 실패 하였습니다."),

//    WRONG_AREA_

    USER_WRONG_ID_PASSWORD_AGAIN_CHECK(" 사용자 정보가 잘 못 되었거나 패스워드가 틀렸습니다. \n 다시 확인하여 입력 하세요."),

    USER_INSERT_FAIL ("사용자 등록이 실패 하였습니다. \n 다시 확인하여 입력 하세요"),

    USER_IS_NOT_EXIST ("존재하지 않는 사용자입니다."),

    /* 파일 */
    UNSUPPORTED_FORMAT("지원되지 않는 형식입니다."),
    PARAMETER_EMPTY("%1(이)가 입력되지 않았습니다."),
    PARAMETER_INVALID("%1(이)가 잘못 입력되었습니다."),

    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_STORE_FAILURE("파일 저장 중 오류가 발생하였습니다."),
    FILE_READ_FAILURE("파일을 불러오는 중 오류가 발생하였습니다."),
    FILE_DELETE_FAILURE("파일 삭제 중 오류가 발생하였습니다."),
    DIRECTORY_MAKE_FAILURE("디렉토리 생성 중 오류가 발생하였습니다."),
    ;

    private String message;

    TeraExceptionCode(String message){
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.toString();
    }

    @Override
    public String getMessage(String... args) {
        return TeraErrCodeUtil.parseMessage(this.message, args);
    }


}
