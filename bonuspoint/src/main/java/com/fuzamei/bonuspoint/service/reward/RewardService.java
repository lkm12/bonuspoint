package com.fuzamei.bonuspoint.service.reward;

import com.fuzamei.bonuspoint.entity.dto.reward.RewardDTO;
import com.fuzamei.bonuspoint.entity.query.RewardQuery;
import com.fuzamei.common.model.vo.ResponseVO;

public interface RewardService {

    /**
     * 添加邀请奖励规则
     * @param rewardDTO
     * @return
     */
    ResponseVO addRewardRule(RewardDTO rewardDTO);

    /**
     * 使邀请奖励规则生效
     * @param id
     * @param platformId
     * @return
     */
    ResponseVO effectiveRewardRule(Long id, Long platformId);

    /**
     * 暂停奖励规则
     * @param id
     * @param platformId
     * @return
     */
    ResponseVO stopReward(Long id, Long platformId);


    /**
     * 删除奖励规则
     * @param id
     * @param uid
     * @return
     */
    ResponseVO deleteReward(Long id, Long uid);

    /**
     * 查看奖励规则列表
     * @param rewardQuery
     * @return
     */
    ResponseVO listRewardRules(RewardQuery rewardQuery);

    /**
     * 查看商户能够用于奖励的积分
     * @param companyId
     * @return
     */
    ResponseVO listCompanyRewardPoints(Long companyId,Long platformId);
}
