package com.fuzamei.bonuspoint.dao.common.mapper;

import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Q版小李
 * @description
 * @create 2018/8/3 14:26
 */
@Mapper
@Repository
public interface BlockInfoMapper extends TkMapper<BlockInfoPO> {
}
