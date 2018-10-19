package com.fuzamei.bonuspoint.sql.point;

import com.fuzamei.bonuspoint.entity.po.point.PointPO;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/4 17:57
 */
@Slf4j
public class PlatformPointSqlFactory {
    
    public String updatePointInfo(PointPO pointPO) {
        return new SQL() {{
            UPDATE("bp_point_info");
            if (pointPO.getName() != null) {
                SET("name = '" + pointPO.getName() + "'");
            }
            if (pointPO.getCompany() != null) {
                SET("company = '" + pointPO.getCompany() + "'");
            }
            if (pointPO.getPlatform() != null) {
                SET("issue_platform = '" + pointPO.getPlatform() + "'");
            }
            if (pointPO.getMemo() != null) {
                SET("memo = '" + pointPO.getMemo() + "'");
            }
            if (pointPO.getNum() != null) {
                SET("num = '" + pointPO.getNum() + "'");
            }
            if (pointPO.getNumSend() != null) {
                SET("num_send = '" + pointPO.getNumSend() + "'");
            }
            if (pointPO.getNumUsed() != null) {
                SET("num_used = '" + pointPO.getNumUsed() + "'");
            }
            if (pointPO.getStatus() != null) {
                SET("status = '" + pointPO.getStatus() + "'");
            }
            if (pointPO.getReason() != null) {
                SET("reason = '" + pointPO.getReason() + "'");
            }
            if (pointPO.getIsLife() != null) {
                SET("is_life = '" + pointPO.getIsLife() + "'");
            }
            if (pointPO.getStartTime() != null) {
                SET("start_at = '" + pointPO.getStartTime() + "'");
            }
            if (pointPO.getEndTime() != null) {
                SET("end_at = '" + pointPO.getEndTime() + "'");
            }
            if (pointPO.getCreateTime() != null) {
                SET("created_at = '" + pointPO.getCreateTime() + "'");
            }
            if (pointPO.getUpdateTime() != null) {
                SET("updated_at = '" + pointPO.getUpdateTime() + "'");
            }
            WHERE("id = '" + pointPO.getPointId() + "'");
        }}.toString();
    }

}
