package com.fuzamei.bonuspoint.controller.asset;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.asset.CompanyCashRecordDTO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.CompanyCashRecordVO;
import com.fuzamei.bonuspoint.service.asset.CompanyCashService;
import com.fuzamei.common.bean.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/17
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/asset/company")
public class CompanyCashQueryController {

    private final CompanyCashService companyCashService;

    @Autowired
    public CompanyCashQueryController(CompanyCashService companyCashService) {
        this.companyCashService = companyCashService;
    }

    /**
     * @param token                令牌 (required = true)
     * @param companyCashRecordDTO 请求参数
     *                             {
     *                             startTime: 查询起始时间 (required = false)
     *                             endTime: 查询截止时间 (required = false)
     *                             currentPage: 当前页 (required = false)
     *                             pageSize: 每页记录数 (required = false)
     *                             }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团备付金充值记录接口
     */
    @PostMapping("/recharge/get")
    public ResponseVO<PageBean<CompanyCashRecordVO>> getRechargeCashRecord(@RequestAttribute("token") Token token,
                                                                           @RequestBody CompanyCashRecordDTO companyCashRecordDTO) {
        log.info("获取单个集团备付金充值记录");
        companyCashRecordDTO.setUid(token.getUid());
        // 请求服务
        log.info("参数校验正常");
        return companyCashService.getRechargeCashRecord(companyCashRecordDTO);
    }

    /**
     * @param token                令牌 (required = true)
     * @param companyCashRecordDTO 请求参数
     *                             {
     *                             startTime: 查询起始时间 (required = false)
     *                             endTime: 查询截止时间 (required = false)
     *                             currentPage: 当前页 (required = false)
     *                             pageSize: 每页记录数 (required = false)
     *                             }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团提现记录接口
     */
    @PostMapping("/withdraw/get")
    public ResponseVO<PageBean<CompanyCashRecordVO>> getWithdrawCashRecord(@RequestAttribute("token") Token token,
                                                                           @RequestBody CompanyCashRecordDTO companyCashRecordDTO) {
        log.info("集团查询,测试开始");
        companyCashRecordDTO.setUid(token.getUid());
        // 请求服务
        log.info("参数校验正常");
        return companyCashService.getWithdrawCashRecord(companyCashRecordDTO);
    }

    /**
     * @param token 令牌 (required = true)
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团资产信息接口
     */
    @GetMapping("/company-cash/get")
    public ResponseVO<CompanyCashRecordVO> getCompanyCashRecord(@RequestAttribute("token") Token token) {
        log.info("获取单个集团资产信息");
        // 请求服务
        log.info("参数校验正常");
        return companyCashService.getCompanyCashRecord(token.getUid());
    }

    /**
     * @param token 令牌 (required = true)
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取备付金信息接口
     */
    @GetMapping("/provisions/get")
    public ResponseVO<CompanyInfoPO> getProvisionsCashRecord(@RequestAttribute("token") Token token) {
        log.info("获取备付金信息");
        // 请求服务
        log.info("参数校验正常");
        return companyCashService.getProvisionsCashRecord(token.getUid());
    }

    /**
     * 获取集团备付金流水
     * lkm
     *
     *
     * {
     *  currentPage: 当前页 (required = false)
     *  pageSize: 每页记录数 (required = false)
     *  startTime:起始时间(required = false)
     *  endTime:结束时间(required = false)
     * }
     * @param token
     * @param companyCashRecordDTO
     * @return
     */
    @PostMapping("/company-cash-flow")
    public ResponseVO getCompanyCashFlow(@RequestAttribute("token") Token token,@RequestBody CompanyCashRecordDTO companyCashRecordDTO){

        companyCashRecordDTO.setUid(token.getUid());

        return companyCashService.getCompanyCashFlow(companyCashRecordDTO);
    }
}
