package com.fuzamei.bonuspoint.entity.po.location;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: bonus-point-cloud
 * @description: 行政区划-市
 * @author: WangJie
 * @create: 2018-04-22 14:39
 **/
@Data
@NoArgsConstructor
public class CityPO {
    /** 市编码*/
    private Long code;
    /** 市名称*/
    private String areaName;
    /** 所属省编码*/
    private Long provinceCode;
}
