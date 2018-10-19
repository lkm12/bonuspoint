package com.fuzamei.bonuspoint.entity.po.location;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: bonus-point-cloud
 * @description: 行政区划-县，地级市
 * @author: WangJie
 * @create: 2018-04-22 14:42
 **/
@Data
@NoArgsConstructor
public class DistrictPO {
    /** 县编码*/
    private Long code;
    /** 县名称*/
    private String areaName;
    /** 所属市编码*/
    private Long cityCode;
    /** 邮编 */
    private Integer zipCode;

}
