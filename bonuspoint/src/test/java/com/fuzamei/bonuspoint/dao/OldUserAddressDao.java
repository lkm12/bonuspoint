package com.fuzamei.bonuspoint.dao;

import com.fuzamei.bonuspoint.entity.po.user.UserAddressPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OldUserAddressDao {

    @Select("select * from ld_user_address")
    List<UserAddressPO> getAllAddress();
}
