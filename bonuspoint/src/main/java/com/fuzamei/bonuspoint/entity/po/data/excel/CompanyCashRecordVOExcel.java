package com.fuzamei.bonuspoint.entity.po.data.excel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyCashRecordVOExcel{

    /** 用户id */
    private Long uid;
    /** 集团名称 */
    private String companyName;
    /** 区块链随机码 */
    private String blockSid;
    /** 记录状态 */
    private Integer status;
    /** 充值时间 */
    private Long createTime;
    /** 到账时间 */
    private Long updateTime;
    /** 累计申请数量 */
    private Long num;
    /** 已兑换积分数量 */
    private Long numUsed;
    /** 剩余积分数量 */
    private Long numRemain;
    /** 待兑换数量 */
    private Long numOutside;
    /** 通用积分数量 */
    private Long pointNum;
    /** 积分兑换通用积分比例 */
    private Float pointRate;
    /** 金额数量 */
    private BigDecimal amount;
    /** 累计支出备付金金额 */
    private BigDecimal amountPay;
    /** 备付金比例 */
    private Float cashRate;
    /** 累计充值备付金金额 */
    private BigDecimal amountRecharge;
    /** 累计提现备付金金额 */
    private BigDecimal amountWithdraw;
    /** 当前销售商品 */
    private Integer goodNum;
    /** 今日销售商品数量 */
    private Integer goodNumToday;
    /** 本月销售商品数量 */
    private Integer goodNumMonth;
    /**充值时间DateTime*/
    private String createTimeDate;
    /**到账时间DateTime*/
    private String updateTimeDate;
}
