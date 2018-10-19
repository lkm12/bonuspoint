package com.fuzamei.bonuspoint.entity.po.location;

import lombok.Data;

/**
 * @program: bonus-point-cloud
 * @description: 行政区划-街道
 * @author: WangJie
 * @create: 2018-07-17 17:04
 **/
@Data
public class StreetPO {
    /** 街道编码*/
    private Long code;
    /** 街道名称*/
    private String areaName;
    /** 所属省编码*/
    private Long districtCode;
}
