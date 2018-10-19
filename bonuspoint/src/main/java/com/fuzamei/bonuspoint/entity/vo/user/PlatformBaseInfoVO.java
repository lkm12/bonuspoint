package com.fuzamei.bonuspoint.entity.vo.user;

import lombok.Data;


/**
 * @program: bonus-point-cloud
 * @description: 平台基本信息
 * @author: WangJie
 * @create: 2018-07-13 14:50
 **/
@Data
public class PlatformBaseInfoVO {

    /** 平台名称 */
    private String platformName;
    /** 平台详细地址 */
    private String platformAddress;
    /** 平台负责人*/
    private String platformLeader;
    /** 平台负责人手机号*/
    private String platformLeaderMobile;
    private String platformLeaderIdCard;
    /** 平台电话*/
    private String platformTelephone;
    /** 平台邮箱*/
    private String platformEmail;
    /**平台税号 */
    private String taxNumber;
    private Long platformCreatedAt;
    /** 平台的注册公司名称*/
    private String companyName;
    /**平台logo*/
    private String logoUrl;

    /**积分兑换人民币比例*/
    private Float pointRate;
}
