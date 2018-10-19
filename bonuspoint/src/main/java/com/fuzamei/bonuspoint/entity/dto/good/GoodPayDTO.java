package com.fuzamei.bonuspoint.entity.dto.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lmm
 * @description
 * @create 2018/7/25 17:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodPayDTO {
    /** 支付方式*/
    private Integer payModel;
    /** 交易密码*/
    private String payword;
}
