package com.fuzamei.bonuspoint.controller.asset;

import com.fuzamei.bonuspoint.enums.AssetResponseEnum;
import com.fuzamei.bonuspoint.entity.dto.asset.PlatformCashRecordDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.asset.PlatformCashService;
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
public class PlatformCashManageController {

    private final PlatformCashService platformCashService;

    @Autowired
    public PlatformCashManageController(PlatformCashService platformCashService) {
        this.platformCashService = platformCashService;
    }

    /**
     * @param id 流水号
     * @return 请求响应
     * @author qbanxiaoli
     * @description 备付金审核通过接口
     */
    @PutMapping("/recharge/check/{id}")
    public ResponseVO checkRechargeCashRecord(@PathVariable("id") Long id) {
        log.info("备付金审核通过");
        // 参数处理判断
        if (id == null) {
            log.info("流水号不能为空");
            return new ResponseVO(AssetResponseEnum.CASH_RECORD_ID);
        }
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.checkRechargeCashRecord(id);
    }

    /**
     * @param platformCashRecordDTO 备付金充值记录数据传输类
     *                              {
     *                              id: 流水号（required = true）
     *                              reason：拒绝原因（required = false）
     *                              }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 备付金审核拒绝接口
     */
    @PutMapping("/recharge/refuse")
    public ResponseVO refuseRechargeCashRecord(@RequestBody PlatformCashRecordDTO platformCashRecordDTO) {
        log.info("备付金审核拒绝");
        // 参数处理判断
        if (platformCashRecordDTO.getId() == null) {
            log.info("流水号不能为空");
            return new ResponseVO(AssetResponseEnum.CASH_RECORD_ID);
        }
        // 请求服务
        log.info("参数校验正常");
        return platformCashService.refuseRechargeCashRecord(platformCashRecordDTO);
    }

}
