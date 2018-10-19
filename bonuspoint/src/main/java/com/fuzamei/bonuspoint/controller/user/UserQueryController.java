package com.fuzamei.bonuspoint.controller.user;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.vo.account.AccountVO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.data.advertisement.AdvertisementService;
import com.fuzamei.bonuspoint.service.user.UserLoginService;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: bonus-point-cloud
 * @description: 用户通用接口controller
 * @author: WangJie
 * @create: 2018-04-28 15:41
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point")
public class UserQueryController {

    @Value("${token.client.browser}")
    private String browserClient;
    @Value("${token.outTime.browser}")
    private Long browserOutTime;
    @Value("${md5.salt}")
    private String salt;

    private final UserService userService;
    private final AccountService accountService;
    private final UserLoginService userLoginService;
    private final AdvertisementService advertisementService;

    @Autowired
    public UserQueryController(UserService userService, AccountService accountService, UserLoginService userLoginService, AdvertisementService advertisementService) {
        this.userService = userService;
        this.accountService = accountService;
        this.userLoginService = userLoginService;
        this.advertisementService = advertisementService;
    }




    /**
     * 转账时通过手机号查找账号
     * @param token
     * @param accountDTO{
     *                     mobile
     *                 }
     * @return
     */
    @LogAnnotation(note = "转账时通过手机号查找账号")
    @PostMapping("/user/get-account-info-by-mobile")
    public ResponseVO<List<AccountVO>> getUserAccountInfoByMobile(@RequestAttribute("token") Token token, @RequestBody AccountDTO accountDTO){
        accountDTO.setPId(token.getPId());
        accountDTO.setRole(Roles.MEMBER);
        return accountService.getAccountInfo(accountDTO);

    }

    /**
     * 转账时通过公钥查找账号
     * @param token
     * @param accountDTO{
     *                     publicKey
     *                 }
     * @return
     */
    @LogAnnotation(note = "转账时通过公钥查找账号")
    @PostMapping("/user/get-account-info-by-publicKey")
    public ResponseVO<AccountVO> getUserAccountInfoByPublicKey(@RequestAttribute("token") Token token, @RequestBody AccountDTO accountDTO){
        return accountService.getAccountInfoByPublicKey(accountDTO.getPublicKey());

    }

    /**
     * 检查手机号是否已被使用
     * @param accountVO{
     *                     mobile
     *                     pId
     *                 }
     * @return
     */
    @LogAnnotation(note ="检查手机号是否已被使用")
    @PostMapping("/user/is-mobile-exist")
    public ResponseVO isMobileExist(@RequestBody AccountVO accountVO ){
       boolean result =  accountService.checkMobileAvailable(accountVO.getPId(),accountVO.getMobile());
        Map<String ,Object> map = new HashMap<>(1);
        map.put("isExist",String.valueOf(result));
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,map);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @PostMapping("/info")
    public ResponseVO findUserInfo(@RequestAttribute("token") Token token) {
        return userService.findUserInfoById(token.getUid());
    }

    /**
     * 集团与平台网页端登录
     * username
     * password
     * mark
     * role
     * @param jsonData
     * @return
     */
    @PostMapping("/browser-login")
    public ResponseVO browserLogin(@RequestBody String jsonData) {

        UserDTO userDTO;
        try {
            userDTO = JSON.parseObject(jsonData, UserDTO.class);
        } catch (Exception e) {
            log.info("参数错误");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }


        if (StringUtil.isBlank(userDTO.getUsername()) || StringUtil.isBlank(userDTO.
                getPassword()) || StringUtil.isBlank(userDTO.getMark())|| userDTO.getRole() == null) {
            log.info("参数不能为空");
            return new ResponseVO(CommonResponseEnum.PARAMETER_BLANK);
        }


        try {
            userDTO.setPasswordHash(MD5HashUtil.md5SaltEncrypt(userDTO.getPassword(), salt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        userDTO.setTokenType(browserClient);
        userDTO.setTokenTime(browserOutTime);
        return userLoginService.browserLogin(userDTO);


    }

    /**
     * 查询用户持有对应集团的可用会员积分总数
     * @param token token
     * @param gid gid
     * @return
     */
    @GetMapping("/query/point-sum")
    public ResponseVO getCompanyPoint(@RequestAttribute("token") Token token,@RequestParam Long gid){
        log.info("查询用户持有接团可用积分");
        return userService.getCompanyPoint(token.getUid(),gid);
    }

    @LogAnnotation(note = "获取广告列表")
    @GetMapping("/list-advertisement")
    public ResponseVO listAdvertisement(){
        return advertisementService.listAdvertisement();
    }
}