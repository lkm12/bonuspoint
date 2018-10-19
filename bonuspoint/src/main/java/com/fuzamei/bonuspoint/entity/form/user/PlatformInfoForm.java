package com.fuzamei.bonuspoint.entity.form.user;

import com.fuzamei.bonuspoint.validation.*;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @program: bonus-point-cloud
 * @description: 平台信息表单
 * @author: WangJie
 * @create: 2018-09-10 14:14
 **/
@Data
public class PlatformInfoForm {

    /**管理员用户名*/
    @NotBlank(message = "{PARAMETER_ERROR}" )
    private String username;
    /** 管理员手机号 */
    @NotBlank(message = "{PARAMETER_ERROR}" )
    @Phone()
    private String mobile;
    /**管理员用户密码*/
    @NotBlank(message = "{PARAMETER_ERROR}" )
    @Password()
    private String password;
    /**交易密码*/
    @NotBlank(message = "{PARAMETER_ERROR}" )
    @Payword
    private String payword;
    /** 平台名称 */
    @NotBlank(message = "PARAMETER_ERROR" )
    private String platformName;
    /** 平台的注册公司名称*/
    @NotBlank(message = "PARAMETER_ERROR")
    private String companyName;
    /** 平台负责人*/
    @NotBlank(message = "PARAMETER_ERROR" )
    private String platformLeader;
    /** 平台负责人手机号*/
    @Phone
    @NotBlank(message = "PARAMETER_ERROR" )
    private String platformLeaderMobile;
    @IDCard
    @NotBlank(message = "PARAMETER_ERROR" )
    private String platformLeaderIdCard;
    /** 平台电话*/
    @TelePhone
    private String platformTelephone;
    /** 平台邮箱*/
    @Email(message = "EMAIL_FORMAT_ERROR" )
    private String platformEmail;
    /**平台税号 */
    @NotBlank(message = "PARAMETER_ERROR")
    private String taxNumber;
    /** 备付金比例 */
    @NotNull(message = "{PARAMETER_ERROR}" )
    @Min(value = 0 ,message ="{CASH_RATE_ERROR}" )
    @Max(value = 1,message = "{CASH_RATE_ERROR}")
    private Float cashRate;
    /** 通用积分兑换比例（N：1通用积分） */
    @NotNull(message = "{PARAMETER_ERROR}" )
    @Min(value = 0,message = "{POINT_RATE_ERROR}")
    private Float pointRate;


}
