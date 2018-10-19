package com.fuzamei.bonuspoint.dao.reward;

import com.fuzamei.bonuspoint.entity.po.reward.RewardPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RewardMapper extends TkMapper<RewardPO> {


}
