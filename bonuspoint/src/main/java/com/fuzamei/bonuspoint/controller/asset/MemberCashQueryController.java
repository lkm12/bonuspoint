package com.fuzamei.bonuspoint.controller.asset;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.MemberCashRecordVO;
import com.fuzamei.bonuspoint.service.asset.MemberCashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/asset/member")
public class MemberCashQueryController {

    private final MemberCashService memberCashService;

    @Autowired
    public MemberCashQueryController(MemberCashService memberCashService) {
        this.memberCashService = memberCashService;
    }

    /**
     * @param token 令牌 (required = true)
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个会员资产信息接口
     */
    @GetMapping("/member-cash/get")
    public ResponseVO<MemberCashRecordVO> getMemberCashRecord(@RequestAttribute("token") Token token) {
        log.info("获取单个会员资产信息");
        // 请求服务
        log.info("参数校验正常");
        return memberCashService.getMemberCashRecord(token.getUid());
    }

    /**
     * @param token 令牌 (required = true)
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表接口
     */
    @GetMapping("/member-point/list")
    public ResponseVO<List<MemberCashRecordVO>> listMemberPointCashRecord(@RequestAttribute("token") Token token) {
        log.info("获取会员用户集团积分列表");
        // 请求服务
        log.info("参数校验正常");
        return memberCashService.listMemberPointCashRecord(token.getUid());
    }

    /**
     * @param token 令牌 (required = true)
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分详情接口
     */
    @GetMapping("/member-point-detail/get")
    public ResponseVO<List<MemberCashRecordVO>> getMemberPointCashRecordDetail(@RequestAttribute("token") Token token) {
        log.info("获取会员用户集团积分详情");
        // 请求服务
        log.info("参数校验正常");
        return memberCashService.getMemberPointCashRecordDetail(token.getUid());
    }

}
