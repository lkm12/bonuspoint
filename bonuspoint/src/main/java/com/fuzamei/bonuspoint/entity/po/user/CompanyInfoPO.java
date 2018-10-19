package com.fuzamei.bonuspoint.entity.po.user;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/17
 */
@Data
@Table(name = "bp_company_info")
public class CompanyInfoPO {

    /** 集团id */
    @Id
    private Long id;
    /** 用户id */
    private Long uid;

    /** 集团名称 */
    private String companyName;
    /** 集团图像url*/
    private String logoUrl;
    /** 集团详细地址 */
    private String companyAddress;
    /** 集团负责人 */
    private String companyLeader;
    /** 法人身份证号 */
    private String companyLeaderIdCard;
    /** 法人手机 */
    private String companyLeaderMobile;
    /** 集团电话 */
    private String companyTelephone;
    /** 公司邮箱 */
    private String companyEmail;
    /** 备付金余额 */
    private BigDecimal amount;
    /** 备付金账户 */
    private String cashNum;
    /** 备付金比例 */
    private Float cashRate;
    /** 通用积分兑换比例（N：1通用积分）*/
    private Float pointRate;
    /** 创建时间 */
    private Long createdAt;
    /** 商户状态 */
    private Integer companyStatus;

}

