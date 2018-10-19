package com.fuzamei.bonuspoint.service.user;

import com.fuzamei.bonuspoint.entity.dto.user.UserAddressDTO;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface UserAddressService {

    ResponseVO getUserAddressList(Long uid, PageDTO pageDTO);
    ResponseVO saveUserAddress(UserAddressDTO userAddressDTO);
    ResponseVO updateUserAddress(UserAddressDTO userAddressDTO);
    ResponseVO deleteUserAddress(Long addressId,Long uid);

    ResponseVO<UserAddressDTO> getDefaultAddress(Long defaultAddressId);

    ResponseVO updateDefaultAddress(Long uid,Long addressId);
}
