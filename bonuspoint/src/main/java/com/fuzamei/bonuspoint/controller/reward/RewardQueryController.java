package com.fuzamei.bonuspoint.controller.reward;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.query.RewardQuery;
import com.fuzamei.bonuspoint.service.reward.RewardService;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: bonus-point-cloud
 * @description: 邀请、注册奖励规则读接口
 * @author: WangJie
 * @create: 2018-09-12 11:19
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/platform/invite-reward")
public class RewardQueryController {
    private final RewardService rewardService;

    @Autowired
    public RewardQueryController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @PostMapping("/list-rules")
    public ResponseVO listRewardRules(@RequestBody RewardQuery rewardQuery , @RequestAttribute("token")Token token){
        rewardQuery.setPlatformId(token.getUid());
        return rewardService.listRewardRules(rewardQuery);
    }

    @GetMapping("/list-company-reward-points/{companyId}")
    public ResponseVO listRewardPoints(@PathVariable("companyId") Long companyId ,@RequestAttribute("token")Token token ){
        return rewardService.listCompanyRewardPoints(companyId,token.getUid());
    }
}
