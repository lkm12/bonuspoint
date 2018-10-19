package com.fuzamei.bonuspoint.enums;

import com.fuzamei.common.enums.ResponseEnum;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/19 14:24
 */
public enum SafeResponseEnum implements ResponseEnum {

    MOBILE_BLANK("false","MOBILE_BLANK","500"),
    EMAIL_BLANK("false","EMAIL_BLANK","500"),
    CAPTCHA_TYPE_WRONG("false","CAPTCHA_TYPE_WRONG","500"),
    TWO_DIFFERENT_INPUT("false", "TWO_DIFFERENT_INPUT", "201"),
    MOBILE_BOUNDED("false", "MOBILE_BOUNDED", "202"),
    MOBILE_FORMAT_ERROR("false", "MOBILE_FORMAT_ERROR", "203"),
    TELEPHONE_FORMAT_ERROR("false", "TELEPHONE_FORMAT_ERROR", "203"),
    EMAIL_BOUNDED("false", "EMAIL_BOUNDED", "204"),
    EMAIL_UNBOUND("false", "EMAIL_NOT_BOUND", "204"),
    EMAIL_FORMAT_ERROR("false", "EMAIL_FORMAT_ERROR", "205"),
    EMAIL_WRONG("false", "EMAIL_WRONG", "204"),
    UPDATE_SUCCESS("true", "UPDATE_SUCCESS", "200"),
    UPDATE_FAIL("false", "UPDATE_FAIL", "206"),
    ORIGINAL_PASSWORD_ERROR("false", "ORIGINAL_PASSWORD_ERROR", "207"),

    AUTHORITY_WRONG("false", "AUTHORITY_WRONG", "210"),
    MOBILE_UNBOUND("false", "MOBILE_UNBOUND", "213"),
    MOBILE_WRONG("false", "MOBILE_WRONG", "214"),
    MD5_ERROR("false", "MD5_ERROR", "215"),
    MOBILE_EXIST("false", "MOBILE_EXIST", "216"),
    CAPTCHA_WRONG("false", "CAPTCHA_WRONG", "217");

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

   SafeResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }
}
