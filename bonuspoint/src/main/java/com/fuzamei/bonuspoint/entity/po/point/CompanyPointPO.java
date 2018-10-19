package com.fuzamei.bonuspoint.entity.po.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by 18519 on 2018/5/7.
 *
 * lkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPointPO {

    /** 集团id*/
    private Long id;
    /** 集团名称 */
    private String companyName;
    /** 集团公钥*/
    private String publickey;
    /** 集团头像*/
    private String img;
    /** 集团积分数量*/
    private BigDecimal num;
    /** 通用积分兑换比例（N：1通用积分）*/
    private Float pointRate;
    /** 可兑换成通用积分数量*/
    private Long numExchange;
    /** 通用积分数量*/
    private Long generalPoint;

}
