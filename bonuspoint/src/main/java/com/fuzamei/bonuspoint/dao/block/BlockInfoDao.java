package com.fuzamei.bonuspoint.dao.block;

import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-24 18:32
 **/
@Mapper
@Repository
public interface BlockInfoDao extends TkMapper<BlockInfoPO> {

    @Insert("insert into bp_block_info values(null,#{uid},#{operationType},#{height},#{hash},#{createdAt})")
    int insertBlockInfo(BlockInfoPO blockInfoPO);

}
