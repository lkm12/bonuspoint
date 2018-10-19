package com.fuzamei.bonuspoint.entity.vo.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品兑换详情VO
 * @author liumeng
 * @create 2018年6月29日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodExchangeVO {
    /** 商品兑换记录ID*/
    private Long id;
    /** 商户名称*/
    private String companyName;
    /** 用户手机号*/
    private String mobile;
    /** 用户地址*/
    private String address;
    /** 商品名称*/
    private String goodName;
    /** 父分类名称*/
    private String typeName;
    /** 子分类名称*/
    private String subTypeName;
    /** 兑换数量*/
    private Integer num;
    /** 付款类型*/
    private String payModel;
    /** 积分数量*/
    private BigDecimal payTotal;
    /** 积分类型*/
    private BigDecimal worth;
    /** 兑换时间（时间戳）*/
    private Long createdAt;
    /** 商品状态 */
    private String status;
    /** 区块高度*/
    private Long height;
    /** 区块hash*/
    private String hash;

    private String logisticsInfo;



}
