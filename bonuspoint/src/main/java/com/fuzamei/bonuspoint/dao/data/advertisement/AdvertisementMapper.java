package com.fuzamei.bonuspoint.dao.data.advertisement;

import com.fuzamei.bonuspoint.entity.po.data.advertisement.AdvertisementPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdvertisementMapper extends TkMapper<AdvertisementPO> {
}
