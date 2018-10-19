package com.fuzamei.bonuspoint.service.impl.account;

import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.common.mapper.AccountMapper;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.vo.account.AccountVO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.account.AccountService;
import com.fuzamei.bonuspoint.util.MD5HashUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-06-27 10:02
 **/
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    private final AccountMapper accountMapper;
    @Value("${md5.salt}")
    private String salt;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao, AccountMapper accountMapper) {
        this.accountDao = accountDao;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountPO getUserById(Long id) {
        return accountDao.getUserById(id);
    }


    @Override
    public int countUser(AccountPO accountPO) {
        return accountMapper.selectCount(accountPO);
    }

    @Override
    public boolean checkMobileAvailable(Long pId, String mobile) {
        int count = accountDao.checkMobileAvailable(pId, mobile);
        if (count == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPayword(String payword, Long uid) {
        if (payword == null || "".equals(payword)) {
            return false;
        }
        AccountPO accountPO = accountDao.getUserById(uid);
        if (accountPO != null) {
            try {
                String paywordHash = MD5HashUtil.md5SaltEncrypt(payword, salt);
                if (paywordHash.equals(accountPO.getPaywordHash())) {
                    return true;
                }
            } catch (Exception e) {
                log.info("MD5错误" + e.getMessage());
                throw new RuntimeException("MD5错误" + e.getMessage());
            }

        }
        return false;
    }

    @Override
    public boolean checkPassword(String password, Long uid) {
        if (password == null || "".equals(password)) {
            return false;
        }
        AccountPO accountPO = accountDao.getUserById(uid);
        if (accountPO != null) {
            try {
                String paswordHash = MD5HashUtil.md5SaltEncrypt(password, salt);
                if (paswordHash.equals(accountPO.getPasswordHash())) {
                    return true;
                }
            } catch (Exception e) {
                log.info("MD5错误" + e.getMessage());
                throw new RuntimeException("MD5错误" + e.getMessage());
            }

        }
        return false;
    }

    @Override
    public ResponseVO<List<AccountVO>> getAccountInfo(AccountDTO accountDTO) {


        AccountPO accountPO = new AccountPO();
        BeanUtils.copyProperties(accountDTO, accountPO);
        List<AccountPO> accountPOS = accountMapper.select(accountPO);
        List<AccountVO> accountVOS = accountPOS.stream()
                .map(account -> {
                    AccountVO accountVO = new AccountVO();
                    BeanUtils.copyProperties(account, accountVO);
                    return accountVO;
                })
                .collect(Collectors.toList());
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, accountVOS);
    }

    @Override
    public ResponseVO<AccountVO> getAccountInfoByPublicKey(String publicKey) {
        AccountPO accountPO = accountDao.getUserByPublicKey(publicKey);
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(accountPO, accountVO);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, accountVO);
    }

    @Override
    public AccountPO getAccount(AccountDTO accountDTO) {
        AccountPO accountPO = new AccountPO();
        BeanUtils.copyProperties(accountDTO, accountPO);
        return accountMapper.selectOne(accountPO);
    }

    @Override
    public ResponseVO updateNickname(AccountDTO accountDTO) {
        AccountPO accountPO = new AccountPO();
        accountPO.setId(accountDTO.getId());
        accountPO.setNickname(accountDTO.getNickname());
        accountPO.setUpdatedAt(TimeUtil.timestamp());
        int result = accountMapper.updateByPrimaryKeySelective(accountPO);
        if (result == 1) {
            Map<String, String> map = new HashMap<>(16);
            map.put("nickname", accountPO.getNickname());
            return new ResponseVO<>(CommonResponseEnum.SET_SUCCESS, map);
        }
        return new ResponseVO(CommonResponseEnum.SET_FAIL);
    }
}
