package com.fuzamei.bonuspoint.enums;
import com.fuzamei.common.enums.ResponseEnum;
/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/19 10:18
 */
public enum AssetResponseEnum implements ResponseEnum {

    COMPANY_UID("false", "COMPANY_UID", "101"),
    COMPANY_NAME("false", "COMPANY_NAME", "102"),
    COMPANY_ADDRESS("false", "COMPANY_ADDRESS", "103"),
    COMPANY_LEADER("false", "COMPANY_LEADER", "104"),
    COMPANY_PROVISIONS_LACK("false", "COMPANY_PROVISIONS_LACK", "105"),
    CASH_RECORD_ID("false", "CASH_RECORD_ID", "106"),
    CASH_RECORD_AMOUNT("false", "CASH_RECORD_AMOUNT", "107"),
    CASH_RECORD_PASSWORD("false", "CASH_RECORD_PASSWORD", "108"),
    CASH_RECORD_TYPE("false", "CASH_RECORD_TYPE", "109"),
    SAVE_RECORD_FAILURE("false", "SAVE_RECORD_FAILURE", "110"),
    UPDATE_CASH_FAILURE("false", "UPDATE_CASH_FAILURE", "111");

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

    AssetResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

}
