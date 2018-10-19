package com.fuzamei.bonuspoint.entity.dto.good;

import lombok.Data;


/**
 * @program: bonus-point-cloud-2
 * @description: 商铺商铺概述
 * @author: WangJie
 * @create: 2018-10-17 15:34
 **/
@Data
public class CompanyGoodsInfoDTO {
    /** 商品种类总量*/
    private Integer goodsTotalNum;
    /** 商品累计销售量*/
    private Integer goodsTotalUsed;
}
