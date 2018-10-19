package com.fuzamei.bonuspoint.entity.po.data.excel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 18519 on 2018/5/12.
 * lkm
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExcelPO {
    /** 记录编号*/
    private Long recordId;
    /** 手机*/
    private String mobile;
    /** 用户地址*/
    private String publickey;
    /** 商品名称*/
    private String goodName;
    /** 兑换积分数量*/
    private Long exchangeNum;
    /** 支付积分类型*/
    private Integer payMode;
    /** 积分数量*/
    private BigDecimal pointNum;
    /** 积分价值*/
    private Long money;
    /** 商品分类名称*/
    private String goodSidName;
    /** 记录时间戳*/
    private Long updateTime;
    /** 通用积分数量*/
    private BigDecimal generalPoint;
    /** 积分兑换比率*/
    private Float pointRate;
    /** 记录时间 (dateTime 类型)*/
    private String updateTimeDate;
    /** 积分名称*/
    private String name;
    /** 集团名称*/
    private String companyName;

    private String hash;

    private Long height;

    private String pointRateStr;
    //备付金
    private BigDecimal cashNum;
    //备付金/平台积分
    private Float platformPointRate;

    private String cashNumStr;
}