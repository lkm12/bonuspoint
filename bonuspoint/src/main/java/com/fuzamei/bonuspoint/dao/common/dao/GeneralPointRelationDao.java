package com.fuzamei.bonuspoint.dao.common.dao;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/6/28 16:31
 */
public interface GeneralPointRelationDao  {

    /**
     * @param uid 用户id
     * @param num 通用积分数量
     * @return 0 or 1
     * @author qbanxiaoli
     * @description 更新通用积分
     */
    int updateGeneralPoint(Long uid, BigDecimal num);


}
