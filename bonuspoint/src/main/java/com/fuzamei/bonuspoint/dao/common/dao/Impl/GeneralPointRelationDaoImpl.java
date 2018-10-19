package com.fuzamei.bonuspoint.dao.common.dao.Impl;

import com.fuzamei.bonuspoint.dao.common.dao.GeneralPointRelationDao;
import com.fuzamei.bonuspoint.dao.common.mapper.GeneralPointRelationMapper;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRelationPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/6/28 16:56
 */
@Component
public class GeneralPointRelationDaoImpl implements GeneralPointRelationDao {

    private final GeneralPointRelationMapper generalPointRelationMapper;

    @Autowired
    public GeneralPointRelationDaoImpl(GeneralPointRelationMapper generalPointRelationMapper) {
        this.generalPointRelationMapper = generalPointRelationMapper;

    }

    /**
     * @param uid 用户id
     * @param num 通用积分数量
     * @return 0 or 1
     * @author qbanxiaoli
     * @description 更新通用积分
     */
    @Override
    public int updateGeneralPoint(Long uid, BigDecimal num) {
        //设置更新母版
        Example example = new Example(GeneralPointRelationPO.class);
        Example.Criteria criteria = example.createCriteria();
        //根据userId更新num
        criteria.andEqualTo("userId", uid);
        GeneralPointRelationPO generalPointRelationPO = new GeneralPointRelationPO();
        generalPointRelationPO.setNum(num);
        //更新用户通用积分
        return generalPointRelationMapper.updateByExampleSelective(generalPointRelationPO, example);
    }

}
