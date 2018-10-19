package com.fuzamei.bonuspoint.entity.dto.reward;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description: 邀请奖励规则
 * @author: WangJie
 * @create: 2018-09-11 17:33
 **/
@Data
public class RewardDTO {

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 奖励活动名称
     */
    @NotBlank(message = "PARAMETER_ERROR")
    private String rewardName;
    /**
     * 积分类型 1通用积分 2商户积分
     */
    @NotBlank(message = "PARAMETER_ERROR")
    @Pattern(regexp = "[1-2]",message = "PARAMETER_ERROR")
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
    @NotBlank(message = "PARAMETER_ERROR")
    private String description;

    /**
     * 注册人奖励
     */
    @Min(value = 0,message = "PARAMETER_ERROR")
    private BigDecimal registerReward;
    /**
     * 一级奖励数量
     */
    @Min(value = 0,message = "PARAMETER_ERROR")
    private BigDecimal firstInviteReward;
    /**
     * 二级奖励数量
     */
    @Min(value = 0,message = "PARAMETER_ERROR")
    private BigDecimal secondInviteReward;
    /**
     * 三级奖励数量
     */
    @Min(value = 0,message = "PARAMETER_ERROR")
    private BigDecimal thirdInviteReward;



}
