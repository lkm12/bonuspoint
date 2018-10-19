package com.fuzamei.bonuspoint.blockchain.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lmm
 * @description
 * @create 2018/7/31 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commodity {
    /** 商户id*/
    private Long merchantId;
    /** 积分类型*/
    private Integer pointType;
    /** 会员积分数量*/
    private List<MerchantPoint> merchantPoints;
    /** 通用积分数量*/
    private BigDecimal general;
    /** 总平台数量*/
    private BigDecimal tgeneral;





}
