package com.fuzamei.bonuspoint.dao.common.mapper;

import com.fuzamei.common.mapper.TkMapper;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/6/28 16:31
 */
@Mapper
@Repository
public interface GeneralPointRecordMapper extends TkMapper<GeneralPointRecordPO> {

}

