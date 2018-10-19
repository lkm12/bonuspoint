package com.fuzamei.bonuspoint.constant;

/**
 * @program: bonus-point-cloud
 * @description: 奖励规则状态
 * @author: WangJie
 * @create: 2018-09-12 10:26
 **/
public class RewardRuleStatus {
    /**
     * 失效
     */
    public static final int FINISHED = 0;
    /**
     * 生效
     */
    public static final int EFFECTIVE = 1;

    /**
     * 待生效
     */
    public static final int PENDING_EFFECTIVE= 2;

    /**
     * 删除
     */
    public static final int DELETE = 3;

}
