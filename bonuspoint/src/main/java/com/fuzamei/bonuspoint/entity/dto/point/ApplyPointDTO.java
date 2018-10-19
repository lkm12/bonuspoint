package com.fuzamei.bonuspoint.entity.dto.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 申请发情积分DTO
 * @Author lmm
 * @Date 2018-7-4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyPointDTO {
    /** 集团用户id*/
    private Long uid;
    /** 申请积分数量*/
    private BigDecimal num;
    /** 活动名称*/
    private String pointName;
    /** 是否有有效其*/
    private Integer isLife;
    /** 活动起始时间*/
    private Long startTime;
    /** 活动结束时间*/
    private Long endTime;
    /** 支付密码*/
    private String payWord;
    /** 备注信息*/
    private String memo;
}
