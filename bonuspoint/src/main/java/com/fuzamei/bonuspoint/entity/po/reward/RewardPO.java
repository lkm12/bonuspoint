package com.fuzamei.bonuspoint.entity.po.reward;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description: 邀请奖励规则
 * @author: WangJie
 * @create: 2018-09-11 17:33
 **/
@Data
@Table(name = "bp_reward")
public class RewardPO {

    @Id
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
     * 积分id
     */
    private Long pointId;
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
    /**
     * 修改时间
     */
    private Long updatedAt;

}
