package com.fuzamei.bonuspoint.service.authorization;

import com.fuzamei.bonuspoint.entity.dto.authorization.UserTokenDTO;
import com.fuzamei.bonuspoint.entity.po.authorization.UserTokenPO;

public interface UserAuthorizationService {
    boolean isRightToken(UserTokenDTO userTokenDTO);
    int updateToken(UserTokenDTO userTokenDTO);
    int addToken(UserTokenDTO userTokenDTO);
    UserTokenPO getUserTokenByTokenAndUid(UserTokenDTO userTokenDTO);

    UserTokenPO getBrowserTokenByUid(Long uid);

    UserTokenPO getAppTokenByUid(Long uid);
}
