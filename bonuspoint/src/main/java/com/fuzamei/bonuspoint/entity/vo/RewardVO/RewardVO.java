package com.fuzamei.bonuspoint.entity.vo.RewardVO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description: 奖励规则
 * @author: WangJie
 * @create: 2018-09-12 11:55
 **/
@Data
public class RewardVO {
    private Long id;
    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 奖励活动名称
     */
    private String rewardName;
    /**
     * 积分类型 1通用积分 2商户积分
     */
    private String pointType;
    /**
     * 如果积分类型为商户积分，需填写商户信息id
     */
    private Long companyId;
    /**
     * 商户名
     */
    private String companyName;
    /**
     * 积分id
     */
    private Long pointId;
    /**
     * 积分名
     */
    private String pointName;
    /**
     * 积分剩余量
     */
    private BigDecimal numRemain;
    /**
     * 奖励描述
     */
    private String description;
    /**
     * 注册人奖励
     */
    private BigDecimal registerReward;
    /**
     * 一级奖励数量
     */
    private BigDecimal firstInviteReward;
    /**
     * 二级奖励数量
     */
    private BigDecimal secondInviteReward;
    /**
     * 三级奖励数量
     */
    private BigDecimal thirdInviteReward;
    /**
     * 是否生效 0失效，1生效，2待生效
     */
    private Integer status;

    /**
     * 添加时间
     */
    private Long createdAt;

}
