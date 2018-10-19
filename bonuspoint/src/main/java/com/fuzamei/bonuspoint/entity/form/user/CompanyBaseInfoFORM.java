package com.fuzamei.bonuspoint.entity.form.user;

import com.fuzamei.bonuspoint.validation.IDCard;
import com.fuzamei.bonuspoint.validation.Phone;
import com.fuzamei.bonuspoint.validation.TelePhone;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @program: bonus-point-cloud
 * @description: 公司基本信息
 * @author: WangJie
 * @create: 2018-07-09 16:32
 **/
@Data
public class CompanyBaseInfoFORM {
    /** 集团名称 */
    @NotBlank(message = "PARAMETER_ERROR" )
    private String companyName;
    /** 集团负责人 */
    @NotBlank(message = "PARAMETER_ERROR" )
    private String companyLeader;
    /** 法人身份证号*/
    @IDCard
    @NotBlank(message = "PARAMETER_ERROR" )
    private String companyLeaderIdCard;
    /** 法人手机 */
    @Phone
    @NotBlank(message = "PARAMETER_ERROR" )
    private String companyLeaderMobile;
    /** 集团电话 */
    @TelePhone
    private String companyTelephone;
    /** 公司邮箱 */
    @Email(message = "EMAIL_FORMAT_ERROR" )
    @NotBlank(message = "PARAMETER_ERROR" )
    private String companyEmail;
    /** 公司logo */
    private String logoUrl;

}
