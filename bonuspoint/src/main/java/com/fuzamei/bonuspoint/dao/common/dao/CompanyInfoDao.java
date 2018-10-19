package com.fuzamei.bonuspoint.dao.common.dao;

import java.math.BigDecimal;

public interface CompanyInfoDao {

    /**
     * @param uid 用户id
     * @param amount 备付金金额
     * @return 0 or 1
     * @author qbanxiaoli
     * @description 更新备付金金额
     */
    int UpdateProvisions(Long uid, BigDecimal amount);

}
