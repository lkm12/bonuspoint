package com.fuzamei.bonuspoint.service.user;

import com.fuzamei.bonuspoint.entity.dto.user.*;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.common.model.vo.ResponseVO;

import java.util.List;

/**
 * @author: WangJie
 **/

public interface UserService {

    UserPO getUserRoleById(Long id);

    UserPO getUser(List<String> fields, UserDTO userDTO);


    /**
     * @author: lkm
     **/

    /**用户注册*/
    ResponseVO addUser(UserDTO userDTO1);
    /**获取用户信息*/
    ResponseVO findUserInfoById(Long id);


    /**获取用户信息*/
    ResponseVO findInviteInfo(Long id);
    /**获取账户二维码*/
    ResponseVO findQrcodeById(Long id);


    /**平台模糊查询用户信息(平台)*/
    ResponseVO getUserInfoListFromPlatform(PagePointDTO pagePointDTO);

    /**查找邀请码信息*/
    ResponseVO findInvite(PagePointDTO pagePointDTO);
    /**获取用户信息(APP)*/
    ResponseVO findUserInfoByIdAPP(Long uid);

    /**查询用户持有对应集团的可用会员积分总数*/
    ResponseVO getCompanyPoint(Long uid,Long gid);



}
