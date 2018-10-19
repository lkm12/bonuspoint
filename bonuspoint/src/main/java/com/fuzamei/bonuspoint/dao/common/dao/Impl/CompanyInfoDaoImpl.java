package com.fuzamei.bonuspoint.dao.common.dao.Impl;

import com.fuzamei.bonuspoint.dao.common.dao.CompanyInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.CompanyInfoMapper;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/9 上午10:29
 */
@Component
public class CompanyInfoDaoImpl implements CompanyInfoDao {

    private final CompanyInfoMapper companyInfoMapper;

    @Autowired
    public CompanyInfoDaoImpl(CompanyInfoMapper companyInfoMapper) {
        this.companyInfoMapper = companyInfoMapper;
    }

    /**
     * @param uid    用户id
     * @param amount 备付金金额
     * @return 0 or 1
     * @author qbanxiaoli
     * @description 更新备付金金额
     */
    @Override
    public int UpdateProvisions(Long uid, BigDecimal amount) {
        //设置更新母版
        Example example = new Example(CompanyInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        //根据uid更新amount
        criteria.andEqualTo("uid", uid);
        CompanyInfoPO companyInfoPO = new CompanyInfoPO();
        companyInfoPO.setAmount(amount);
        //更新备付金金额
        return companyInfoMapper.updateByExampleSelective(companyInfoPO, example);
    }

}
