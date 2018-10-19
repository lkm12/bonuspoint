package com.fuzamei.bonuspoint.service.impl.safe;

import com.fuzamei.bonuspoint.constant.UserStatus;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.entity.dto.account.SecrecyDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.safe.SafeService;
import com.fuzamei.bonuspoint.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;


/**
 * @program: bonuspoint
 * @description:
 * @author: WangJie
 * @create: 2018-04-17 14:12
 **/
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class SafeServiceImpl implements SafeService {

    private final static String BIND = "1";

    private final static String NOTBIND = "0";

    private final AccountDao accountDao;

    @Autowired
    public SafeServiceImpl( AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public ResponseVO getSafeStatusInfo(Long uid) {
        UserDTO userDTO = new UserDTO();

        AccountPO accountPo = accountDao.getUserById(uid);

        if (StringUtil.isEmpty(accountPo.getPaywordHash())) {
            userDTO.setPaywordStatus(NOTBIND);
        } else {
            userDTO.setPaywordStatus(BIND);
            userDTO.setPaywordTime(accountPo.getPaywordAt());
        }

        if (StringUtil.isEmpty(accountPo.getMobile())) {
            userDTO.setMobileStatus(NOTBIND);
        } else {
            userDTO.setMobileStatus(BIND);
            userDTO.setMobile(accountPo.getMobile());
            userDTO.setCountry(accountPo.getCountry());
        }
        if (StringUtil.isEmpty(accountPo.getEmail())) {
            userDTO.setEmailStatus(NOTBIND);
        } else {
            userDTO.setEmailStatus(BIND);
            userDTO.setEmail(accountPo.getEmail());
        }
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, userDTO);
    }

    @Override
    public ResponseVO updatePassword(SecrecyDTO secrecyDTO) {

        secrecyDTO.setUpdatedAt(System.currentTimeMillis());
        secrecyDTO.setIsInitialize(UserStatus.INIT);
        int result = accountDao.updatePassword(secrecyDTO);
        if (result == 1) {
            Map<String, Long> map = new HashMap<>(16);
            map.put("uid", secrecyDTO.getId());
            return new ResponseVO<>(SafeResponseEnum.UPDATE_SUCCESS, map);
        }
        return new ResponseVO(SafeResponseEnum.UPDATE_FAIL);
    }

    @Override
    public ResponseVO updatePayWord(SecrecyDTO secrecyDTO) {
        secrecyDTO.setPaywordAt(System.currentTimeMillis());
        int result = accountDao.updatePayword(secrecyDTO);
        if (result == 1) {
            Map<String, Long> map = new HashMap<>(16);
            map.put("uid", secrecyDTO.getId());
            return new ResponseVO<>(CommonResponseEnum.SET_SUCCESS, map);
        }
        return new ResponseVO(CommonResponseEnum.SET_FAIL);
    }





    @Override
    public boolean hasMobile(Long uid) {
        AccountPO accountPO = accountDao.getUserById(uid);

        return StringUtil.isNotEmpty(accountPO.getMobile());
    }

    @Override
    public boolean hasEmail(Long uid) {
        AccountPO accountPO = accountDao.getUserById(uid);
        return StringUtil.isNotEmpty(accountPO.getEmail());
    }

  /*  private ResponseVO getResponseVOByResult(int result,Long uid){
        ResponseVO responseVO = new ResponseVO();


        if(result==1){
            responseVO.setResponseVO(ResponseEnum.SET_SUCCESS);
            Map map = new HashMap(4);
            map.put("uid",uid);
            responseVO.setData(map);
            return responseVO;
        }
        responseVO.setResponseVO(ResponseEnum.SET_FAIL);
        return responseVO;
    }*/

    @Override
    public ResponseVO updateMobile(SecrecyDTO secrecyDTO) {
        secrecyDTO.setUpdatedAt(System.currentTimeMillis());
        int result =  accountDao.updateMobile(secrecyDTO);
        if (result == 1) {
            Map<String, String> map = new HashMap<>(16);
            map.put("mobile", secrecyDTO.getMobile());
            return new ResponseVO<>(CommonResponseEnum.SET_SUCCESS, map);
        }
        return new ResponseVO(CommonResponseEnum.SET_FAIL);
    }

    @Override
    public ResponseVO updateEmail(SecrecyDTO secrecyDTO) {
        secrecyDTO.setUpdatedAt(System.currentTimeMillis());
        int result =  accountDao.updateEmail(secrecyDTO);
        if (result == 1) {
            Map<String, String> map = new HashMap<>(16);
            map.put("email", secrecyDTO.getEmail());
            return new ResponseVO<>(CommonResponseEnum.SET_SUCCESS, map);
        }
        return new ResponseVO(CommonResponseEnum.SET_FAIL);
    }
}
