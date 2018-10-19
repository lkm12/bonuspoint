package com.fuzamei.bonuspoint.entity.form.point;

import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.PageVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-10 17:53
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPointRecordFORM extends PageDTO {
    /** 积分名称 */
    private String name;
    /** 用户手机号*/
    private String mobile;
}
