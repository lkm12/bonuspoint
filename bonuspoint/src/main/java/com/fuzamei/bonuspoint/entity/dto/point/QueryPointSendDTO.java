package com.fuzamei.bonuspoint.entity.dto.point;

import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询集团发送积分数量
 * @author liumeng
 * @create 2018年5月8日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPointSendDTO extends PageDTO {
    /** 集团id*/
    private String companyId;
    /** 积分名称*/
    private String pointName;
    /** 发给用户名称*/
    private Long userId;

}
