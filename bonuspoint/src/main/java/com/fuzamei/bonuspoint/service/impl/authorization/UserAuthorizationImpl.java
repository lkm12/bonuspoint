package com.fuzamei.bonuspoint.service.impl.authorization;

import com.fuzamei.bonuspoint.dao.authorization.UserAuthorizationDao;
import com.fuzamei.bonuspoint.entity.dto.authorization.UserTokenDTO;
import com.fuzamei.bonuspoint.entity.po.authorization.UserTokenPO;
import com.fuzamei.bonuspoint.service.authorization.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: bonus-point-cloud
 * @description: 验证token，权限管理
 * @author: WangJie
 * @create: 2018-05-08 09:56
 **/
@Service
@Transactional
public class UserAuthorizationImpl implements UserAuthorizationService {

    private final UserAuthorizationDao userAuthorizationDao;

    @Autowired
    public UserAuthorizationImpl(UserAuthorizationDao userAuthorizationDao) {
        this.userAuthorizationDao = userAuthorizationDao;
    }

    @Override
    public boolean isRightToken(UserTokenDTO userTokenDTO) {
        return userAuthorizationDao.countByTokenAndUid(userTokenDTO)==1? true:false;
    }

    @Override
    public int updateToken(UserTokenDTO userTokenDTO) {
        return userAuthorizationDao.updateToken(userTokenDTO);
    }

    @Override
    public int addToken(UserTokenDTO userTokenDTO) {
        return userAuthorizationDao.addToken(userTokenDTO);
    }

    @Override
    public UserTokenPO getUserTokenByTokenAndUid(UserTokenDTO userTokenDTO) {
        return userAuthorizationDao.getUserTokenByTokenAndUid(userTokenDTO);
    }

    @Override
    public UserTokenPO getBrowserTokenByUid(Long uid) {
        return userAuthorizationDao.getBrowserTokenByUid(uid);
    }

    @Override
    public UserTokenPO getAppTokenByUid(Long uid) {
        return userAuthorizationDao.getAppTokenByUid(uid);
    }
}
