package com.fuzamei.bonuspoint.entity.form.user;

import com.fuzamei.bonuspoint.validation.IDCard;
import com.fuzamei.bonuspoint.validation.Phone;
import com.fuzamei.bonuspoint.validation.TelePhone;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @program: bonus-point-cloud
 * @description: 平台基本信息
 * @author: WangJie
 * @create: 2018-07-13 14:50
 **/
@Data
public class PlatformBaseInfoFORM {

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
    private String logoUrl;
    /**积分兑换人民币 比率*/
    @NotNull(message = "PARAMETER_ERROR")
    private Float pointRate;
}
