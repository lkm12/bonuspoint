package com.fuzamei.bonuspoint.entity.dto.user;

import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @create 2018/4/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyInfoDTO extends PageDTO {

    /** 集团信息编号 */
    private Long id;
    /** 用户id */
    private Long uid;
    /** 用户名 */
    private String username;
    /**集团所属平台id*/
    private Long pId;
    /** 集团名称 */
    private String companyName;
    /** 集团详细地址 */
    private String companyAddress;
    /** 公司logo */
    private String logoUrl;
    /** 集团负责人 */
    private String companyLeader;
    /** 法人身份证号*/
    private String companyLeaderIdCard;
    /** 法人手机 */
    private String companyLeaderMobile;
    /** 集团电话 */
    private String companyTelephone;
    /** 公司邮箱 */
    private String companyEmail;
    /** 银行 */
    private String bank;
    /** 支行 */
    private String bankBranch;
    /** 银行账户名 */
    private String bankAccount;
    /** 银行账号 */
    private String bankNum;
    /** 当前备付金 */
    private BigDecimal amount;
    /** 备付金账户 */
    private String cashNum;
    /** 备付金比例 */
    private Float cashRate;
    /** 通用积分兑换比例（N：1通用积分） */
    private Float pointRate;

    /** 排序字段 */
    private String orderBy;

    /**创建时间*/
    private Long createdAt;


}
