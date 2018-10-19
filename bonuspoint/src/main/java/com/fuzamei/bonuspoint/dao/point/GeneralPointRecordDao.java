package com.fuzamei.bonuspoint.dao.point;

import com.fuzamei.bonuspoint.entity.dto.point.PointRecordDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;

import com.fuzamei.bonuspoint.entity.po.point.GeneralPointInfoPO;
import com.fuzamei.bonuspoint.sql.point.GeneralPointRecordSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository
public interface GeneralPointRecordDao   {

    /**
     * 查平台发放积分记录
     * @param queryPointDTO
     * @return
     */
    @SelectProvider(type = GeneralPointRecordSqlFactory.class , method = "grantPointRecordListSQL")
    List<PointRecordDTO> grantPointRecordList(QueryPointDTO queryPointDTO);

    /**
     * 通过平台id获取平台通用积分信息
     * @param platformId
     * @return
     */
    @Select("select * from bp_general_point_info where platform_id = #{platformId}")
    GeneralPointInfoPO getGeneralPointInfoByPlatformId(Long platformId);

    /**
     * 通用积分增加
     * @param uid    对应用户uid
     * @param num   增加数量
     * @return
     */
    @Update("update bp_general_point_relation set num = num + #{num} where user_id = #{uid}")
    int increaseGeneralPoint(@Param("uid") Long uid,@Param("num") BigDecimal num);
    /**
     * 花费通用积分
     * @param uid    对应用户uid
     * @param num   数量
     * @return
     */
    @Update("update bp_general_point_relation set num = num - #{num} where user_id = #{uid}")
    int reduceGeneralPoint(@Param("uid") Long uid, @Param("num") BigDecimal num);
}
