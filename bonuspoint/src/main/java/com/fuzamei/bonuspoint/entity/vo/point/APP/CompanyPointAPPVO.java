package com.fuzamei.bonuspoint.entity.vo.point.APP;

import lombok.Data;

@Data
public class CompanyPointAPPVO {

    /** 集团id*/
    private String id;
    /** 集团名称 */
    private String companyName;
    /** 集团公钥*/
    private String publickey;
    /** 集团头像*/
    private String headimgurl;
    /** 集团积分数量*/
    private String num;
    /** 通用积分兑换比例（N：1通用积分）*/
    private String pointRate;
    /** 可兑换成通用积分数量*/
    private String numExchange;
    /** 通用积分数量*/
    private String generalPoint;

}
