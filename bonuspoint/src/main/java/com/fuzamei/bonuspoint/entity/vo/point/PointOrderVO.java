package com.fuzamei.bonuspoint.entity.vo.point;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 用户可用积分VO(可用积分，不包括过期积分)
 * @author liumeng
 * @create 2018年5月9日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointOrderVO {
    /** 主键（bp_point_relation 主键）*/
    private Long id ; 
    /** 用户id*/
    private Long userId;
    /** 积分id*/
    private Long pointId;
    /** 集团Id*/
    private Long company;
    /** 集团负责人Id*/
    private Long companyUid;
    /** 积分数量*/
    private BigDecimal num;
    /** 兑换积分率*/
    private float pointRate;
    /** 是否有有有效期期*/
    private Integer isLife;
    /** 积分开始时间*/
    private Long startAt;
    /** 积分失效时间*/
    private Long endAt;
}
