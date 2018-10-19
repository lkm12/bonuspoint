package com.fuzamei.bonuspoint.enums;

import com.fuzamei.common.enums.ResponseEnum;

public enum AddressResponseEnum  implements ResponseEnum {


    NO_DEFAULT_ADDRESS_YET("true", "NO_DEFAULT_ADDRESS_YET", "300"),
    ADDRESS_NOT_EXIT("false","ADDRESS_NOT_EXIT","500"),

    ;

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

    AddressResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

}
