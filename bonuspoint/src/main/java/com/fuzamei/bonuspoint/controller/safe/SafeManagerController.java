package com.fuzamei.bonuspoint.controller.safe;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.constant.CodeType;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.account.SecrecyDTO;
import com.fuzamei.bonuspoint.entity.dto.common.Message;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.form.account.SecrecyFORM;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.captcha.CaptchaService;
import com.fuzamei.bonuspoint.service.safe.SafeService;
import com.fuzamei.bonuspoint.util.*;
import com.fuzamei.bonuspoint.validation.group.UserSecrecy;
import com.google.i18n.phonenumbers.NumberParseException;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-18 17:30
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/safe")
public class SafeManagerController {

    @Value("${md5.salt}")
    private String salt;


    private final SafeService safeService;

    private final AccountService accountService;

    private final CaptchaService captchaService;

    @Autowired
    public SafeManagerController(SafeService safeService, AccountService accountService, CaptchaService captchaService) {
        this.safeService = safeService;
        this.accountService = accountService;
        this.captchaService = captchaService;
    }

    /**
     * 修改登录密码
     *
     * @param secrecyFORM{ oldPassword        原密码
     *                     password           新密码
     *                     passwordRepeat     重复密码
     *                     }
     * @return
     */
    // @PutMapping("/password-edit")
    @LogAnnotation(note = "修改登录密码")
    @RequestMapping(value = "/password-edit", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseVO updatePassword(@RequestAttribute("token") Token token,
                                     @RequestBody @Validated(UserSecrecy.UpdatePassword.class) SecrecyFORM secrecyFORM,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        AccountPO accountPO = accountService.getUserById(token.getUid());
        if (secrecyFORM.getPassword().equals(secrecyFORM.getPasswordRepeat())) {
            SecrecyDTO secrecyDTO = new SecrecyDTO();
            try {
                secrecyDTO.setPasswordHash(MD5HashUtil.md5SaltEncrypt(secrecyFORM.getPassword(), salt));
                if (!accountPO.getPasswordHash().equals(MD5HashUtil.md5SaltEncrypt(secrecyFORM.getOldPassword(), salt))) {
                    return new ResponseVO(UserResponseEnum.WRONG_PASSWORD);
                }
            } catch (Exception e) {
                return new ResponseVO(SafeResponseEnum.MD5_ERROR);
            }
            secrecyDTO.setId(token.getUid());
            return safeService.updatePassword(secrecyDTO);
        } else {
            return new ResponseVO(SafeResponseEnum.TWO_DIFFERENT_INPUT);
        }
    }

    /**
     * 重设登录密码
     *
     * @param secrecyFORM{ mobile:    手机号
     *                     code  :    验证码
     *                     password:  重设的密码
     *                     passwordRepeat: 重复密码
     *                     role :角色
     * @return
     */
    // @PutMapping("/password-reset")
    @LogAnnotation(note = "重设登录密码")
    @RequestMapping(value = "/password-reset", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO resetPassword(@RequestBody @Validated(UserSecrecy.ResetPassword.class) SecrecyFORM secrecyFORM,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setMobile(secrecyFORM.getMobile());
        accountDTO.setRole(secrecyFORM.getRole());
        AccountPO accountPO = accountService.getAccount(accountDTO);


        /**
         * 验证码处理
         */
        Message message = new Message();
        message.setUid(accountPO.getId());
        message.setMobile(secrecyFORM.getMobile());
        message.setCode(secrecyFORM.getCode());
        message.setType(CodeType.RESET_PASSWORD);
        boolean isRightCode = captchaService.checkCaptcha(message);
        if (!isRightCode) {
            return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }
        SecrecyDTO secrecyDTO = new SecrecyDTO();
        try {
            secrecyDTO.setPasswordHash(MD5HashUtil.md5SaltEncrypt(secrecyFORM.getPassword(), salt));
        } catch (Exception e) {
            return new ResponseVO(SafeResponseEnum.MD5_ERROR);
        }

        secrecyDTO.setId(accountPO.getId());

        return safeService.updatePassword(secrecyDTO);

    }

    /**
     * 修改支付密码
     *
     * @param secrecyFORM{ country         手机归属国家
     *                     mobile          手机号码
     *                     code            验证码
     *                     payword         新交易密码
     *                     paywordRepeat   确认密码
     *                     }
     * @return
     */
    //  @PutMapping("/payword-set")
    @LogAnnotation(note = "修改支付密码")
    @RequestMapping(value = "/payword-set", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO updatePayword(@RequestBody @Validated(UserSecrecy.UpdatePayWord.class) SecrecyFORM secrecyFORM,
                                    @RequestAttribute("token") Token token,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        //检查支付密码格式
        boolean check = NumberCheck.checkPayword(secrecyFORM.getPayword());
        if (!check) {
            return new ResponseVO(CommonResponseEnum.PAYWORD_FORMAT_WRONG);
        }
        AccountPO accountPO = accountService.getUserById(token.getUid());
        if (accountPO.getMobile() == null) {
            return new ResponseVO(SafeResponseEnum.MOBILE_UNBOUND);
        }

        if (!secrecyFORM.getMobile().equals(accountPO.getMobile())) {
            return new ResponseVO(SafeResponseEnum.MOBILE_WRONG);
        }

        /**
         * 验证码处理
         */
        Message message = new Message();
        message.setUid(token.getUid());
        message.setMobile(secrecyFORM.getMobile());
        message.setCode(secrecyFORM.getCode());
        message.setType(CodeType.SET_PAYWORD);
        boolean isRightCode = captchaService.checkCaptcha(message);
        if (!isRightCode) {
            return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }

        if (secrecyFORM.getPayword().equals(secrecyFORM.getPaywordRepeat())) {
            SecrecyDTO secrecyDTO = new SecrecyDTO();
            BeanUtils.copyProperties(secrecyFORM, secrecyDTO);
            try {
                secrecyDTO.setPaywordHash(MD5HashUtil.md5SaltEncrypt(secrecyFORM.getPayword(), salt));
            } catch (Exception e) {
                return new ResponseVO(SafeResponseEnum.MD5_ERROR);
            }

            secrecyDTO.setId(token.getUid());
            return safeService.updatePayWord(secrecyDTO);
        } else {
            return new ResponseVO(SafeResponseEnum.TWO_DIFFERENT_INPUT);
        }
    }


    /**
     * 绑定手机号
     *
     * @param secrecyFORM{ country
     *                     mobile
     *                     code
     *                     }
     * @return
     */
    //  @PutMapping("/mobile-set")
    @LogAnnotation(note = "绑定手机号")
    @RequestMapping(value = "/mobile-set", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO setMobile(@RequestBody @Validated(UserSecrecy.UserMobile.class) SecrecyFORM secrecyFORM,
                                @RequestAttribute("token") Token token,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        boolean hasMobile = safeService.hasMobile(token.getUid());
        if (hasMobile) {
            return new ResponseVO(SafeResponseEnum.MOBILE_BOUNDED);
        }
        //默认CN
        if (secrecyFORM.getCountry() == null) {
            secrecyFORM.setCountry("CN");
        }
        Message message = new Message();
        message.setUid(token.getUid());
        message.setMobile(secrecyFORM.getMobile());
        message.setCode(secrecyFORM.getCode());
        message.setType(CodeType.BOUND_MOBILE);
        boolean isRightCode = captchaService.checkCaptcha(message);
        if (!isRightCode) {
            new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }
        SecrecyDTO secrecyDTO = new SecrecyDTO();
        BeanUtils.copyProperties(secrecyFORM, secrecyDTO);
        secrecyDTO.setId(token.getUid());
        return saveMobile(secrecyDTO);
    }

    /**
     * 修改手机号第一步 验证原手机号
     *
     * @param secrecyFORM{
     *                     country
     *                     mobile
     *                     code
     *                   }
     * @return
     */
    @LogAnnotation(note = "修改手机号第一步：验证原手机号")
    // @PutMapping("/mobile-edit/verification-original-mobile-number")
    @RequestMapping(value = "/mobile-edit/verification-original-mobile-number", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO updateMobileStepOne(@RequestBody @Validated(UserSecrecy.UserMobile.class) SecrecyFORM secrecyFORM,
                                          @RequestAttribute("token") Token token,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        if (secrecyFORM.getCountry()==null){
            secrecyFORM.setCountry("CN");
        }
        Message message = new Message();
        message.setUid(token.getUid());
        message.setMobile(secrecyFORM.getMobile());
        message.setCode(secrecyFORM.getCode());
        message.setType(CodeType.EDIT_MOBILE_STEP_ONE);
        boolean isRightCode = captchaService.checkCaptcha(message);
        if (!isRightCode) {
            return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }


        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 修改手机号第二步 修改为新手机号
     *
     * @param secrecyFORM
     * @return
     */
    // @PutMapping("/mobile-edit/update-mobile-number")
    @LogAnnotation(note = "修改手机号第二步：修改为新手机号")
    @RequestMapping(value = "/mobile-edit/update-mobile-number", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO updateMobileStepTwo(@RequestBody @Validated(UserSecrecy.UpdateMobile.class) SecrecyFORM secrecyFORM,
                                          @RequestAttribute("token") Token token,
                                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        Message message = new Message();
        BeanUtils.copyProperties(secrecyFORM, message);
        message.setUid(token.getUid());
        message.setType(CodeType.EDIT_MOBILE_STEP_TWO);
        boolean isRightCode = captchaService.checkCaptcha(message);
        if (!isRightCode) {
            return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }

        SecrecyDTO secrecyDTO = new SecrecyDTO();
        BeanUtils.copyProperties(secrecyFORM, secrecyDTO);
        secrecyDTO.setId(token.getUid());
        return saveMobile(secrecyDTO);
    }


    private ResponseVO saveMobile(SecrecyDTO secrecyDTO) {

        //如果同一父级下存在要更新的手机号，则不允许更新
        List<String> fields = new ArrayList<>(1);
        fields.add("p_id");
        AccountPO accountPO = accountService.getUserById(secrecyDTO.getId());
        boolean isMobileAvailable = accountService.checkMobileAvailable(accountPO.getPId(), secrecyDTO.getMobile());
        if (!isMobileAvailable) {
            return new ResponseVO(SafeResponseEnum.MOBILE_EXIST);
        }
        boolean isValid;
        try {
            isValid = MobileUtil.isRightMobile(secrecyDTO.getCountry(), secrecyDTO.getMobile());
        } catch (NumberParseException e) {
            log.info("NumberParseException was thrown: " + e.toString());
            return new ResponseVO(SafeResponseEnum.MOBILE_FORMAT_ERROR);
        }
        if (isValid) {
            return safeService.updateMobile(secrecyDTO);
        } else {
            return new ResponseVO(SafeResponseEnum.MOBILE_FORMAT_ERROR);
        }
    }

    /**
     * 绑定邮箱
     *
     * @param secrecyFORM{
     *                   code
     *                   email
     *                   }
     * @return
     */
    // @PutMapping("/email-set")
    @LogAnnotation(note = "绑定邮箱")
    @RequestMapping(value = "/email-set", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO setEmail(@RequestBody @Validated(UserSecrecy.UserEmail.class) SecrecyFORM secrecyFORM,
                               @RequestAttribute("token") Token token,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        boolean hasEmail = safeService.hasEmail(token.getUid());
        if (hasEmail) {
            return new ResponseVO(SafeResponseEnum.EMAIL_BOUNDED);
        }

        boolean isEmail = RegxUtils.isEmail(secrecyFORM.getEmail());
        if (isEmail) {

            // 验证码处理
            Message message = new Message();
            message.setUid(token.getUid());
            message.setEmail(secrecyFORM.getEmail());
            message.setCode(secrecyFORM.getCode());
            message.setType(CodeType.BOUND_EMAIL);
            boolean isRightCode = captchaService.checkCaptcha(message);
            if (!isRightCode) {
                return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
            }
            SecrecyDTO secrecyDTO = new SecrecyDTO();
            BeanUtils.copyProperties(secrecyFORM, secrecyDTO);
            secrecyDTO.setId(token.getUid());
            return safeService.updateEmail(secrecyDTO);
        } else {
            return new ResponseVO(SafeResponseEnum.EMAIL_FORMAT_ERROR);
        }
    }


    /**
     * 修改邮箱第一步 验证原邮箱
     * @param secrecyFORM {
     *                    code
     *                    email
     *
     * }
     * @return
     */
    // @PutMapping("/email-edit/verification-original-email")
    @RequestMapping(value = "/email-edit/verification-original-email", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO updateEmailStepOne(@RequestBody @Validated(UserSecrecy.UserEmail.class) SecrecyFORM secrecyFORM,
                                         @RequestAttribute("token") Token token,
                                         BindingResult bindingResult) {
        log.info("updateEmailStepOne验证原邮箱开始" + secrecyFORM);
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        Message message = new Message();
        message.setUid(token.getUid());
        message.setEmail(secrecyFORM.getEmail());
        message.setCode(secrecyFORM.getCode());
        message.setType(CodeType.EDIT_EMAIL_STEP_ONE);
        boolean isRightCode = captchaService.checkCaptcha(message);
        if (!isRightCode) {
            return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 修改邮箱第二步 修改为新邮箱
     *
     * @param secrecyFORM{
     *                   code
     *                   email
     *                   codeToken
     *                   }
     * @return
     */
    // @PutMapping("/email-edit/update-email")
    @LogAnnotation(note = "修改邮箱第二步：修改为新邮箱")
    @RequestMapping(value = "/email-edit/update-email", method = {RequestMethod.POST, RequestMethod.PUT})

    public ResponseVO updateEmailStepTwo(@RequestBody @Validated(UserSecrecy.UpdateEmail.class) SecrecyFORM secrecyFORM,
                                         @RequestAttribute("token") Token token,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        Message message = new Message();
        BeanUtils.copyProperties(secrecyFORM, message);
        message.setUid(token.getUid());
        message.setType(CodeType.EDIT_EMAIL_STEP_TWO);
        boolean isRightCode = captchaService.checkCaptcha(message);
        if (!isRightCode) {
            return new ResponseVO(SafeResponseEnum.CAPTCHA_WRONG);
        }
        boolean isEmail = RegxUtils.isEmail(secrecyFORM.getEmail());
        if (isEmail) {
            SecrecyDTO secrecyDTO = new SecrecyDTO();
            secrecyDTO.setId(token.getUid());
            secrecyDTO.setEmail(secrecyFORM.getEmail());
            return safeService.updateEmail(secrecyDTO);
        } else {
            return new ResponseVO(SafeResponseEnum.EMAIL_FORMAT_ERROR);
        }
    }
}
