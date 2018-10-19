package com.fuzamei.bonuspoint.entity.dto.common;

import com.fuzamei.bonuspoint.validation.CaptchaType;
import com.fuzamei.bonuspoint.validation.Phone;
import com.fuzamei.bonuspoint.validation.group.Captcha;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @program: bonus-point-cloud
 * @description: 前端发来的消息模板，用于手机和邮箱验证码
 * @author: WangJie
 * @create: 2018-05-02 13:47
 **/
@Log
@Data
@NoArgsConstructor
public class Message {
    /** 用户id*/
    private Long uid;
    /** 用户手机号*/
    @NotBlank(message = "{MOBILE_BLANK}",groups = {Captcha.MobileCaptcha.class})
    @Phone(message = "{MOBILE_FORMAT_ERROR}",groups = {Captcha.MobileCaptcha.class})
    private String mobile;

    /** 用户email*/
    @NotBlank(message = "{EMAIL_BLANK}",groups = {Captcha.EmailCaptcha.class})
    @Email(message = "{EMAIL_FORMAT_ERROR}",groups = {Captcha.EmailCaptcha.class})
    private String email;
    /** 验证码用途 类型*/
    @CaptchaType(message = "{CAPTCHA_TYPE_WRONG}",groups = {Captcha.class})
    @NotNull(message = "{CAPTCHA_TYPE_WRONG}",groups = {Captcha.class})
    private Integer type;
/*    @NotBlank(message = "{CAPTCHA_BLANK")
    @com.fuzamei.bonuspoint.validation.Captcha(message = "{CAPTCHA_WRONG}")*/
    /** 验证码 */
    private String code;
    /** 验证码token */
    private String codeToken;

}
