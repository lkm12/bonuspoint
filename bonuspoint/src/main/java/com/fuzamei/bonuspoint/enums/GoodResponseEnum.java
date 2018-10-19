package com.fuzamei.bonuspoint.enums;

import com.fuzamei.common.enums.ResponseEnum;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/19 14:21
 */
public enum GoodResponseEnum implements ResponseEnum {

    GOOD_TYPE_FAILED("false", "GOOD_TYPE_FAILED", "001"),
    GOOD_TYPE_ID("false", "GOOD_TYPE_ID", "002"),
    GOOD_TYPE_NAME("false", "GOOD_TYPE_NAME", "003"),
    GOOD_PACKET_FAILED("false", "GOOD_PACKET_FAILED", "004"),
    GOOD_SUB_TYPE_ID("false", "GOOD_SUB_TYPE_ID", "004"),
    GOOD_SUB_TYPE_PID("false", "GOOD_SUB_TYPE_PID", "005"),
    GOOD_SUB_TYPE_PID_NOT_EXIT("false", "GOOD_SUB_TYPE_PID_NOT_EXIT", "006"),
    GOOD_DELETE_SUB_TYPE_FIRST("false","GOOD_DELETE_SUB_TYPE_FIRST","500"),
    SUB_TYPE_HAS_GOODS("false","SUB_TYPE_HAS_GOODS","500"),
    GOOD_SUB_TYPE_NAME("false", "GOOD_SUB_TYPE_NAME", "007"),
    GOOD_ID("false", "GOOD_ID", "009"),
    GOOD_GID("false", "GOOD_GID", "010"),
    GOOD_SID("false", "GOOD_SID", "011"),
    GOOD_NAME("false", "GOOD_NAME", "012"),
    GOOD_PRICE("false", "GOOD_PRICE", "013"),
    GOOD_NUM("false", "GOOD_NUM", "014"),
    GOOD_WORTH("false", "GOOD_WORTH", "015"),
    GOOD_IMAGE_DELETE("false", "GOOD_IMAGE_DELETE", "016"),
    GOOD_IMAGE_ADD("false", "GOOD_IMAGE_ADD", "017"),
    GOOD_ORDER_ID("false", "GOOD_ORDER_ID", "020"),
    GOOD_ORDER_UID("false", "GOOD_ORDER_UID", "021"),
    GOOD_ORDER_GID("false", "GOOD_ORDER_GID", "022"),
    GOOD_ORDER_NUM("false", "GOOD_ORDER_NUM", "023"),
    GOOD_ORDER_ADDRESS("false", "GOOD_ORDER_ADDRESS", "024"),
    GOOD_ORDER_GID_NOEXIT("false", "GOOD_ORDER_GID_NOEXIT", "025"),
    GOOD_ORDER_ADDRESS_NOEXIT("false", "GOOD_ORDER_ADDRESS_NOEXIT", "026"),
    GOOD_ORDER_UID_DIFF("false", "GOOD_ORDER_UID_DIFF", "027"),
    GOOD_ORDER_NOEXIT("false", "GOOD_ORDER_NOEXIT", "028"),
    GOOD_ORDER_CANNOT_BACK("false", "GOOD_ORDER_CANNOT_BACK", "029"),
    GOOD_ORDER_NOT_MATCH("false", "GOOD_ORDER_NOT_MATCH", "030"),
    GOOD_ORDER_LOGISTICSINFO("false", "GOOD_ORDER_LOGISTICSINFO", "031"),
    GOOD_ORDER_BACK_LOGISTICSINFO("false", "GOOD_ORDER_BACK_LOGISTICSINFO", "031"),
    GOOD_ORDER_USER_NOT_EXIT("false", "GOOD_ORDER_USER_NOT_EXIT", "032"),
    GOOD_PAY_MODEL_ERROR("false", "GOOD_PAY_MODEL_ERROR", "033"),
    GOOD_PAY_POINT_OUTOF("false", "GOOD_PAY_POINT_OUTOF", "034"),
    GOOD_PAY_GENERAL_POINT_OUTOF("false", "GOOD_PAY_GENERAL_POINT_OUTOF", "034"),
    GOOD_PAY_NOT_POINT("false", "GOOD_PAY_NOT_POINT", "034"),
    GOOD_NOT_PIONT_RATE("false", "GOOD_NOT_PIONT_RATE", "035"),
    GOOD_GOLABEL_PRICE("false", "GOOD_GOLABEL_PRICE", "036"),
    GOOD_UID("false", "GOOD_UID", "037"),
    GOOD_HAVING_DELETE("false", "GOOD_HAVING_DELETE", "038"),
    GOOD_STOCK_NOT_ENOUGH("false", "GOOD_STOCK_NOT_ENOUGH", "040"),
    GOOD_LIFE_TIME_ERROR("false","GOOD_LIFE_TIME_ERROR","041"),
    GOOD_PAYWORD_ERROR("false","GOOD_PAYWORD_ERROR","042"),
    GOOD_NOT_MATCH("false","GOOD_NOT_MATCH","043"),
    PAYWORD_ENCRYPT_ERROR("false","PAYWORD_ENCRYPT_ERROR","044"),
    GOOD_LIFE_ERROR("false","GOOD_LIFE_ERROR","045"),
    GOOD_PAY_TIME_NOT_COMING("false","GOOD_PAY_TIME_NOT_COMING","046"),
    USER_DEFAULT_ADDRESS_NOT_EXIT("false","USER_DEFAULT_ADDRESS_NOT_EXIT","047"),
    TYPE_HAVED_USED("false","TYPE_HAVED_USED","048");

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

    GoodResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }
}
