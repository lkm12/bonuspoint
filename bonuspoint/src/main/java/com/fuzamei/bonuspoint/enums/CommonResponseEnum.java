package com.fuzamei.bonuspoint.enums;

import com.fuzamei.common.enums.ResponseEnum;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/19 15:06
 */
public enum CommonResponseEnum implements ResponseEnum {

    ADD_SUCCESS("true", "ADD_SUCCESS", "200"),
    ADD_FAIL("false", "ADD_FAIL", "500"),
    UPDATE_FALL("false", "UPDATE_FALL", "500"),
    UPDATE_SUCCESS("true", "UPDATE_SUCCESS", "200"),
    SET_SUCCESS("true", "SET_SUCCESS", "200"),
    SET_FAIL("true", "SET_FAIL", "500"),
    DELETE_FAIL("false", "DELETE_FAIL", "220"),
    DELETE_SUCCESS("true", "DELETE_SUCCESS", "200"),
    QUERY_FAIL("false", "QUERY_FAIL", "500"),
    QUERY_SUCCESS("true", "QUERY_SUCCESS", "200"),
    PLATFORM_NOT_EXIST("false", "PLATFORM_NOT_EXIST", "324"),
    TOKEN_WRONG("false", "TOKEN_WRONG", "233"),
    COMPANY_NOT_EXIST("false", "COMPANY_NOT_EXIST", "270"),
    FIND_BUY_GENERAL_POINTS_SUCCESS("true", "FIND_BUY_GENERAL_POINTS_SUCCESS", "200"),
    BAD_REQUEST("false","BAD_REQUEST","500"),
    TOKEN_ERROR("false", "TOKEN_ERROR","401"),
    TOKEN_NOT_ACTIVE("false","TOKEN_NOT_ACTIVE","401"),
    TOKEN_BLANK_ERROR("false", "TOKEN_BLANK_ERROR","401"),
    VARIABLE("true", "VARIABLE", "100"),
    SUCCESS("true", "SUCCESS", "200"),
    FAILURE("false", "FAILURE", "300"),
    PARAMETER_ERROR("false", "PARAMETER_ERROR", "400"),
    PAYWORD_WRONG("false", "PAYWORD_WRONG", "500"),
    PAYWORD_FORMAT_WRONG("false", "PAYWORD_FORMAT_WRONG", "500"),
    CAPTCHA_SENT("false", "CAPTCHA_SENT", "250"),
    UPLOAD_SUCCESS("true", "UPLOAD", "200"),
    PARAMETER_BLANK("false", "PARAMETER_BLANK", "401"),
    IMAGE_TYPE_FAIL("false", "IMAGE_TYPE_FAIL", "402"),
    IMAGE_FORMAT_FAIL("false", "IMAGE_FORMAT_FAIL", "403"),
    NORMALIZE_FORMAT_FAIL("false", "IMAGE_FORMAT_FAIL", "404"),
    SEND_CAPTCHA_ERROR("false", "SEND_CAPTCHA_ERROR", "500"),
    IMAGE_FOMMAT_NOT_RIGHT("false", "IMAGE_FOMMAT_NOT_RIGHT", "400"),
    IMAGE_IS_EMPTY("false", "IMAGE_IS_EMPTY", "401"),
    CHAIN_FAILE("false","CHAIN_FAILE","402"),
    APK_FORMAT_ERROR("false","APK_FORMAT_ERROR","403"),
    PLATFORM_LOGIN_ERROR("false","PLATFORM_LOGIN_ERROR" ,"409" );


    private String success;
    private String message;
    private String code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSuccess() {
        return success;
    }

    @Override
    public String getCode() {
        return code;
    }

    CommonResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

}
