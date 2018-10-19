package com.fuzamei.bonuspoint.dao.point;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Mapper
@Repository
public interface GeneralPointRelationDao {

    @Select("select count(*) from bp_general_point_relation  where user_id =#{uid} and platform_id = #{platformId} ")
    boolean isGeneralPointRelationExist(@Param("uid") long uid ,@Param("platformId") Long platformId);

    @Insert("insert into bp_general_point_relation (platform_id , user_id,num) values (#{platformId},#{uid},0)")
    int createGeneralPointRelation(@Param("uid") long uid ,@Param("platformId") Long platformId);

    @Update("update bp_general_point_relation set num = num+#{num} where user_id =#{uid}")
    int updateGeneralPointNumByUid(@Param("uid") long uid ,@Param("num") BigDecimal num);
    /**
     * 通用积分增加
     * @param uid    对应用户uid
     * @param num   增加数量
     * @return
     */
    @Update("update bp_general_point_relation set num = num + #{num} where user_id = #{uid}")
    int increaseGeneralPoint(@Param("uid") Long uid,@Param("num") Long num);
    /**
     * 花费通用积分
     * @param uid    对应用户uid
     * @param num   数量
     * @return
     */
    @Update("update bp_general_point_relation set num = num - #{num} where user_id = #{uid}")
    int reduceGeneralPoint(@Param("uid") Long uid,@Param("num") Long num);
}
