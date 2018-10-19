package com.fuzamei.bonuspoint.dao;

import com.fuzamei.bonuspoint.entity.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OldGeneralPointRecordDao {

    @Select("SELECT t1.id,bp_company_info.id company_id, t1.uid, num_new  FROM ld_general_point_record  as t1 left join bp_company_info  on t1.uid = bp_company_info.uid WHERE t1.created_at = ( SELECT max( created_at ) FROM ld_general_point_record  WHERE t1.uid = uid )")
    List<Point> getAllUserHasGeneralPoint();
}
