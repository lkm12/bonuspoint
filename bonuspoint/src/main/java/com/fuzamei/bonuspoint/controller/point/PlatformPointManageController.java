/**
 * FileName: PlatformController
 * Author: wangtao
 * Date: 2018/4/27 15:12
 * Description:
 */
package com.fuzamei.bonuspoint.controller.point;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.point.SendPointDTO;

import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.point.GeneralPointService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.service.point.PlatformPointService;
import com.fuzamei.common.model.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author wangtao
 * @create 2018/4/27
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/point/platform")
public class PlatformPointManageController {

    @Value("${page.pageSize}")
    private Integer size;

    @Value("${page.maxSize}")
    private Integer maxSize;

    private final PlatformPointService platformPointService;

    private final AccountService accountService;
    private final GeneralPointService generalPointService;

    @Autowired
    public PlatformPointManageController(PlatformPointService platformPointService, AccountService accountService, GeneralPointService generalPointService) {
        this.platformPointService = platformPointService;
        this.accountService = accountService;
        this.generalPointService = generalPointService;
    }

    /**
     * 同意集团发放积分
     * @param token token
     * @return
     */
    @GetMapping("/applyrecord/{id}/review")
    public ResponseVO reviewPoint(@RequestAttribute("token") Token token, @PathVariable Long id) {
        log.info("集团审核积分同意");

        ResponseVO result =  platformPointService.reviewPoint(token.getUid(), id);
        return  result ;
    }

    /**
     * 拒绝集团发放积分申请
     * @param token token
     * @param id id
     * @param reason 理由
     * @return
     */
    @GetMapping("/applyrecord/{id}/refuse")
    public ResponseVO refusePoint(@RequestAttribute("token") Token token, @PathVariable Long id, @RequestParam String reason) {
        log.info("平台拒绝集团发行积分");

        return platformPointService.refusePoint(token.getUid(), id, reason);
    }

    /**
     * 平台转积分给用户
     * @param token
     * @param sendPointDTO{
     *                        toId     对方用户id      require=true
     *                        num      积分数量        require=true
     *                        memo     备注           require=false
     *                        payword  支付密码        require=true
     *
     *                   }
     * @return
     */
    @LogAnnotation(note = "平台转积分给用户")
    @PostMapping("sendPoint")
    public ResponseVO sendGeneralPointToMember(@RequestAttribute("token") Token token, @RequestBody SendPointDTO sendPointDTO) {
        if (sendPointDTO.getNum()==null ||sendPointDTO.getNum().compareTo(BigDecimal.ZERO)!=1){
            log.info("积分数量错误：");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }
        boolean check = accountService.checkPayword(sendPointDTO.getPayword(), token.getUid());
        if (!check) {
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }
        sendPointDTO.setFromId(token.getUid());
        return generalPointService.sendGeneralPoint(sendPointDTO);
    }


}
