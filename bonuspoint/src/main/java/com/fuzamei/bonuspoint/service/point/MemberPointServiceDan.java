package com.fuzamei.bonuspoint.service.point;

import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface MemberPointServiceDan {

    ResponseVO exchange(ExchangePointDTO exchangeDTO);
    ResponseVO memberTranPoint(ExchangePointDTO exchangeDTO);
}
