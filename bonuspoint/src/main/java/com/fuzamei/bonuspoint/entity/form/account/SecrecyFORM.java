package com.fuzamei.bonuspoint.entity.form.account;

import com.fuzamei.bonuspoint.validation.Captcha;
import com.fuzamei.bonuspoint.validation.Phone;
import com.fuzamei.bonuspoint.validation.group.UserSecrecy;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @program: bonus-point-cloud
 * @description: 用户表登录注册相关字段
 * @author: WangJie
 * @create: 2018-06-25 15:16
 **/
@Data
public class SecrecyFORM {

    /**
     * 用户名
     */
    private String username;
    /**
     * 角色
     */
    @NotNull(message = "PARAMETER_ERROR",groups = {UserSecrecy.ResetPassword.class})
    private Integer role;
    /**
     * 原密码
     */
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.UpdatePassword.class})
    private String oldPassword;

    /**
     * 密码 ，新密码
     */
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.UpdatePassword.class,UserSecrecy.ResetPassword.class})
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.UpdatePassword.class,UserSecrecy.ResetPassword.class})
    private String passwordRepeat;

    /**
     * 支付密码
     */
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.UpdatePayWord.class})
    private String payword;

    /**
     * 确认支付密码
     */
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.UpdatePayWord.class})
    private String paywordRepeat;

    /**
     * 手机归属国家
     */
    private String country;

    /**
     * 手机号
     */
    @Phone
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.ResetPassword.class,
            UserSecrecy.UpdatePayWord.class,
            UserSecrecy.UserMobile.class,
            UserSecrecy.UpdateMobile.class})
    private String mobile;

    /**
     * 验证码
     */
    @Captcha
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.ResetPassword.class,
            UserSecrecy.UpdatePayWord.class,
            UserSecrecy.UserMobile.class,
            UserSecrecy.UpdateMobile.class,
            UserSecrecy.UserEmail.class,
            UserSecrecy.UpdateEmail.class})
    private String code;

    /**
     * 验证码token，用于修改手机和邮箱的第二步
     */
    @NotBlank(message = "PARAMETER_ERROR",groups = {UserSecrecy.UpdateMobile.class,UserSecrecy.UpdateEmail.class})
    private String codeToken;

    /**
     * 邮箱
     */
    @Email(message = "EMAIL_FORMAT_ERROR" ,groups = {UserSecrecy.UserEmail.class,UserSecrecy.UpdateEmail.class})
    private String email;


}
