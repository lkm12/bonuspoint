package com.fuzamei.bonuspoint.entity.vo.user;

import com.fuzamei.common.model.vo.PageVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-02 11:47
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryUserVO extends PageVO {
    /** 用户id*/
    private Long uid ;
    /**集团信息id*/
    private Long companyId;

    private String mobile;

}
