package com.fuzamei.bonuspoint.entity.po.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/7 15:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bp_point_relation")
public class PointRelationPO {
    /** 主键 */
    @Id
    private Long id;
    /** 积分编号 */
    private Long pointId;
    /** 用户id */
    private Long userId;
    /** 积分数量 */
    private BigDecimal num;
}
