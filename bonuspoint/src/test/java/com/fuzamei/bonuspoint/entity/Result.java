package com.fuzamei.bonuspoint.entity;

import lombok.Data;

/**
 * @author Q版小李
 * @description
 * @create 2018/9/12 18:23
 */
@Data
public class Result {

    /** 结果状态 */
    private String success;

    /** 结果信息 */
    private String message;

    /** 结果码 */
    private String code;

    /** 返回数据 */
    private Token data;

}
