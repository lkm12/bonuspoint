package com.fuzamei.bonuspoint.sql.point;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRelationPO;

/**
 * bp_general_point_relation sql 工厂类
 *
 * @author liumeng
 * @create 2018年5月9日
 */
@Slf4j
public class GeneralPointRalationSqlFactory {
    /**
     * 添加通用积分管关联SQL
     *
     * @param generalPointRelationPO 数据传输类
     * @return SQL
     */
    public String saveGeneralPointRalation(GeneralPointRelationPO generalPointRelationPO) {
        String sql = new SQL() {
            {
                INSERT_INTO("bp_general_point_relation");
                if (generalPointRelationPO.getPlatformId() != null) {
                    VALUES("platform_id", generalPointRelationPO.getPlatformId().toString());
                }
                if (generalPointRelationPO.getUserId() != null) {
                    VALUES("user_id", generalPointRelationPO.getUserId().toString());
                }
                if (generalPointRelationPO.getNum() != null) {
                    VALUES("num", generalPointRelationPO.getNum().toString());
                }
            }
        }.toString();
        log.info("添加通用积分管关联SQL:\n" + sql);
        return sql;
    }

    /**
     * 更新通用积分信息SQL
     *
     * @param generalPointRelationPO 数据传输类
     * @return SQL
     */
    public String updateGeneralPointRalation(GeneralPointRelationPO generalPointRelationPO) {
        String sql = new SQL() {
            {
                UPDATE("bp_general_point_relation");
                if (generalPointRelationPO.getPlatformId() != null) {
                    SET("platform_id = " + generalPointRelationPO.getPlatformId());
                }
                if (generalPointRelationPO.getUserId() != null) {
                    SET("user_id = " + generalPointRelationPO.getUserId());
                }
                if (generalPointRelationPO.getNum() != null) {
                    SET("num = " + generalPointRelationPO.getNum());
                }
                WHERE("id = " + generalPointRelationPO.getId());
            }
        }.toString();
        log.info("更新通用积分信息SQL:\n" + sql);
        return sql;
    }
}
