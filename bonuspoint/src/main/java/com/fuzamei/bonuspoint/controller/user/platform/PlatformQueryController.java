package com.fuzamei.bonuspoint.controller.user.platform;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.vo.user.CompanyInfoVO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.vo.user.PlatformBaseInfoVO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.user.company.CompanyInfoService;
import com.fuzamei.bonuspoint.service.user.platform.PlatformService;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.service.user.mainplatform.MainPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: bonus-point-cloud
 * @description: 平台controller
 * @author: WangJie
 * @create: 2018-04-27 15:52
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/platform")
public class PlatformQueryController {

    private final UserService userService;
    private final MainPlatformService mainStationService;
    private final CompanyInfoService companyInfoService;
    private final PlatformService platformService;
    @Autowired
    public PlatformQueryController(UserService userService, MainPlatformService mainStationService, CompanyInfoService companyInfoService, PlatformService platformService) {
        this.userService = userService;
        this.mainStationService = mainStationService;
        this.companyInfoService = companyInfoService;
        this.platformService = platformService;
    }

    /**
     * 平台模糊查询下属集团（商户）信息
     *
     * @param token
     * @param pageDTO {
     *                fuzzyMatch        模糊查询内容
     *                page              目标页
     *                pageSize          页大小
     *                }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note="平台模糊查询下属集团信息")
    @PostMapping("/list-companyInfo")
    public ResponseVO getCompanyInfoList(@RequestAttribute("token") Token token, @RequestBody PageDTO pageDTO) {
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        BeanUtils.copyProperties(pageDTO, companyInfoDTO);
        companyInfoDTO.setPId(token.getUid());
        return companyInfoService.getCompanyInfoList(companyInfoDTO);
    }

    /**
     * 平台查询用户信息(平台)
     *
     * @param token
     * @param  {
     *                均为非必须
     *                fuzzyMatch        模糊查询内容
     *                page              目标页
     *                pageSize          页大小
     *                }
     * @return
     * @author lkm
     */
    @LogAnnotation(note="平台查询用户信息")
    @PostMapping("/list-userInfo")
    public ResponseVO getUserInfoListFromPlatform(@RequestAttribute("token") Token token, @RequestBody PagePointDTO pagePointDTO) {
        pagePointDTO.setId(token.getUid());
        return userService.getUserInfoListFromPlatform(pagePointDTO);
    }


    /**
     * 平台查询集团备付金比例    支持分页
     *
     * @param token
     * @param  companyInfoVO{
     *
     *                companyName       公司名，支持模糊查询
     *                page              目标页
     *                pageSize          页大小    page和pageSize都为-1时不分页
     *                orderType         排序类型
     *         }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note="平台查询集团备付金比例")
    @PostMapping("/list-companyCashRateInfo")
    public ResponseVO getCompanyCashRateList(@RequestAttribute("token") Token token, @RequestBody CompanyInfoVO companyInfoVO) {

        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        BeanUtils.copyProperties(companyInfoVO,companyInfoDTO);
        companyInfoDTO.setPId(token.getUid());
        return companyInfoService.getCompanyCashRateList(companyInfoDTO);
    }

    /**
     * 平台查询集团积分兑换比例    支持分页
     *
     * @param token
     * @param  companyInfoVO{
     *
     *                companyName       公司名，支持模糊查询
     *                page              目标页
     *                pageSize          页大小    page和pageSize都为-1时不分页
     *                orderType         排序类型
     *         }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "平台查询集团积分兑换比例")
    @PostMapping("/list-companyPointRateInfo")
    public ResponseVO getCompanyPointRateList(@RequestAttribute("token") Token token, @RequestBody CompanyInfoVO companyInfoVO) {
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        BeanUtils.copyProperties(companyInfoVO,companyInfoDTO);
        companyInfoDTO.setPId(token.getUid());
        return companyInfoService.getCompanyPointRateList(companyInfoDTO);
    }





    /**
     * 查询邀请码信息(平台)
     * 非必需
     * page
     * pageSize
     *
     * @param jsonData
     * @return
     */
    @PostMapping("/inviteInfo")
    public ResponseVO inviteInfo(@RequestAttribute("token") Token token,@RequestBody String jsonData){

        PagePointDTO pagePointDTO;

        try {
            pagePointDTO = JSON.parseObject(jsonData, PagePointDTO.class);
        } catch (Exception e) {
            log.info("参数错误");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }
        pagePointDTO.setId(token.getUid());
        return userService.findInvite(pagePointDTO);
    }



    /**
     * 平台查看自己平台基本信息
     * @param token
     * @author wangjie
     * @return
     */
    @LogAnnotation(note = "平台查看自己平台基本信息")
    @GetMapping("/platform-base-info")
    public ResponseVO<PlatformBaseInfoVO> getCompanyBaseInfo(@RequestAttribute("token") Token token){
        log.info("平台查看自己平台基本信息开始");
        return platformService.getSelfPlatformBaseInfo(token.getUid());
    }

}
