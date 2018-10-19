package com.fuzamei.bonuspoint.controller.asset;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.enums.AssetResponseEnum;
import com.fuzamei.bonuspoint.entity.dto.asset.CompanyCashRecordDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.asset.CompanyCashService;
import com.fuzamei.bonuspoint.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/17
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/asset/company")
public class CompanyCashManageController {

    private final CompanyCashService companyCashService;

    @Autowired
    public CompanyCashManageController(CompanyCashService companyCashService) {
        this.companyCashService = companyCashService;
    }

    /**
     * @param token                令牌 (required = true)
     * @param companyCashRecordDTO 请求参数
     *                             {
     *                             amount: 金额数量 (required = true)
     *                             payword: 交易密码 (required = true)
     *                             }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 申请充值备付金接口
     */
    @PostMapping("/recharge/save")
    public ResponseVO saveRechargeCashRecord(@RequestAttribute("token") Token token,
                                             @RequestBody CompanyCashRecordDTO companyCashRecordDTO) {
        log.info("申请充值备付金");
        companyCashRecordDTO.setUid(token.getUid());
        // 参数处理判断
        if (companyCashRecordDTO.getAmount() == null || companyCashRecordDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("金额数量不能为空");
            return new ResponseVO(AssetResponseEnum.CASH_RECORD_AMOUNT);
        }
        if (StringUtil.isBlank(companyCashRecordDTO.getPayword())) {
            log.info("交易密码不能为空");
            return new ResponseVO(AssetResponseEnum.CASH_RECORD_PASSWORD);
        }
        // 请求服务
        log.info("参数校验正常");
        return companyCashService.saveRechargeCashRecord(companyCashRecordDTO);
    }

    /**
     * @param token                令牌 (required = true)
     * @param companyCashRecordDTO 请求参数
     *                             {
     *                             amount: 金额数量 (required = true)
     *                             payword: 交易密码 (required = true)
     *                             }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 集团提现接口
     */
    @PostMapping("/withdraw/save")
    public ResponseVO saveWithdrawCashRecord(@RequestAttribute("token") Token token,
                                             @RequestBody CompanyCashRecordDTO companyCashRecordDTO) {
        log.info("集团提现");
        companyCashRecordDTO.setUid(token.getUid());
        // 参数处理判断
        if (companyCashRecordDTO.getAmount() == null || companyCashRecordDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("金额数量不能为空");
            return new ResponseVO(AssetResponseEnum.CASH_RECORD_AMOUNT);
        }
        if (StringUtil.isBlank(companyCashRecordDTO.getPayword())) {
            log.info("交易密码不能为空");
            return new ResponseVO(AssetResponseEnum.CASH_RECORD_PASSWORD);
        }
        // 请求服务
        log.info("参数校验正常");
        return companyCashService.saveWithdrawCashRecord(companyCashRecordDTO);
    }

}
