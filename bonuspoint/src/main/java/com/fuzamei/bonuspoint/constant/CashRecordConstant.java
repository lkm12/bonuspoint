package com.fuzamei.bonuspoint.constant;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/25 14:28
 */
public class CashRecordConstant {

    /** 成功 */
    public final static int SUCCESS = 2;
    /** 失败 */
    public final static int FAILURE = 101;
    /** 已过期 */
    public final static int OUT_OF_DATE = 3;
    /** 待审核 */
    public final static int WAIT_FOR_CHECK = 0;
    /** 审核通过 */
    public final static int CHECK_PASS = 1;
    /** 审核不通过 */
    public final static int CHECK_NOT_PASS = 2;
    /** 收入资产 */
    public final static int INCOME_ASSETS = 1;
    /** 支出资产 */
    public final static int COST_ASSETS = 2;
    /** 充值备付金 */
    public final static int RECHARGE_CASH = 1;
    /**集团支付备付金*/
    public final static int PAY_CASH = 2;
    /**平台收入备付金*/
    public final static int INCOME_CASH = 3;
    /** 备付金提现 */
    public final static int WITHDRAW_CASH = 6;
    /** 兑换通用积分 */
    public final static int POINT_EXCHANGE = 4;
    /** 结算通用积分 */
    public final static int POINT_BALANCE = 5;
    /**未审核*/
    public final static int STATUS_NO_EXAM = 0;
}
