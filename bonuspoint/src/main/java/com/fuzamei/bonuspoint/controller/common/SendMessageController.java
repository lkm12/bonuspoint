package com.fuzamei.bonuspoint.controller.common;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.constant.CodeType;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.validation.group.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fuzamei.bonuspoint.entity.dto.common.Message;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.captcha.CaptchaService;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: bonus-point-cloud
 * @description: 向手机、邮箱发送验证码，验证码校验
 * @author: WangJie
 * @create: 2018-05-02 10:43
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/message")
public class SendMessageController {


    private final CaptchaService captchaService;


    @Autowired
    public SendMessageController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @LogAnnotation(note = "发送验证码")
    @PostMapping("/send-SMS-verification-code")
    public ResponseVO sendMobileOrEmailMessage(@RequestAttribute("token") Token token,
                                               @RequestBody @Validated(Captcha.MobileCaptcha.class) Message message,
                                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(token.getUid());
        return captchaService.saveCaptcha(message, accountDTO);

    }
    @LogAnnotation(note = "发送邮箱验证码")
    @PostMapping("/send-email-verification-code")
    public ResponseVO sendEmailMessage(@RequestAttribute("token") Token token,
                                       @RequestBody @Validated(Captcha.EmailCaptcha.class) Message message,
                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(token.getUid());
        return captchaService.saveCaptcha(message, accountDTO);

    }

    @LogAnnotation(note="注册发送验证码")
    @PostMapping({"/register/send-SMS-verification-code"})
    public ResponseVO sendMobileCodeForRegister(@RequestBody @Validated(Captcha.MobileCaptcha.class) Message message, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        if (message.getType()!=CodeType.REGISETER){
            return new ResponseVO(SafeResponseEnum.CAPTCHA_TYPE_WRONG);
        }
        return captchaService.saveCaptcha(message);


    }
    @LogAnnotation(note = "重置密码发送验证码")
    @PostMapping({"/reset/send-SMS-verification-code"})
    public ResponseVO sendMobileCodeForReset(@RequestBody @Validated(Captcha.MobileCaptcha.class) Message message, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        if (message.getType()!=CodeType.RESET_PASSWORD){
            return new ResponseVO(SafeResponseEnum.CAPTCHA_TYPE_WRONG);
        }
        return captchaService.saveCaptcha(message);


    }
}
