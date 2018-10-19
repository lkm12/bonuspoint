package com.fuzamei.bonuspoint.dao.common.mapper;

import com.fuzamei.bonuspoint.entity.po.asset.CashRecordPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/18 12:54
 */
@Mapper
@Repository
public interface CashRecordMapper extends TkMapper<CashRecordPO> {

}
