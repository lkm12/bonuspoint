package com.fuzamei.bonuspoint.enums;

import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.common.enums.ResponseEnum;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/19 14:31
 */
public enum UserResponseEnum implements ResponseEnum {

    USER_NOT_ROOT("false", "USER_NOT_ROOT", "038"),
    WRONG_ID_CARD("false","WRONG_ID_CARD","500"),
    WRONG_CASH_RATE("false", "WRONG_CASH_RATE", "250"),
    WRONG_POINT_RATE("false", "WRONG_POINT_RATE", "250"),
    WRONG_ADDRESS("false", "WRONG_ADDRESS", "422"),
    WRONG_PASSWORD("false","WRONG_PASSWORD","500"),
    USER_NOT_EXIST("false", "USER_NOT_EXIST", "218"),
    USER_IS_NOT_UNIQUE("false","USER_IS_NOT_UNIQUE","300"),

    USER_REGISTER_FAIL("true", "USER_REGISTER_FAIL", "500"),
    USER_REGISTER_SUCCESS("true", "USER_REGISTER_SUCCESS", "200"),
    COMPANY_ADD_FAIL("false", "COMPANY_ADD_FAIL", "500"),
    ADMIN_REGISTER_SUCCESS("true", "ADMIN_REGISTER_SUCCESS", "200"),
    EXIST_NAME("false", "EXIST_NAME", "251"),
    MEMBER_INFO_SUCCESS("true", "MEMBER_INFO_SUCCESS", "200"),
    MEMBER_INVITE_SUCCESS("true", "MEMBER_INVITE_SUCCESS", "200"),
    MEMBER_QRCODE_SUCCESS("true", "MEMBER_QRCODE_SUCCESS", "200"),
    MEMBER_LOGIN_SUCCESS("true", "MEMBER_LOGIN_SUCCESS", "200"),
    CONTACT_SAVE_SUCCESS("true", "CONTACT_SAVE_SUCCESS", "200"),
    CONTACT_UPDATE_SUCCESS("true", "CONTACT_UPDATE_SUCCESS", "200"),
    CONTACT_DELETE_SUCCESS("true", "CONTACT_DELETE_SUCCESS", "200"),
    CONTACT_FIND_SUCCESS("true", "CONTACT_FIND_SUCCESS", "200"),

    ADD_BANKCARD_SUCCESS("true", "ADD_BANKCARD_SUCCESS", "200"),
    FIND_BANKINFO_SUCCESS("true", "FIND_BANKINFO_SUCCESS", "200"),
    DELETE_BANKINFO_SUCCESS("true", "DELETE_BANKINFO_SUCCESS", "200"),
    FIND_BANKTYPE_SUCCESS("true", "FIND_BANKTYPE_SUCCESS", "200"),
    FIND_USERVAGUE_SUCCESS("true", "FIND_USERVAGUE_SUCCESS", "200"),
    EXCEL_SUCCESS("true", "EXCEL_SUCCESS", "200"),
    NO_COMPANY_INFO("false", "NO_COMPANY_INFO", "260"),
    COMPANY_HAS_BEEN_DELETED("false","COMPANY_HAS_BEEN_DELETED","500"),
    CONTACT_EXIST_FAIL("false", "CONTACT_EXIST_FAIL", "261"),
    CONTACT_NOT_EXIST_FAIL("false", "CONTACT_NOT_EXIST_FAIL", "262"),
    USERNAME_FORMAT_FAIL("false", "USERNAME_FORMAT_FAIL", "263"),
    PASSWORD_FORMAT_FAIL("false", "PASSWORD_FORMAT_FAIL", "264"),
    INVITEDCODE_NOT_EXIST_FAIL("false", "INVITEDCODE_NOT_EXIST_FAIL", "266"),
    INVITEDCODE_EXIST_FAIL("false", "INVITEDCODE_EXIST_FAIL", "267"),
    INVITEDCODE_EMPTY("false", "INVITEDCODE_EMPTY", "268"),
    USERNAME_PASSWORD_FAIL("false", "USERNAME_PASSWORD_FAIL", "269"),
    REGISTER_CHAIN_ERROR("false", "REGISTER_CHAIN_ERROR", "271"),
    FIND_USERINFO_PLATFORM_SUCCESS("true", "FIND_USERINFO_PLATFORM_SUCCESS", "200"),
    QRCODE_NOT_EXIST("false", "QRCODE_NOT_EXIST", "271"),
    QUERY_POINT_SUCCESS("true","QUERY_POINT_SUCCESS","200");
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

    UserResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

}
