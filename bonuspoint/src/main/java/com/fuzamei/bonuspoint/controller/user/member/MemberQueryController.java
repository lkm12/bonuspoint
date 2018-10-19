package com.fuzamei.bonuspoint.controller.user.member;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.user.ContactService;
import com.fuzamei.bonuspoint.service.user.UserLoginService;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @program: bonus-point-cloud
 * @description: 普通用户（会员）的查询controller
 * @author: WangJie
 * @create: 2018-04-27 15:47
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/member")
public class MemberQueryController {

    @Value("${md5.salt}")
    private String salt;

    private final ContactService contactService;

    private final UserService userService;

    @Value(("${token.client.app}"))
    private String appClient;
    @Value("${token.client.browser}")
    private String browserClient;

    @Value("${token.outTime.browser}")
    private Long browserOutTime;


    @Value("${token.outTime.app}")
    private Long appOutTime;

    @Value("${key.platform.publicKey}")
    private String platformPublicKey;
    @Value("${key.platform.privateKey}")
    private String platformPrivate;

    private final UserLoginService userLoginService;
    @Autowired
    public MemberQueryController(UserLoginService userLoginService,ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;

        this.userLoginService = userLoginService;
    }

    /**
     * 获取联系人列表
     * 皆为非必需
     * username:用户名
     * page（非必需）
     * pageSize（非必需）
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/contacts/list")
    public ResponseVO listContact(@RequestAttribute("token") Token token, @RequestBody PagePointDTO pagePointDTO ) {


        pagePointDTO.setId(token.getUid());
        return contactService.findContactsList(pagePointDTO);

    }


    /**
     * 获取邀请信息
     *
     * @return
     */
    @PostMapping("/invite-info")
    public ResponseVO findInviteInfo(@RequestAttribute("token") Token token) {


        return userService.findInviteInfo(token.getUid());


    }

    /**
     * 获取账户二维码
     *
     * @return
     */
    @PostMapping("/qrcode")
    public ResponseVO findQrcode(@RequestAttribute("token") Token token) {


        return userService.findQrcodeById(token.getUid());


    }


    /**
     * APP登录
     * username
     * password
     * mark:平台uuid
     * @param
     * @return
     */
    @PostMapping("/memberLoginAPP")
    public ResponseVO memberLoginAPP(@RequestBody String jsonData) {

        UserDTO userDTO;
        try {
            userDTO = JSON.parseObject(jsonData,UserDTO.class);
        }catch (Exception e){
            log.info("参数错误");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }
        if(StringUtil.isBlank(userDTO.getUsername()) || StringUtil.isBlank(userDTO.getPassword()) || StringUtil.isBlank(userDTO.getMark())){
            log.info("参数不能为空");
            return new ResponseVO(CommonResponseEnum.PARAMETER_BLANK);
        }

        try {
            userDTO.setPasswordHash(MD5HashUtil.md5SaltEncrypt(userDTO.getPassword(), salt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDTO.setTokenType(appClient);
        userDTO.setTokenTime(appOutTime);
        return userLoginService.memberLogin(userDTO);


    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @PostMapping("/infoAPP")
    public ResponseVO findUserInfoAPP(@RequestAttribute("token") Token token) {
        return userService.findUserInfoByIdAPP(token.getUid());
    }





}

