package com.fuzamei.bonuspoint.entity.po.user;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-13 14:57
 **/
@Data
@Table(name = "bp_platform_info")
public class PlatformInfoPO {
    /**平台信息编号*/
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    /** 平台管理用户编号*/
    private Long uid;
    /** 平台名称 */
    private String platformName;
    /** 平台的注册公司名称*/
    private String companyName;
    /**平台logo*/
    private String logoUrl;
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
    /** 当前备付金量*/
    private BigDecimal amount;
    /** 备付金比例 */
    private Float cashRate;
    /** 积分兑换比例 */
    private Float pointRate;
    private Long platformCreatedAt;
    private Long platformUpdatedAt;
    /** 平台标识 */
    private String mark;
}
