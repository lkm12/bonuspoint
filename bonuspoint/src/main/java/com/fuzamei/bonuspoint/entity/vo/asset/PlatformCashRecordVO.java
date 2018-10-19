package com.fuzamei.bonuspoint.entity.vo.asset;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description 平台资产数据返回类
 * @create 2018/4/28 16:38
 */
@Data
public class PlatformCashRecordVO {

    /** 流水号 */
    private Long id;
    /** 商户编号 */
    private Long companyId;
    /** 商户名称 */
    private String companyName;
    /** 充值时间 */
    private Long createTime;
    /** 到账时间 */
    private Long updateTime;
    /** 备用金比例 */
    private BigDecimal cashRate;
    /** 积分比例 */
    private BigDecimal pointRate;
    /** 金额 */
    private BigDecimal amount;
    /** 余额比例 */
    private BigDecimal numRate;
    /** 会员积分余额 */
    private BigDecimal numRemain;
    /** 流通中会员积分 */
    private BigDecimal numOutside;
    /** 累计兑换通用积分 */
    private BigDecimal numExchange;
    /** 累计结算通用积分 */
    private BigDecimal numBalance;
    /** 可兑换金额 */
    private BigDecimal amountExchange;
    /** 通用积分 */
    private BigDecimal generalPoint;
    /** 商户积分总数 */
    private BigDecimal pointTotal;
    /** 用户id */
    private Long memberId;
    /** 用户手机号 */
    private String mobile;
    /** 用户地址 */
    private String address;
    /** 推荐人 */
    private String referee;
    /** 状态 */
    private Integer status;
    /** 区块哈希 */
    private String hash;
    /** 区块高度 */
    private Long height;
}
