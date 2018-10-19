package com.fuzamei.bonuspoint.enums;

import com.fuzamei.common.enums.ResponseEnum;

public enum BlockChainResponseEnum implements ResponseEnum {

    ADD_BLOCK_FAILED("false", "ADD_BLOCK_FAILED", "500"),
    BLOCK_CHAIN_ERROR("false","BLOCK_CHAIN_ERROR","500"),

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

    BlockChainResponseEnum(String success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

}
