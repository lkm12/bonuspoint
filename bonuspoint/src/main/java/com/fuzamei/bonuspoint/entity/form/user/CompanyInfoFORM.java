package com.fuzamei.bonuspoint.entity.form.user;

import com.fuzamei.bonuspoint.validation.*;
import com.fuzamei.bonuspoint.validation.group.CompanyInfo;
import lombok.Data;


import javax.validation.constraints.*;

/**
 * @author wangjie
 * @create 2018/6/28
 */
@Data
public class CompanyInfoFORM  {

    /**用户名*/
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    private String username;
    /** 管理员手机号 */
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    @Phone(groups = CompanyInfo.createCompanyInfo.class)
    private String mobile;
    /**用户密码*/
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    @Password(groups = CompanyInfo.createCompanyInfo.class)
    private String password;
    /**交易密码*/
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    @Payword(groups = CompanyInfo.createCompanyInfo.class)
    private String payword;
    /** 集团名称 */
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    private String companyName;
    /** 集团详细地址 */
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    private String companyAddress;
    /** 集团负责人 */
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    private String companyLeader;
    /** 法人手机 */
    @Phone(groups = CompanyInfo.createCompanyInfo.class)
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    private String companyLeaderMobile;
    /** 集团电话 */
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    @TelePhone(groups = CompanyInfo.createCompanyInfo.class)
    private String companyTelephone;
    /** 公司邮箱 */
    @NotBlank(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.class)
    @Email(message = "{EMAIL_FORMAT_ERROR}" ,groups = CompanyInfo.class)
    private String companyEmail;
    /** 备付金比例 */
    @NotNull(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    @Min(value = 0 ,message ="{CASH_RATE_ERROR}" )
    @Max(value = 1,message = "{CASH_RATE_ERROR}")
    private Float cashRate;
    /** 通用积分兑换比例（N：1通用积分） */
    @NotNull(message = "{PARAMETER_ERROR}" ,groups = CompanyInfo.createCompanyInfo.class)
    @Min(value = 0,message = "{POINT_RATE_ERROR}")
    private Float pointRate;
}
