package com.fuzamei.bonuspoint.entity.vo.asset;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 平台备付金流水返回类
 */
@Data
public class CashFlowVO {

    private Long id;
    /** 金额数量 */
    private BigDecimal amount;
    /** 状态 */
    private String statusStr;
    /** 拒绝原因 */
    private String reason;
    /** 创建时间 */
    private String createdAt;
    /** 修改时间 */
    private String updatedAt;
    /** 区块哈希 */
    private String hash;
    /** 区块高度 */
    private Long height;
    /** 出入金标志*/
    private String typeStr;
    /** 出入金类型 */
    private String categoryStr;
    /** 对方名称*/
    private String oppositeName;
    /** 商户名称*/
    private String companyName;
    /** 商户账号*/
    private String username;

}
