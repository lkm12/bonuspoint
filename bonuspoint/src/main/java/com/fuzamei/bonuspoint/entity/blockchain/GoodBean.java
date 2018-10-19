package com.fuzamei.bonuspoint.entity.blockchain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品信息类
 * @author liumeng
 * @create 2018年6月25日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodBean {
    /** 商品id */
    private Long id;
    /** 商品名称*/
    private String name;
    /** 父分类名称*/
    private String type;
    /** 子分类名称*/
    private String subType;
    /** 过期时间 */
    private String expiryDate;
    /** 会员积分价格*/
    private String merchantPrice;
    /** 通用积分价格*/
    private String generalPrice;
    /** 库存*/
    private Integer num;
    /** 价值*/
    private String price;
    /** 商品详情*/
    private String info;
}
