package com.fuzamei.bonuspoint.entity.vo.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author lmm
 * @description 积分活动
 * @create 2018/7/5 17:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointActivityVO {
    /** 活动对应积分id*/
    private Long pointId;
    /** 活动积分总量 */
    private BigDecimal num;
    /** 积分剩余量*/
    private BigDecimal numRemain;
    /** 积分发放量*/
    private BigDecimal numUsed;
    /** 活动名称*/
    private String activityName;
    /** 是否有有效期 r\n0->否，\r\n1->是\r\n*/
    private Integer isLife;
    /** 活动起始时间*/
    private Long startAt;
    /** 活动结束时间*/
    private Long endAt;
}
