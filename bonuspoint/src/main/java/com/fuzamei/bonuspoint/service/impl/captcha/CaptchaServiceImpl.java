package com.fuzamei.bonuspoint.service.impl.captcha;



import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fuzamei.bonuspoint.constant.CodeType;
import com.fuzamei.bonuspoint.constant.CommonConst;
import com.fuzamei.bonuspoint.dao.common.mapper.AccountMapper;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.common.Message;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.captcha.CaptchaService;
import com.fuzamei.bonuspoint.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: bonus-point-cloud
 * @description: 手机与邮箱验证码
 * @author: WangJie
 * @create: 2018-05-02 16:11
 **/
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Value("${spring.redis.keyOutTime}")
    private Long keyOutTime;

    @Value("${spring.redis.keySignOutTime}")
    private Long keySignOutTime;

    @Value("${email_title}")
    private String title;

    @Value("${email_content}")
    private String content;
    @Value("${method.sms}")
    private boolean smsSwitch;

    private final RedisTemplate redisTemplate;

    private final SubmailSMSUtil submailSMSUtil;

    private final AccountMapper accountMapper;
    @Autowired
    public CaptchaServiceImpl(RedisTemplate redisTemplate,SubmailSMSUtil submailSMSUtil,AccountMapper accountMapper) {
        this.redisTemplate = redisTemplate;
        this.submailSMSUtil = submailSMSUtil;
        this.accountMapper =accountMapper;
    }


    /**
     * 用于注册,重置密码时保存验证码
     *
     * @param message
     * @return
     */
    @Override
    public ResponseVO saveCaptcha(Message message) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String codeSign = valueOperations.get(CodeType.REGISETER + "_" + message.getMobile() + "_sign");
        if (codeSign == null) {
            message.setCode(CaptchaUtil.getCode());
            if (!smsSwitch){
                message.setCode("123456");
            }
            valueOperations.set(message.getType() + "_" + message.getMobile() + "_sign", message.getCode(), keySignOutTime, TimeUnit.SECONDS);
            valueOperations.set(message.getType() + "_" + message.getMobile(), message.getCode(), keyOutTime, TimeUnit.SECONDS);

            if (smsSwitch) {
                /*SendSmsResponse sendSmsResponse;
                try {
                    sendSmsResponse = SendSmsUtils.sendSms(message.getMobile(), message.getCode());
                } catch (ClientException e) {
                    log.info("验证码发送错误：{}", e.getErrMsg());
                    return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                }
                if (StringUtil.isBlank(sendSmsResponse.getCode()) || !CommonConst.OK.equals(sendSmsResponse.getCode())) {
                    log.info("验证码发送失败：{}", sendSmsResponse.getMessage());
                    return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                }*/
              boolean result = submailSMSUtil.sendCaptcha(message.getMobile(),message.getCode());
              if (!result){
                  log.info("手机号{},验证码类型：{} ,验证码发送失败",message.getMobile(),message.getType());
                  return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
              }

            }
            return new ResponseVO(CommonResponseEnum.SUCCESS);
        } else {
            return new ResponseVO(CommonResponseEnum.CAPTCHA_SENT);
        }
    }

    /**
     * 其他场景保存验证码
     *
     * @param message
     * @param accountDTO
     * @return
     */
    @Override
    public ResponseVO saveCaptcha(Message message, AccountDTO accountDTO) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String codeSign = valueOperations.get(message.getType() + "_" + message.getMobile() + "_" + accountDTO.getId() + "_sign");
        String codeToken = null;
        if (codeSign == null) {
            message.setCode(CaptchaUtil.getCode());
            if (!smsSwitch){
                message.setCode("123456");
            }
            switch (message.getType()) {
                case CodeType.BOUND_EMAIL:{
                    valueOperations.set(message.getType() + "_" + message.getEmail() + "_" + accountDTO.getId() + "_sign", message.getCode(), keySignOutTime, TimeUnit.SECONDS);
                    valueOperations.set(message.getType() + "_" + message.getEmail() + "_" + accountDTO.getId(), message.getCode(), keyOutTime, TimeUnit.SECONDS);
                    content=content.replaceFirst("code", message.getCode());
                    if (smsSwitch) {
                        SendEmailUtil.sendEmail(message.getEmail(), title, content);
                    }
                    break;
                }
                case CodeType.EDIT_EMAIL_STEP_ONE:
                case CodeType.EDIT_EMAIL_STEP_TWO: {
                    AccountPO accountPO = accountMapper.selectByPrimaryKey(accountDTO.getId());
                    if (accountPO.getEmail()==null){
                        return new ResponseVO(SafeResponseEnum.EMAIL_UNBOUND);
                    }
                    if (message.getType() != CodeType.EDIT_EMAIL_STEP_TWO && !accountPO.getEmail().equals(message.getEmail())) {
                        return new ResponseVO(SafeResponseEnum.EMAIL_WRONG);
                    }

                    if (message.getType() == CodeType.EDIT_EMAIL_STEP_ONE) {
                        codeToken = UUID.randomUUID().toString();
                        valueOperations.set(message.getType() + "_codeToken_" + accountDTO.getId() + "_token", codeToken, keyOutTime, TimeUnit.SECONDS);
                    }

                    valueOperations.set(message.getType() + "_" + message.getEmail() + "_" + accountDTO.getId() + "_sign", message.getCode(), keySignOutTime, TimeUnit.SECONDS);
                    valueOperations.set(message.getType() + "_" + message.getEmail() + "_" + accountDTO.getId(), message.getCode(), keyOutTime, TimeUnit.SECONDS);
                    content=content.replaceFirst("code", message.getCode());
                    if (smsSwitch) {
                        SendEmailUtil.sendEmail(message.getEmail(), title, content);
                    }
                    break;
                }
                case CodeType.BOUND_MOBILE:{
                    valueOperations.set(message.getType() + "_" + message.getMobile() + "_" + accountDTO.getId() + "_sign", message.getCode(), keySignOutTime, TimeUnit.SECONDS);
                    valueOperations.set(message.getType() + "_" + message.getMobile() + "_" + accountDTO.getId(), message.getCode(), keyOutTime, TimeUnit.SECONDS);
                    /**
                     *  发送短信
                     *
                     */
                    if (smsSwitch) {
                    /*    SendSmsResponse sendSmsResponse;
                        try {
                            sendSmsResponse = SendSmsUtils.sendSms(message.getMobile(), message.getCode());
                        } catch (ClientException e) {
                            log.info("验证码发送错误：{}", e.getErrMsg());
                            return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                        }
                        if (StringUtil.isBlank(sendSmsResponse.getCode()) || !CommonConst.OK.equals(sendSmsResponse.getCode())) {
                            log.info("验证码发送失败：{}", sendSmsResponse.getMessage());
                            return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                        }*/
                        boolean result = submailSMSUtil.sendCaptcha(message.getMobile(),message.getCode());
                        if (!result){
                            log.info("手机号{},验证码类型：{} ,验证码发送失败",message.getMobile(),message.getType());
                            return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                        }

                    }
                    break;
                }
                default: {
                    AccountPO accountPO = accountMapper.selectByPrimaryKey(accountDTO.getId());
                    if (accountPO.getMobile()==null){
                        return new ResponseVO(SafeResponseEnum.MOBILE_UNBOUND);
                    }
                    if (message.getType() != CodeType.EDIT_MOBILE_STEP_TWO && !accountPO.getMobile().equals(message.getMobile())) {
                        return new ResponseVO(SafeResponseEnum.MOBILE_WRONG);
                    }

                    if (message.getType() == CodeType.EDIT_MOBILE_STEP_ONE) {
                        codeToken = UUID.randomUUID().toString();
                        valueOperations.set(message.getType()   + "_codeToken_" + accountDTO.getId() + "_token", codeToken, keyOutTime, TimeUnit.SECONDS);
                    }
                    valueOperations.set(message.getType() + "_" + message.getMobile() + "_" + accountDTO.getId() + "_sign", message.getCode(), keySignOutTime, TimeUnit.SECONDS);
                    valueOperations.set(message.getType() + "_" + message.getMobile() + "_" + accountDTO.getId(), message.getCode(), keyOutTime, TimeUnit.SECONDS);
                    /**
                     *  发送短信
                     *
                     */
                    if (smsSwitch) {
                     /*   SendSmsResponse sendSmsResponse;
                        try {
                            sendSmsResponse = SendSmsUtils.sendSms(message.getMobile(), message.getCode());
                        } catch (ClientException e) {
                            log.info("验证码发送错误：{}", e.getErrMsg());
                            return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                        }
                        if (StringUtil.isBlank(sendSmsResponse.getCode()) || !CommonConst.OK.equals(sendSmsResponse.getCode())) {
                            log.info("验证码发送失败：{}", sendSmsResponse.getMessage());
                            return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                        }*/
                       boolean result = submailSMSUtil.sendCaptcha(message.getMobile(),message.getCode());
                        if (!result){
                            log.info("手机号{},验证码类型：{} ,验证码发送失败",message.getMobile(),message.getType());
                            return new ResponseVO(CommonResponseEnum.SEND_CAPTCHA_ERROR);
                        }

                    }

                }

            }
            if (codeToken!=null){
                Map<String , String> map = new HashMap<>(1);
                map.put("codeToken",codeToken);
                return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
            }
            return new ResponseVO<>(CommonResponseEnum.SUCCESS );
        } else {
            return new ResponseVO(CommonResponseEnum.CAPTCHA_SENT);
        }
    }

    /**
     * 验证验证码的正确性
     *
     * @param message
     * @return
     */
    @Override
    public boolean checkCaptcha(Message message) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String code;
        switch (message.getType()) {
            case CodeType.BOUND_EMAIL:
            case CodeType.EDIT_EMAIL_STEP_ONE:
            case CodeType.EDIT_EMAIL_STEP_TWO: {
                if (message.getType() == CodeType.EDIT_EMAIL_STEP_TWO) {
                    String codeToken = valueOperations.get(CodeType.EDIT_EMAIL_STEP_ONE + "_codeToken_" + message.getUid() + "_token");
                    boolean isCodeTokenRight = message.getCodeToken() != null && message.getCodeToken().equals(codeToken);
                    if (!isCodeTokenRight) {
                        return false;
                    }
                }
                code = valueOperations.get(message.getType() + "_" + message.getEmail() + "_" + message.getUid());
                if (message.getCode().equals(code)) {
                    return true;
                } else {
                    return false;
                }

            }
            case CodeType.RESET_PASSWORD:
            case CodeType.REGISETER: {
                code = valueOperations.get(message.getType() + "_" + message.getMobile());

                if (message.getCode().equals(code)) {
                    return true;
                } else {
                    return false;
                }
            }
            default: {
                if (message.getType() == CodeType.EDIT_MOBILE_STEP_TWO) {
                    String codeToken = valueOperations.get(CodeType.EDIT_MOBILE_STEP_ONE + "_codeToken_" + message.getUid() + "_token");
                    boolean isCodeTokenRight = message.getCodeToken() != null && message.getCodeToken().equals(codeToken);
                    if (!isCodeTokenRight) {
                        return false;
                    }
                }

                code = valueOperations.get(message.getType() + "_" + message.getMobile() + "_" + message.getUid());
                if (message.getCode().equals(code)) {
                    return true;
                } else {
                    return false;
                }
            }

        }
    }
}
