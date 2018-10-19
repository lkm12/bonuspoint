package com.fuzamei.bonuspoint.service.impl.user;

import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.location.LocationPO;
import com.fuzamei.bonuspoint.dao.location.LocationDao;
import com.fuzamei.bonuspoint.dao.user.UserAddressDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.user.UserAddressDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.user.UserAddressPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.enums.AddressResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.user.UserAddressService;
import com.fuzamei.common.bean.PageBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-20 16:03
 **/
@Slf4j
@Service
@RefreshScope
@Transactional(rollbackFor = Exception.class)
public class UserAddressServiceImpl implements UserAddressService {

    @Value("${page.addressPageSize}")
    private Integer pageSize;


    private final UserDao userDao;

    private final LocationDao locationDao;

    private final UserAddressDao userAddressDao;

    private final AccountDao accountDao;

    @Autowired
    public UserAddressServiceImpl(UserDao userDao, LocationDao locationDao, UserAddressDao userAddressDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.locationDao = locationDao;
        this.userAddressDao = userAddressDao;
        this.accountDao = accountDao;
    }

    @Override
    public ResponseVO getUserAddressList(Long uid, PageDTO pageDTO) {
        List<UserAddressDTO> userAddressDTOList = new ArrayList<>(8);
        List<UserAddressPO> addressList;
        AccountPO accountPO = accountDao.getUserById(uid);
        Page page = null;
        if (pageDTO.getPage() != null && pageDTO.getPageSize() != null && pageDTO.getPage() == -1 && pageDTO.getPageSize() == -1) {
            addressList = userAddressDao.getUserAddressList(uid);
        } else {
            if (pageDTO.getPage() == null || pageDTO.getPage() < 1) {
                pageDTO.setPage(1);
            }
            if (pageDTO.getPageSize() == null || pageDTO.getPageSize() < 1) {
                pageDTO.setPageSize(pageSize);
            }
            page = PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize());
            addressList = userAddressDao.getUserAddressList(uid);
        }
        for (UserAddressPO userAddressPO : addressList
                ) {
            UserAddressDTO address = new UserAddressDTO();
            BeanUtils.copyProperties(userAddressPO, address);
            LocationPO locationPO = locationDao.getLocationByDistrictCode(userAddressPO.getDistrictCode());
            BeanUtils.copyProperties(locationPO, address);
            if (address.getId().equals(accountPO.getDefaultAddress())) {
                address.setIsDefaultAddress(1);
            } else {
                address.setIsDefaultAddress(0);
            }
            userAddressDTOList.add(address);
        }
        int total = 0;
        if (page == null) {
            total = addressList.size();
        } else {
            total = Integer.valueOf(String.valueOf(page.getTotal()));
        }
        PageBean pageBean = new PageBean<>(userAddressDTOList, pageDTO.getPage(), pageDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    @Override
    public ResponseVO saveUserAddress(UserAddressDTO userAddressDTO) {
        int result = userAddressDao.saveUserAddress(userAddressDTO);
        if (result == 1) {
            //如果用户还未添加收货地址，将改地址设为默认地址
            List<String> fields = new ArrayList<>(1);
            fields.add("default_address");
            fields.add("id");
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userAddressDTO.getUid());
            UserPO userPO = userDao.getUser(fields, userDTO);
            if (userPO.getDefaultAddress() == null) {
                userDTO.setDefaultAddress(userAddressDTO.getId());
                userDao.updateUser(userDTO);
            }
            Map<String, Long> map = new HashMap<>(16);
            map.put("id", userAddressDTO.getId());
            return new ResponseVO<>(CommonResponseEnum.SET_SUCCESS, map);
        }
        return new ResponseVO(CommonResponseEnum.SET_FAIL);
    }

    @Override
    public ResponseVO updateUserAddress(UserAddressDTO userAddressDTO) {
        AccountPO accountPO = accountDao.getUserById(userAddressDTO.getUid());

        Long oldAddressId = userAddressDTO.getId();
        userAddressDao.setIsDelete(oldAddressId);
        userAddressDTO.setId(null);
        userAddressDTO.setUpdateAt(TimeUtil.timestamp());
        userAddressDTO.setCreatedAt(TimeUtil.timestamp());
        userAddressDao.saveUserAddress(userAddressDTO);
        if (accountPO.getDefaultAddress() != null && accountPO.getDefaultAddress().equals(oldAddressId)) {
            accountDao.updateDefaultAddress(userAddressDTO.getUid(), userAddressDTO.getId());
        }
        return new ResponseVO<>(SafeResponseEnum.UPDATE_SUCCESS);

    }

    @Override
    public ResponseVO deleteUserAddress(Long addressId, Long uid) {
        AccountPO accountPO = accountDao.getUserById(uid);
        if (accountPO.getDefaultAddress() != null && accountPO.getDefaultAddress().equals(addressId)) {
            accountDao.updateDefaultAddress(uid, null);
        }
        int result = userAddressDao.setIsDelete(addressId);
        if (result == 1) {
            return new ResponseVO<>(CommonResponseEnum.DELETE_SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.DELETE_FAIL);
    }

    @Override
    public ResponseVO<UserAddressDTO> getDefaultAddress(Long defaultAddressId) {
        UserAddressPO userAddressPO = userAddressDao.getAddressById(defaultAddressId);
        if (userAddressPO == null) {
            return new ResponseVO<>(CommonResponseEnum.FAILURE);
        }
        UserAddressDTO address = new UserAddressDTO();
        BeanUtils.copyProperties(userAddressPO, address);
        LocationPO locationPO = locationDao.getLocationByDistrictCode(userAddressPO.getDistrictCode());
        BeanUtils.copyProperties(locationPO, address);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, address);
    }

    @Override
    public ResponseVO updateDefaultAddress(Long uid, Long addressId) {
        UserAddressPO addressPO = userAddressDao.getAddressById(addressId);
        if (addressPO == null || !addressPO.getUid().equals(uid)) {
            return new ResponseVO(AddressResponseEnum.ADDRESS_NOT_EXIT);
        }
        int result = accountDao.updateDefaultAddress(uid, addressId);
        if (result == 1) {
            return new ResponseVO<>(CommonResponseEnum.SUCCESS);
        }
        return new ResponseVO<>(CommonResponseEnum.FAILURE);
    }
}
