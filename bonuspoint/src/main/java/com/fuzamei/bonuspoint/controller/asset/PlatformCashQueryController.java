package com.fuzamei.bonuspoint.controller.asset;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.asset.PlatformCashRecordDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.PlatformCashRecordVO;
import com.fuzamei.bonuspoint.service.asset.PlatformCashService;
import com.fuzamei.common.bean.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/asset/platform")
public class PlatformCashQueryController {

    private final PlatformCashService platformCashService;

    @Autowired
    public PlatformCashQueryController(PlatformCashService platformCashService) {
        this.platformCashService = platformCashService;
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              mobile: 用户手机号 (required = false)
     *                              currentPage: 当前页 (required = false)
     *                              pageSize: 每页记录数 (required = false)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户资产信息接口
     */
    @PostMapping("/member-cash/list")
    public ResponseVO<PageBean<PlatformCashRecordVO>> listMemberCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取平台用户资产信息");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.listMemberCashRecord(platformCashRecordDTO);
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              companyName: 商户名称 (required = false)
     *                              currentPage: 当前页 (required = false)
     *                              pageSize: 每页记录数 (required = false)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团资产信息接口
     */
    @PostMapping("/company-cash/list")
    public ResponseVO<PageBean<PlatformCashRecordVO>> listCompanyCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取集团资产信息");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.listCompanyCashRecord(platformCashRecordDTO);
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              companyName: 商户名称 (required = false)
     *                              currentPage: 当前页 (required = false)
     *                              pageSize: 每页记录数 (required = false)
     *                              startTime: 开始时间 (required = false)
     *                              endTime: 结束时间 (required = false)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团充值记录接口
     */
    @PostMapping("/recharge/list")
    public ResponseVO<PageBean<PlatformCashRecordVO>> getRechargeCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取集团充值记录");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.getRechargeCashRecord(platformCashRecordDTO);
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              companyName: 商户名称 (required = false)
     *                              currentPage: 当前页 (required = false)
     *                              pageSize: 每页记录数 (required = false)
     *                              startTime: 开始时间 (required = false)
     *                              endTime: 结束时间 (required = false)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团提款记录接口
     */
    @PostMapping("/withdraw/list")
    public ResponseVO<PageBean<PlatformCashRecordVO>> getWithdrawCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取集团提款记录");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.getWithdrawCashRecord(platformCashRecordDTO);
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              payword: 交易密码 (required = true)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取备付金充值列表接口
     */
    @PostMapping("/recharge/get")
    public ResponseVO listRechargeCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取备付金充值列表");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.listRechargeCashRecord(platformCashRecordDTO);
    }

    /**
     * @param token 令牌 (required = true)
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取平台资产信息接口
     */
    @GetMapping("/platform-cash/get")
    public ResponseVO<PlatformCashRecordVO> getPlatformCashRecord(@RequestAttribute("token") Token token) {
        log.info("获取平台资产信息");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.getPlatformCashRecord(token.getUid());
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              payword: 交易密码 (required = true)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团资产信息列表接口
     */
    @PostMapping("/company/list")
    public ResponseVO listCompany(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取集团资产信息列表");

        // 请求服务
        log.info("参数校验正常");
        return platformCashService.listCompany(platformCashRecordDTO);
    }

    /**

     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              payword: 交易密码 (required = true)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取备付金预警通知列表接口
     */
    @PostMapping("/provisions-notice/list")
    public ResponseVO<PageBean<PlatformCashRecordVO>> listProvisionsNotice(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取备付金预警通知列表");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.listProvisionsNotice(platformCashRecordDTO);
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              payword: 交易密码 (required = true)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员积分列表接口
     */
    @PostMapping("/member-point/list")
    public ResponseVO listMemberPointCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("平台查询,测试开始");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.listMemberPointCashRecord(platformCashRecordDTO);
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              payword: 交易密码 (required = true)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表接口
     */
    @PostMapping("/member-point-detail/get")
    public ResponseVO getMemberPointCashRecordDetail(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取会员用户集团积分列表");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.getMemberPointCashRecordDetail(platformCashRecordDTO);
    }

    /**
     * @param platformCashRecordDTO 请求参数
     *                              {
     *                              payword: 交易密码 (required = true)
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团比例信息列表接口
     */
    @PostMapping("/company-rate/list")
    public ResponseVO<PageBean<PlatformCashRecordVO>> listCompanyRateCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("获取集团比例信息列表");
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.listCompanyRateCashRecord(platformCashRecordDTO);
    }

    /***
     *
     * lkm
     *  {
     *   currentPage: 当前页 (required = false)
     *   pageSize: 每页记录数 (required = false)
     *   startTime:起始时间(required = false)
     *   endTime:结束时间(required = false)
     *   }
     * 获取平台备付金流水
     * @param
     * @return
     */
    @PostMapping("/platform-cash-flow")
    public ResponseVO getPlatformCashFlow(@RequestBody PlatformCashRecordDTO platformCashRecordDTO ){


        return platformCashService.getPlatformCashFlow(platformCashRecordDTO);

    }
}
