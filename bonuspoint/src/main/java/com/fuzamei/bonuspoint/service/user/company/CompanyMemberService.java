package com.fuzamei.bonuspoint.service.user.company;

import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface CompanyMemberService {
    /**
     * 统计集团会员活跃数据
     * @param companyId
     * @return
     */
    ResponseVO getMemberActivity(Long  companyId);
}
