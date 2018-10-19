package com.fuzamei.bonuspoint.entity.po.good;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: bonus-point-cloud-2
 * @description: 商品奖励分红
 * @author: WangJie
 * @create: 2018-10-11 17:54
 **/
@Data
@Table(name = "bp_good_rebate")
public class GoodRebatePO {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    private Long goodId;
    private Integer status;
    private Float rate;
    private Long createdAt;
    private Long updatedAt;
}
