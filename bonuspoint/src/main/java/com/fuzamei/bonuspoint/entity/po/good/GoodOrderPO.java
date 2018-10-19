package com.fuzamei.bonuspoint.entity.po.good;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单信息
 * @author liumeng
 * @create 2018年4月23日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodOrderPO {
    /** 流水号  */
    private Long id;
    /** 区块链回执*/
    private String blockSid;
    /** 用户uid*/
    private Long uid;
    /** 集团id*/
    private Long gid;
    /** 商品Id*/
    private Long goodId;
    /** 商品会员积分单价*/
    private  BigDecimal goodPrice;
    /** 商品通用积分单价*/
    private BigDecimal goodGlobalPrice;
    /** 订购数量*/
    private Integer num;
    /**支付方式(1->会员积分，2->通用积分)*/
    private Integer payMode;
    /** 支付总价*/
    private BigDecimal payTotal;
    /** 配送方式*/
    private String distribution;
    /** 卖家留言*/
    private String message;
    /** 收件信息*/
    private Long addressId;
    /**收货人姓名*/
    private String addressName;
    /** 收货人电话*/
    private String addressMobile;
    /** 收货地址 */
    private String addressDistrict;
    /** 收货详情*/
    private String addressDetail;

    /** 订单状态 0->待结算,1->待发货,2->运输中,3->待确认收货,4->成功交易,5->退货中待集团确认,6->退货成功,99->买家关闭交易,100->超时失效,101->隐藏订单*/
    private Integer status;
    /** 物流订单号*/
    private String logisticsInfo;
    /** 退货订单号*/
    private String backLogisticsInfo;
    /** 退货理由*/
    private String backMemo;
    /** 支付时间*/
    private Long payedAt;
    /** 成交时间*/
    private Long bargainAt;
    /** 创建时间*/
    private Long createdAt;
    /** 更新时间*/
    private Long updatedAt;
    /** 区块高度*/
    private Long height;
    /** 区块hash*/
    private String hash;
    
    
}
