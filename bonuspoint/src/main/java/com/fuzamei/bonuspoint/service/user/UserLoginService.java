package com.fuzamei.bonuspoint.service.user;

import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface UserLoginService {


    /**商户，平台与大平台网页端*/
    ResponseVO browserLogin(UserDTO userDTO);

    /**用户登录*/
    ResponseVO memberLogin(UserDTO userDTO);
}
