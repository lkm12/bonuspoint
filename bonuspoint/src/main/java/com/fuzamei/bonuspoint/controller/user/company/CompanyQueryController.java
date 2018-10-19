package com.fuzamei.bonuspoint.controller.user.company;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.QueryUserDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.vo.account.AccountVO;
import com.fuzamei.bonuspoint.entity.vo.user.CompanyBaseInfoVO;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.point.MemberPointService;
import com.fuzamei.bonuspoint.service.user.company.CompanyInfoService;
import com.fuzamei.bonuspoint.service.user.company.CompanyMemberService;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.util.RegxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: bonus-point-cloud
 * @description: 集团（商户）
 * @author: WangJie
 * @create: 2018-04-27 15:50
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/company")
public class CompanyQueryController {

    private final CompanyMemberService companyMemberService;

    private final AccountService accountService;

    private final CompanyInfoService companyInfoService;

    private final MemberPointService memberPointService;

    @Autowired
    public CompanyQueryController(CompanyMemberService companyMemberService, CompanyInfoService companyInfoService, AccountService accountService, MemberPointService memberPointService) {
        this.companyMemberService = companyMemberService;
        this.accountService = accountService;
        this.companyInfoService = companyInfoService;
        this.memberPointService = memberPointService;
    }

    /**
     * 集团查询其某个会员的公钥
     *
     * @param token
     * @param accountDTO {
     *                mobile        手机号
     *                }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "集团查询某个会员公钥")
    @PostMapping("/info-block")
    public ResponseVO<AccountVO> getMemberBlockInfo(@RequestAttribute("token") Token token , @RequestBody AccountDTO accountDTO) {

        boolean isChinaMobile = RegxUtils.isChinaPhoneLegal(accountDTO.getMobile());
        if (!isChinaMobile) {
            return new ResponseVO<>(SafeResponseEnum.MOBILE_FORMAT_ERROR);
        }
        accountDTO.setPId(token.getPId());
        accountDTO.setRole(Roles.MEMBER);
        AccountPO accountPO = accountService.getAccount(accountDTO);
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(accountPO,accountVO);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,accountVO);
    }

    /**
     * 集团（商户）获取旗下会员活跃数据
     *
     * @param token
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "集团获取旗下会员活跃数据")
    @GetMapping("/member-activity")
    public ResponseVO getMemberActivity(@RequestAttribute("token") Token token) {
        Long companyId = companyInfoService.getCompanyIdByUid(token.getUid());
        return companyMemberService.getMemberActivity(companyId);

    }


    /**
     * 集团查看会员信息
     *
     * @param token
     * @param queryUserDTO {
     *                mobile
     *                page
     *                pageSize
     *                }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "集团查看会员信息")
    @PostMapping("/list-member-point-info")
    public ResponseVO getMemberPointInfoList(@RequestAttribute("token") Token token, @RequestBody QueryUserDTO queryUserDTO) {
        queryUserDTO.setUid(token.getUid());
        Long companyId = companyInfoService.getCompanyIdByUid(token.getUid());
        queryUserDTO.setCompanyId(companyId);
        return memberPointService.memberPointInfo(queryUserDTO);
    }

    /**
     * 商户查看自己店铺基本信息
     * @param token
     * @author wangjie
     * @return
     */
    @LogAnnotation(note = "商户查看自己店铺基本信息")
    @GetMapping("/company-base-info")
    public ResponseVO<CompanyBaseInfoVO> getCompanyBaseInfo(@RequestAttribute("token") Token token){
        return companyInfoService.getCompanyBaseInfo(token.getUid());
    }

}
