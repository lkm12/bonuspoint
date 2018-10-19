package com.fuzamei.bonuspoint.entity.vo.asset;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description 集团资产数据返回类
 * @create 2018/4/27 10:47
 */
@Data
public class CompanyCashRecordVO {

    /** 流水号 */
    private Long id;
    /** 集团名称 */
    private String companyName;
    /** 记录状态 */
    private Integer status;
    /** 充值时间 */
    private Long createTime;
    /** 到账时间 */
    private Long updateTime;
    /** 累计发行积分数量 */
    private BigDecimal num;
    /** 已失效积分数量 */
    private BigDecimal numUsed;
    /** 已发放积分数量 */
    private BigDecimal numSend;
    /** 可用积分数量 */
    private BigDecimal numRemain;
    /** 待兑换积分数量 */
    private BigDecimal numOutside;
    /** 通用积分数量 */
    private BigDecimal pointNum;
    /** 累计结算通用积分数量  */
    private BigDecimal balanceNum;
    /** 金额数量 */
    private BigDecimal amount;
    /** 累计支出备付金金额 */
    private BigDecimal amountPay;
    /** 余额比例 */
    private BigDecimal cashRate;
    /** 累计充值备付金金额 */
    private BigDecimal amountRecharge;
    /** 累计提现备付金金额 */
    private BigDecimal amountWithdraw;
    /** 活动中商品种类 */
    private Long goodNum;
    /** 当日兑换商品数量 */
    private Long goodNumToday;
    /** 本月兑换商品数量 */
    private Long goodNumMonth;
    /** 区块哈希 */
    private String hash;
    /** 区块高度 */
    private Long height;

}
