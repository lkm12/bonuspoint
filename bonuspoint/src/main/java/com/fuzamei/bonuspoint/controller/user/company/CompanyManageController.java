package com.fuzamei.bonuspoint.controller.user.company;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.form.user.CompanyBaseInfoFORM;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.user.company.CompanyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @program: bonus-point-cloud
 * @description: 集团商户
 * @author: WangJie
 * @create: 2018-04-27 15:51
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/company")
public class CompanyManageController {

    private final CompanyInfoService companyInfoService;


    @Autowired
    public CompanyManageController(CompanyInfoService companyInfoService) {
        this.companyInfoService = companyInfoService;

    }


    /**
     * 获取集团信息类接口
     *
     * @param uid uid 请求参数
     * @return 请求响应
     */
    @GetMapping("/get/{uid}")
    public ResponseVO getCompanyInfo(@PathVariable("uid") Long uid) {
        log.info("测试开始");
        // 参数校验

        // 请求服务
        log.info("参数校验正常");
        return companyInfoService.getCompanyInfo(uid);
    }



    /**
     * 商户修改店铺基本信息
     * @param token
     * @param companyBaseInfoFORM{
     *                             companyName
     *                             companyLeader
     *                             companLeaderIdCard
     *                             companyLeaderMobile
     *                             companyEmail
     *                             logoUrl
     *                         }
     * @return
     */
    @LogAnnotation(note = "商户修改店铺基本信息")
    @PutMapping("/update-company-base-info")
    public ResponseVO updateCompanyBaseInfo(@RequestAttribute("token") Token token,
                                            @RequestBody CompanyBaseInfoFORM companyBaseInfoFORM,
                                            BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        BeanUtils.copyProperties(companyBaseInfoFORM,companyInfoDTO);
        Long companyId = companyInfoService.getCompanyIdByUid(token.getUid());
        companyInfoDTO.setId(companyId);
        return companyInfoService.updateCompanyBaseInfo(companyInfoDTO);
    }

    /**
     * 获取集团信息列表类接口
     *
     * @param
     * @return 请求响应
     */
 /*   @GetMapping("/list")
    public ResponseVO listCompanyInfo() {
        log.info("测试开始");
        // 参数校验

        // 请求服务
        log.info("参数校验正常");
        return companyInfoService.listCompanyInfo();
    }
*/

}
