package com.fuzamei.bonuspoint.entity.vo.point;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/9 15:03
 */
@Data
public class CompanyPointVO {

    /** 平台公钥 */
    private String paltformPublickey;
    /** 银行公钥 */
    private String bankPublickey;
    /** 通用积分数量 */
    private BigDecimal num;
    /** 通用积分兑换备付金比例 */
    private BigDecimal rate;
    /** 备付金与集团积分比例 */
    private BigDecimal cashRate;
    /** 集团积分兑换通用积分比例 */
    private BigDecimal pointRate;

}
