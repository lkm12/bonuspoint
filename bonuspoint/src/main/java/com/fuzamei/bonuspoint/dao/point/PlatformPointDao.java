/**
 * FileName: PlatformDao
 * Author: wangtao
 * Date: 2018/5/2 17:25
 * Description:
 */
package com.fuzamei.bonuspoint.dao.point;

import com.fuzamei.bonuspoint.entity.po.point.PointPO;
import com.fuzamei.bonuspoint.sql.point.PlatformPointSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author wangtao
 * @create 2018/5/2
 */
@Mapper
@Repository
public interface PlatformPointDao {

    /**
     * 修改积分信息
     *
     * @param pointPO 积分信息类
     * @return 0 or 1
     */
    @InsertProvider(type = PlatformPointSqlFactory.class, method = "updatePointInfo")
    int updatePointInfo(PointPO pointPO);


    /**
     * 判断用户是否是平台的管理人员（单平台）
     * @param uid 用户id
     * @return
     */
    @Select("SELECT count(*) FROM bp_user WHERE id=#{uid} AND role =1")
    Boolean isPlatformUser(Long uid);

    /**
     * 平台拒绝发放积分
     * @param id 申请id
     * @param reason 拒绝原因
     * @return
     */
    @Update("UPDATE bp_point_info SET `STATUS` = 2 , " +
            "reason = #{reason} , updated_at =  unix_timestamp(now()) * 1000  " +
            "WHERE id = #{id} ")
    int refusePoint(@Param("id") Long id, @Param("reason") String reason);

    /**
     * 同意发放积分
     * @param id 记录id
     * @return
     */
    @Update("UPDATE bp_point_info SET `STATUS` = 1 , num_remain = num, " +
            " updated_at =  unix_timestamp(now()) * 1000" +
            " WHERE id = #{id}")
    int reviewPoint(Long id);
}
