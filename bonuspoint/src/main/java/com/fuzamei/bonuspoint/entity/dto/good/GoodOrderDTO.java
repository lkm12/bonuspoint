package com.fuzamei.bonuspoint.entity.dto.good;

import java.math.BigDecimal;

import com.fuzamei.bonuspoint.entity.vo.good.GoodOrderVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单DTO
 * @author liumeng
 * @create 2018年4月25日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodOrderDTO {
    /** 商品Id*/
    private Long goodId;
    /** 订购数量*/
    private Integer num;
    /**支付方式(1->会员积分，2->通用积分)*/
    private Integer payMode;
    /** 配送方式*/
    private String distribution;
    /** 买家留言*/
    private String message;
    /** 收件信息*/
    private Long addressId;
    /** 物流订单号*/
    private String logisticsInfo;
    /** 退货订单号*/
    private String backLogisticsInfo;
    /** 退货理由*/
    private String backMemo;
}
