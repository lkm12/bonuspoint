package com.fuzamei.bonuspoint.enums;

import com.fuzamei.common.enums.ResponseEnum;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/19 15:06
 */
public enum PointResponseEnum implements ResponseEnum {

    OTHER_NOT_EXIST("false", "OTHER_NOT_EXIST", "319"),
    COMPANY_POINT_NOT_EXIST("false", "COMPANY_POINT_NOT_EXIST", "320"),
    POINT_TRANSFER_FAIL("false", "POINT_TRANSFER_FAIL", "321"),
    POINT_NOT_EXIST("false", "POINT_NOT_EXIST", "323"),
    GENERAL_POINT_NOT_EXIST("false", "GENERAL_POINT_NOT_EXIST", "326"),
    COMPANY_PLATFORM_ERROR("false", "COMPANY_PLATFORM_ERROR", "327"),
    POINT_TIME_ERROR("false", "POINT_TIME_ERROR", "328"),
    COMPANY_OR_PAYWORD_ERROR("false", "COMPANY_OR_PAYWORD_ERROR", "301"),
    SAVE_POINT_ERROR("false", "SAVE_POINT_ERROR", "302"),
    CHECK_ERROR("false", "CHECK_ERROR", "303"),
    POINT_EXCHANGE_SUCCESS("true", "POINT_EXCHANGE_SUCCESS", "200"),
    USER_PASSWORD_NULL("false", "USER_PASSWORD_NULL", "305"),
    POINT_NUM_NULL("false", "POINT_NUM_NULL", "306"),
    POINT_ID_NULL("false", "POINT_ID_NULL", "307"),
    USER_ACCOUNT_NULL("false", "USER_ACCOUNT_NULL", "308"),
    USER_PUBLICKEY_NULL("false", "USER_PUBLICKEY_NULL", "309"),
    SAVE_POINTRECORD_ERROR("false", "SAVE_POINTRECORD_ERROR", "310"),
    POINT_FIND_SUCCESS("true", "POINT_FIND_SUCCESS", "200"),
    POINT_COMPANY_NOT_BANK("false", "POINT_COMPANY_NOT_BANK", "311"),
    POINT_INFO_ERROR("false", "POINT_INFO_ERROR", "312"),
    COMPANY_POINT_NOT_ENOUGH("false", "COMPANY_POINT_NOT_ENOUGH", "313"),
    COMPANY_POINT_NOT_HOLD("false", "COMPANY_POINT_NOT_HOLD", "314"),
    COMPANY_USER_NOT_MATCH("false", "COMPANY_USER_NOT_MATCH", "315"),
    POINT_LIFE_TIME("false", "POINT_LIFE_TIME", "316"),
    SEND_POINT_FAILURE("false", "SEND_POINT_FAILURE", "317"),
    BALANCE_POINTRE_FAILURE("false", "BALANCE_POINTRE_FAILURE", "318"),
    PLATFORM_POINT_ERROR("false", "PLATFORM_POINT_ERROR", "319"),
    EXAMINE_REPEAT("false", "EXAMINE_REPEAT", "320"),
    BIG_GENERAL_POINT_NOT_EXIST("false", "BIG_GENERAL_POINT_NOT_EXIST", "321"),
    PAY_BIG_POINT_OUTOF("false", "PAY_BIG_POINT_OUTOF", "322"),
    COMPANY_INFO_NOT_ENOUGH("false", "COMPANY_INFO_NOT_ENOUGH", "323"),
    POINT_APPLY_NUM_ERROR("false","POINT_APPLY_NUM_ERROR","324"),
    POINT_NAME_IS_NULL("false","POINT_NAME_IS_NULL","325"),
    POINT_CONVERT_SUCCESS("true", "POINT_CONVERT_SUCCESS", "200"),
    COMPANY_NOT_EXIST("false","COMPANY_NOT_EXIST","326"),
    GENERALPOINT_BC_FAIL("false","GENERALPOINT_BC_FAIL","327"),
    POINT_NUM_ZERO("false","POINT_NUM_ZERO","328"),
    POINT_UPDATE_ERROR("false","POINT_UPDATE_ERROR","329"),
    POINT_ROLE_TRANSFER_ERROR("false","POINT_ROLE_TRANSFER_ERROR","330");

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

    PointResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

}
