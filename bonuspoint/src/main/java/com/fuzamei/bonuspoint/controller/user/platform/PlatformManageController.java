package com.fuzamei.bonuspoint.controller.user.platform;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.PlatformInfoDTO;
import com.fuzamei.bonuspoint.entity.form.user.CompanyInfoFORM;
import com.fuzamei.bonuspoint.entity.form.user.CompanyRateFORM;
import com.fuzamei.bonuspoint.entity.form.user.PlatformBaseInfoFORM;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.user.platform.PlatformService;
import com.fuzamei.bonuspoint.validation.group.CompanyInfo;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.user.company.CompanyInfoService;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.util.*;
import com.fzm.blockchain.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: bonus-point-cloud
 * @description: 平台controller
 * @author: WangJie
 * @create: 2018-04-27 15:51
 **/
@RestController
@RequestMapping("/bonus-point/platform")
@Slf4j
public class PlatformManageController {


    @Value("${md5.salt}")
    private String salt;
    private final UserService userService;

    private final AccountService accountService;
    private final CompanyInfoService companyInfoService;
    private final PlatformService platformService;

    @Autowired
    public PlatformManageController(UserService userService, AccountService accountService, CompanyInfoService companyInfoService, PlatformService platformService) {
        this.userService = userService;
        this.accountService = accountService;
        this.companyInfoService = companyInfoService;
        this.platformService = platformService;
    }


    /**
     * 平台添加集团
     *
     * @param token
     * @param companyInfoFORM {
     *                        username                    用户名
     *                        password                    登录密码
     *                        companyName                 集团名
     *                        companyLeader               集团法人
     *                        companyEmail                集团邮箱
     *                        companyLeaderMobile         集团负责人手机号
     *                        mobile                      管理员手机号
     *                        companyTelephone            集团电话
     *                        companyAddress              集团地址
     *                        cashRate                    备付金比例
     *                        pointRate                   积分兑换比例
     *                        payword                     支付密码
     *                        <p>
     *                        }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "平台添加集团")
    @PostMapping("/add-companyInfo")
    public ResponseVO addConpanyInfo(@RequestAttribute("token") Token token,
                                     @RequestBody @Validated(CompanyInfo.createCompanyInfo.class) CompanyInfoFORM companyInfoFORM,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        //检查用户名
        AccountPO accountPO = new AccountPO();
        accountPO.setPId(token.getUid());
        accountPO.setUsername(companyInfoFORM.getUsername());
        int count = accountService.countUser(accountPO);
        if (count > 0) {
            return new ResponseVO(UserResponseEnum.EXIST_NAME);
        }
        AccountDTO accountDTO = new AccountDTO();
        BeanUtils.copyProperties(companyInfoFORM, accountDTO);

        try {
            accountDTO.setPasswordHash(MD5HashUtil.md5SaltEncrypt(companyInfoFORM.getPassword(), salt));
            accountDTO.setPaywordHash(MD5HashUtil.md5SaltEncrypt(companyInfoFORM.getPayword(), salt));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("MD5出错");
            throw new RuntimeException(SafeResponseEnum.MD5_ERROR.getMessage());
        }
        accountDTO.setRole(Roles.COMPANY);
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        BeanUtils.copyProperties(companyInfoFORM, companyInfoDTO);
        String privateKey = KeyUtil.privateKey(companyInfoFORM.getPassword(), RandomUtil.getRandomString(32));
        String publicKey = KeyUtil.publicKey(privateKey);
        accountDTO.setPublicKey(publicKey);
        accountDTO.setPrivateKey(privateKey);
        accountDTO.setPId(token.getUid());
        accountDTO.setMobile(companyInfoFORM.getMobile());
        return companyInfoService.createCompany(accountDTO, companyInfoDTO);
    }

    /**
     * 平台
     * 修改集团积分兑换比例
     *
     * @param token
     * @param companyRateFORM {
     *                        id                 集团信息id
     *                        pointRate          集团积分兑换比例
     *                        payword            交易密码
     *                        }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "平台修改集团积分兑换比例")
    @PutMapping("/edit-pointrate")
    public ResponseVO updateCompanyCashRate(@RequestAttribute("token") Token token,
                                            @RequestBody @Validated(CompanyInfo.updatePointRate.class) CompanyRateFORM companyRateFORM,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        AccountPO accountPO = accountService.getUserById(token.getUid());


        boolean isPaywordCorrect = accountPO.getPaywordHash().equals(MD5HashUtil.md5SaltEncrypt(companyRateFORM.getPayword(), salt));
        if (!isPaywordCorrect) {
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }

        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        BeanUtils.copyProperties(companyRateFORM, companyInfoDTO);
        companyInfoDTO.setPId(token.getUid());
        return companyInfoService.setCompanyPointRate(companyInfoDTO);
    }

    /**
     * 平台
     * 修改集团备付金比例
     *
     * @param token
     * @param companyRateFORM {
     *                        id          集团信息id
     *                        cashRate           集团备付金比例
     *                        payword            交易密码
     *                        }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "平台修改集团备付金比例")
    @PutMapping("/edit-cashrate")
    public ResponseVO setCompanyCashRate(@RequestAttribute("token") Token token,
                                         @RequestBody @Validated(CompanyInfo.updateCashRate.class) CompanyRateFORM companyRateFORM,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        AccountPO accountPO = accountService.getUserById(token.getUid());
        boolean isPaywordCorrect = accountPO.getPaywordHash().equals(MD5HashUtil.md5SaltEncrypt(companyRateFORM.getPayword(), salt));
        if (!isPaywordCorrect) {
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        BeanUtils.copyProperties(companyRateFORM, companyInfoDTO);
        companyInfoDTO.setPId(token.getUid());

        return companyInfoService.setCompanyCashRate(companyInfoDTO);
    }

    /**
     * 平台修改平台基础信息
     *
     * @param token
     * @param platformBaseInfoFORM{ platformName
     *                              taxNumber
     *                              platformLeader
     *                              platformLeaderMobile
     *                              platformLeaderIdCard
     *                              platformEmail
     *                              <p>
     *                              }
     * @param bindingResult
     * @return
     */
    @LogAnnotation(note = "平台修改平台基础信息")
    @PostMapping("/update-base-info")
    public ResponseVO updateSelfPlatformBaseInfo(@RequestAttribute("token") Token token,
                                                 @RequestBody @Validated PlatformBaseInfoFORM platformBaseInfoFORM,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        if (platformBaseInfoFORM.getPointRate() <= 0) {
            return new ResponseVO<>(CommonResponseEnum.PARAMETER_ERROR);
        }
        PlatformInfoDTO platformInfoDTO = new PlatformInfoDTO();
        BeanUtils.copyProperties(platformBaseInfoFORM, platformInfoDTO);
        platformInfoDTO.setUid(token.getUid());
        return platformService.updatePlatformBaseInfo(platformInfoDTO);
    }

    @GetMapping("/delete-company/{companyId}")
    @LogAnnotation(note = "平台删除集团")
    public ResponseVO deleteCompany(@PathVariable("companyId") Long companyId, @RequestAttribute("token") Token token) {
        return companyInfoService.deleteCompany(token.getUid(), companyId);
    }

    @GetMapping("/update/platform-pointRate/{pointRate}")
    @LogAnnotation(note = "修改平台积分人民币兑换比率")
    public ResponseVO updatePlatformPointRate(@PathVariable Float pointRate, @RequestAttribute("token") Token token) {
        return platformService.updatePlatformPointRate(token.getUid(), pointRate);
    }

}
