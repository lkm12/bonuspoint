package com.fuzamei.bonuspoint.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-08-17 16:40
 **/
@Data
public class Point {
    private Long id;
    private Long uid;
    private Long companyId;
    private BigDecimal numNew;
}
