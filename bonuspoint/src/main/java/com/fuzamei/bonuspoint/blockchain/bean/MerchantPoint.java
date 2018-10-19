package com.fuzamei.bonuspoint.blockchain.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author lmm
 * @description 商户发行的积分
 * @create 2018/7/31 14:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantPoint {
    /** 积分id*/
    private Long id ;
    /** 积分数量*/
    private BigDecimal quantity;
    /** 过期时间*/
    private Long expiration;

}
