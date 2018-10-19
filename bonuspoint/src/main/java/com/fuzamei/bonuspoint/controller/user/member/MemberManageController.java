package com.fuzamei.bonuspoint.controller.user.member;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.constant.RegexConstant;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.ContactCreateDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.user.ContactService;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.bonuspoint.validation.group.User;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @program: bonus-point-cloud
 * @description: 普通用户（会员）controller
 * @author: WangJie
 * @create: 2018-04-27 15:49
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/member")
public class MemberManageController {

    @Value("${md5.salt}")
    private String salt;

    private final ContactService contactService;

    private final UserService userService;

    private final AccountService accountService;
    @Value("${reg.phone}")
    private String mobileReg;

    @Autowired
    public MemberManageController(ContactService contactService, UserService userService,AccountService accountService) {
        this.contactService = contactService;
        this.userService = userService;
        this.accountService = accountService;
    }

    /**
     * 新增联系人
     *
     * @param
     * @return mobile:手机
     * publickey:对方公钥
     * remark:备注
     */
    @PostMapping("/contacts/create")
    public ResponseVO createContact(@RequestAttribute("token") Token token, @RequestBody @Validated(User.CreateContact.class)
            ContactCreateDTO contactCreateDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }

        contactCreateDTO.setUid(token.getUid());
        return contactService.createContact(contactCreateDTO);

    }

    /**
     * 更新联系人
     *
     * mobile:手机
     * publickey：公钥
     * remark：备注
     * @param
     * @return
     */
    @PostMapping("/contacts/update")
    public ResponseVO updateContact(@RequestAttribute("token") Token token, @RequestBody @Validated(User.CreateContact.class)
            ContactCreateDTO contactCreateDTO,BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }

        contactCreateDTO.setUid(token.getUid());

        return contactService.updateContact(contactCreateDTO);

    }

    /**
     * 删除联系人
     * lkm
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/contacts/delete")
    public ResponseVO deleteContact(@RequestAttribute("token") Token token, @RequestBody @Validated(User.DeleteContact.class)
            ContactCreateDTO contactCreateDTO,BindingResult bindingResult ) {

        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }

        Long contacts_id = contactCreateDTO.getContactsId();
        return contactService.deleteContact(contacts_id);

    }


    /**
     * 用户注册
     * username:用户名
     * password:密码
     * code:验证码
     *
     * inviteCode:邀请码
     * mark:平台uuid
     *
     * @param
     * @return
     */
    @PostMapping("/member-register")
    public ResponseVO memberRegister(@RequestBody String jsonData) {

        UserDTO userDTO;

        userDTO = JSON.parseObject(jsonData,UserDTO.class);



        if (StringUtil.isBlank(userDTO.getUsername()) || StringUtil.isBlank(userDTO.
                getPassword()) || StringUtil.isBlank(userDTO.getCode()) || StringUtil.isBlank(userDTO.getMark()) || StringUtil.isBlank(userDTO.getInviteCode()))
                 {
            log.info("参数不能为空");
            return new ResponseVO(CommonResponseEnum.PARAMETER_BLANK);

        }
        //校验用户名
        Boolean n = userDTO.getUsername().matches(mobileReg);
        if(!n){
            return new ResponseVO(UserResponseEnum.USERNAME_FORMAT_FAIL);
        }
        //校验密码
        if (!userDTO.getPassword().matches(RegexConstant.PWD_REGEX)) {
            return new ResponseVO(UserResponseEnum.PASSWORD_FORMAT_FAIL);
        }

        String passwordHash = null;
        try {
               passwordHash = MD5HashUtil.md5SaltEncrypt(userDTO.getPassword(), salt);
        } catch (Exception e) {
                e.printStackTrace();
            }
            userDTO.setPasswordHash(passwordHash);

            log.info("参数校验正常");

            return userService.addUser(userDTO);



    }

    /**
     * 修改用户昵称
     *
     * @param accountDTO {
     *                nickname    用户昵称
     *                }
     * @author wangjie
     */
    @LogAnnotation(note = "修改用户昵称")
    //@PutMapping("/edit-nickname")
    @RequestMapping(value = "/edit-nickname",method = {RequestMethod.PUT,RequestMethod.POST})
    public ResponseVO updateNickname(@RequestAttribute("token") Token token, @RequestBody AccountDTO accountDTO) {
        accountDTO.setId(token.getUid());
        accountDTO.setUpdatedAt(TimeUtil.timestamp());
        return accountService.updateNickname(accountDTO);
    }


}
