package com.fuzamei.bonuspoint.dao.authorization;

import com.fuzamei.bonuspoint.entity.dto.authorization.UserTokenDTO;
import com.fuzamei.bonuspoint.entity.po.authorization.UserTokenPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-08 09:59
 **/
@Mapper
@Repository
public interface UserAuthorizationDao {
    @Select("select count(*) from bp_token where token = #{token} and uid = #{uid}")
    int countByTokenAndUid(UserTokenDTO userToken);

    @Insert("insert into bp_token (token , uid ,time) VALUES(#{token},#{uid},#{time})")
    int addToken(UserTokenDTO userToken);

    @Update("update bp_token set token = #{newToken} , time =#{time} where uid =#{uid} and token=#{token}")
    int updateToken(UserTokenDTO userTokenDTO);

    @Select("select * from bp_token where token =#{token} and uid = #{uid}")
    UserTokenPO getUserTokenByTokenAndUid(UserTokenDTO userTokenDTO);

    @Select("select * from bp_token where token like 'Browser_%' and uid =#{uid} ")
    UserTokenPO getBrowserTokenByUid(Long uid);

    @Select("select * from bp_token where token like 'App_%' and uid =#{uid} ")
    UserTokenPO getAppTokenByUid(Long uid);
}
