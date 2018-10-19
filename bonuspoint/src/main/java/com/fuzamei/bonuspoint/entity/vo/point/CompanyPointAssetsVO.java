package com.fuzamei.bonuspoint.entity.vo.point;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 集团资产信息
 * @author liumeng
 * @create 2018年5月8日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPointAssetsVO {
    /** 集团id*/
    private Long id;
    /** 管理员id*/
    private Long uid;
    /** 集团名称*/
    private String company_name;
    /** 当前备付金*/
    private BigDecimal amount;
    /** 备付金账户*/
    private String cashNum;
    /** 备付金比例*/
    private Float cashRate;
    /** 通用积分兑换比例（N：1通用积分）*/
    private Float pointRate;
    /** 已经申请量*/
    private BigDecimal sums;
    /** 剩余积分总量*/
    private BigDecimal numRemains;
    /** 已使用积分总量*/
    private BigDecimal numUseds;
    /** 可申请积分总量*/
    private BigDecimal totalAmount;
}
