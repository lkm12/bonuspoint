package com.fuzamei.bonuspoint.dao.common.mapper;

import com.fuzamei.bonuspoint.entity.po.point.GeneralPointInfoPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GeneralPointInfoMapper extends TkMapper<GeneralPointInfoPO> {

}
