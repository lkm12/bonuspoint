package com.fuzamei.bonuspoint.entity.po.location;

import lombok.Data;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-17 18:15
 **/
@Data
public class LocationPO {
    private String provinceName;
    private String cityName;
    private String districtName;
    private String streetName;
    private Integer zipCode;
}
