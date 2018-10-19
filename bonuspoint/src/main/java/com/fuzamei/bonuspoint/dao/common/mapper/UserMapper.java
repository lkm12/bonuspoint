package com.fuzamei.bonuspoint.dao.common.mapper;

import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Q版小李
 * @description
 * @create 2018/8/2 17:14
 */
@Mapper
@Repository
public interface UserMapper extends TkMapper<AccountPO>  {
}
