package com.fuzamei.bonuspoint.dao.common.mapper;

import com.fuzamei.bonuspoint.entity.po.user.InvitePO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * lkm
 */
@Mapper
@Repository
public interface InviteMapper extends TkMapper<InvitePO>{
}
