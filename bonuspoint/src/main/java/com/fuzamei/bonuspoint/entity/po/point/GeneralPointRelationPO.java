package com.fuzamei.bonuspoint.entity.po.point;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 通用积分关联表
 * @author liumeng
 * @create 2018年5月9日
 */
@Data
@Table(name = "bp_general_point_relation")
public class GeneralPointRelationPO {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 平台id */
    private Long platformId;
    /** 用户id */
    private Long userId;
    /** 持有通用积分数量 */
    private BigDecimal num;

}
