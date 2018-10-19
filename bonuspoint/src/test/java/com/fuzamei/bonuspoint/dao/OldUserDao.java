package com.fuzamei.bonuspoint.dao;

import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OldUserDao {

    @Select("select * from ld_user where id = #{id}")
    AccountPO getUserById(Long id);
}
