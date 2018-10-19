package com.fuzamei.bonuspoint.entity.po.location;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: bonus-point-cloud
 * @description: 行政区划-省
 * @author: WangJie
 * @create: 2018-04-22 14:33
 **/
@Data
@NoArgsConstructor
public class ProvincePO {
    /** 省份编码*/
    private Long code;
    /** 省份名称*/
    private String areaName;
}
