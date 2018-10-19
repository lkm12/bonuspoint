package com.fuzamei.bonuspoint.service.point;

import com.fuzamei.bonuspoint.entity.dto.point.SendPointDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface GeneralPointService {
    /**
     * 转通用积分
     * @param sendPointDTO
     * @return
     */
    ResponseVO sendGeneralPoint(SendPointDTO sendPointDTO);
}
