package com.fuzamei.bonuspoint.entity.dto.user;

import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-02 11:54
 **/

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryUserDTO extends PageDTO {

    /** 用户id */
    private Long uid ;
    /** 集团信息id */
    private Long companyId;
    /** 手机号 */
    private String mobile;
}
