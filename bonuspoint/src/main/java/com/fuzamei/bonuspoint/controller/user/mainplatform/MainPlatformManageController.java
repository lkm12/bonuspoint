package com.fuzamei.bonuspoint.controller.user.mainplatform;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.PlatformInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.data.advertisement.AdvertisementDTO;
import com.fuzamei.bonuspoint.entity.form.user.PlatformInfoForm;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.data.advertisement.AdvertisementService;
import com.fuzamei.bonuspoint.service.user.mainplatform.MainPlatformService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
import com.fuzamei.bonuspoint.util.RandomUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: bonus-point-cloud
 * @description: 总平台写接口
 * @author: WangJie
 * @create: 2018-09-10 14:08
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/main-platform")
public class MainPlatformManageController {

    @Value("${md5.salt}")
    private String salt;

    private final AccountService accountService;

    private final MainPlatformService mainPlatformService;

    private final AdvertisementService advertisementService;

    @Autowired
    public MainPlatformManageController(AccountService accountService, MainPlatformService mainPlatformService, AdvertisementService advertisementService) {
        this.accountService = accountService;
        this.mainPlatformService = mainPlatformService;
        this.advertisementService = advertisementService;
    }

    /**
     *
     * @param token
     * @param platformInfoForm {
     *                         username
     *                         mobile
     *                         password
     *                         payword
     *                         platformName
     *                         platformAddress
     *                         platformLeader
     *                         platformLeaderIdCard
     *                         platformLeaderMobile
     *                         platformTelephone
     *                         platformEmail
     *                         companyName
     *                         taxNumber
     *                         cashRate
     *                         pointRate
     *
     * }
     * @return
     */
    @LogAnnotation(note = "添加平台")
    @PostMapping("/add-platform")
    public ResponseVO createPlatform(@RequestAttribute("token")Token token,
                                     @RequestBody @Validated PlatformInfoForm platformInfoForm,
                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        //检查管理员用户名是否被注册
        AccountPO accountPO =new AccountPO();
        accountPO.setPId(token.getUid());
        accountPO.setUsername(platformInfoForm.getUsername());
        int count = accountService.countUser(accountPO);
        if (count > 0) {
            return new ResponseVO(UserResponseEnum.EXIST_NAME);
        }
        //检查管理员手机号是否被注册
        accountPO = new AccountPO();
        accountPO.setRole(Roles.PLATFORM);
        accountPO.setMobile(platformInfoForm.getMobile());
        count = accountService.countUser(accountPO);
        if (count > 0) {
            return new ResponseVO(SafeResponseEnum.MOBILE_EXIST);
        }
        //构造平台管理员用户
        AccountDTO accountDTO = new AccountDTO();
        BeanUtils.copyProperties(platformInfoForm,accountDTO);
        accountDTO.setPasswordHash(MD5HashUtil.md5SaltEncrypt(platformInfoForm.getPassword(),salt));
        accountDTO.setPaywordHash(MD5HashUtil.md5SaltEncrypt(platformInfoForm.getPayword(),salt));
        String privateKey = KeyUtil.privateKey(platformInfoForm.getPassword(), RandomUtil.getRandomString(32));
        String publicKey = KeyUtil.publicKey(privateKey);
        accountDTO.setPublicKey(publicKey);
        accountDTO.setPrivateKey(privateKey);
        accountDTO.setPId(token.getUid());
        accountDTO.setMobile(platformInfoForm.getMobile());
        accountDTO.setRole(Roles.PLATFORM);
        //构造平台信息
        PlatformInfoDTO platformInfoDTO = new PlatformInfoDTO();
        BeanUtils.copyProperties(platformInfoDTO,platformInfoForm);
        return mainPlatformService.addPlatform(accountDTO,platformInfoDTO);

    }

    @LogAnnotation(note = "添加广告")
    @PostMapping("/add-advertisement")
    public ResponseVO addAdvertisement(@RequestBody @Validated AdvertisementDTO advertisementDTO,BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        return advertisementService.addAdvertisement(advertisementDTO);
    }

    @LogAnnotation(note = "删除广告")
    @DeleteMapping("/delete-advertisement/{id}")
    public ResponseVO deleteAdvertisement(@PathVariable("id") Integer id){

        return advertisementService.deleteAdvertisement(id);
    }

    @LogAnnotation(note="更新广告")
    @PostMapping("/update-advertisement")
    public ResponseVO updateAdvertisement(@RequestBody @Validated AdvertisementDTO advertisementDTO,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        if (advertisementDTO.getId()==null){
            return new ResponseVO<>(CommonResponseEnum.FAILURE, "PARAMETER_BLANK");
        }
        return advertisementService.updateAdvertisement(advertisementDTO);
    }

}
