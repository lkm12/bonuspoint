package com.fuzamei.bonuspoint.constant;

/**
 * 订单状态常量
 * @author liumeng
 * @create 2018年4月26日
 */
public class GoodOrderConstant {
    /** 待结算*/
    public static final int GOOD_TO_SETTLED = 0;
    /** 待发货*/
    public static final int GOOD_TO_DELIVERED = 1;
    /** 已发货*/
    public static final int GOOD_IN_TRANST = 2;
    /** 待确认收货*/
    public static final int GOOD_TO_CONFIRM = 3;
    /** 成功交易*/
    public static final int GOOD_SUCCESSED_TRADE = 4;
    /** 退货中待集团确认*/
    public static final int BACK_GOOD_CONFIRM = 5;
    /** 退货中集团确认*/
    public static final int BACK_GOOD_SURE = 6;
    /** 退货用户发货*/
    public static final int BACK_GOOD_SEND = 7;
    /** 退货成功*/
    public static final int BACK_GOOD_SUCCESS = 8;
    /** 买家关闭交易*/
    public static final int CLOSE_GOOD_ORDER = 99;
    /** 超时失效*/
    public static final int TIME_OUT_CLOSE = 100;
    /** 隐藏订单*/
    public static final int HIDDER_ORDER = 101;
    /** 显示失败订单（超时失效和买家关闭交易）*/
    public static final int ORDER_ALL_FAILED = 199;
    /** 会员积分支付*/
    public static final int PAY_MODEL_PRICE = 1;
    /** 通用积分支付*/
    public static final int PAY_MODEL_GLOBAL_PRICE = 2;
}
