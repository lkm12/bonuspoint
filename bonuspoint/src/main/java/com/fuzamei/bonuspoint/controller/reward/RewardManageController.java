package com.fuzamei.bonuspoint.controller.reward;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.reward.RewardDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.point.GeneralPointService;
import com.fuzamei.bonuspoint.service.reward.RewardService;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: bonus-point-cloud
 * @description: 邀请、注册奖励 写接口
 * @author: WangJie
 * @create: 2018-09-11 17:57
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/platform/invite-reward")
public class RewardManageController {

    private final RewardService rewardService;
    private final GeneralPointService generalPointService;

    @Autowired
    public RewardManageController(RewardService rewardService, GeneralPointService generalPointService) {
        this.rewardService = rewardService;
        this.generalPointService = generalPointService;
    }

    /**
     * 添加邀请奖励注册规则
     * @param token
     * @param rewardDTO
     * @return
     */
    @PostMapping("/add-rule")
    public ResponseVO addRewardRule(@RequestAttribute("token") Token token,
                                          @RequestBody @Valid RewardDTO rewardDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("参数错误：{}", bindingResult.getFieldError().getDefaultMessage());
            return new ResponseVO<>(CommonResponseEnum.FAILURE, bindingResult.getFieldError().getDefaultMessage());
        }
        rewardDTO.setPlatformId(token.getUid());
        return rewardService.addRewardRule(rewardDTO);
    }

    /**
     * 使奖励规则生效
     * @param id
     * @param token
     * @return
     */
    @GetMapping("/effective/{id}")
    public ResponseVO effectiveRewardRule(@PathVariable("id") Long id,@RequestAttribute("token") Token token){
        return rewardService.effectiveRewardRule(id,token.getUid());
    }
    /**
     * 暂停奖励规则
     */
    @GetMapping("/stop/{id}")
    public ResponseVO stopReward(@PathVariable("id") Long id,@RequestAttribute("token") Token token){
        return rewardService.stopReward(id,token.getUid());
    }

    /**
     * 删除奖励规则
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO deleteReward(@PathVariable("id") Long id,@RequestAttribute("token") Token token){
        return rewardService.deleteReward(id,token.getUid());
    }




}
