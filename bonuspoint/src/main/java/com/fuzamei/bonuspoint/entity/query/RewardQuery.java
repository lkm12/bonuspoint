package com.fuzamei.bonuspoint.entity.query;

import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: bonus-point-cloud
 * @description: 奖励规则查询条件
 * @author: WangJie
 * @create: 2018-09-12 11:32
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class RewardQuery extends PageDTO {
    private Long platformId;
    private Integer status;

}
