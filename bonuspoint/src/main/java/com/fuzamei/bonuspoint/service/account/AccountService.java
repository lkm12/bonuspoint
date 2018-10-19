package com.fuzamei.bonuspoint.service.account;

import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.vo.account.AccountVO;
import com.fuzamei.common.model.vo.ResponseVO;

import java.util.List;

public interface AccountService {

    AccountPO getUserById(Long id);


    /**
     * 通过不为空的字段统计用户数量
     * 目前有 pid role mobile username publicKey loginAt createdat
     * 时间统计大于该时间的
     * @param accountPO
     * @return
     */
    int countUser(AccountPO accountPO);

    /**
     * 检查同一父级下用户输入的手机号是否已经被使用
     * @param pId 父级id
     * @param mobile 手机号
     * @return
     */
    boolean checkMobileAvailable(Long pId, String mobile);

    /**
     * 检查支付密码
     * @param payword
     * @param uid
     * @return
     */
    boolean checkPayword(String payword , Long uid);

    /**
     * 检查支付密码
     * @param password
     * @param uid
     * @return
     */
    boolean checkPassword(String password , Long uid);


    /**
     * 获取账户信息，转账使用
     * @param accountDTO
     * @return
     */
    ResponseVO<List<AccountVO>> getAccountInfo(AccountDTO accountDTO);

    ResponseVO<AccountVO> getAccountInfoByPublicKey(String publicKey);

    AccountPO getAccount(AccountDTO accountDTO);

    /**
     * 修改昵称
     * @param accountDTO{
     *                  id
     *                  nickname
     *                  }
     *
     * @return
     */
    ResponseVO updateNickname(AccountDTO accountDTO);

}
