package com.fuzamei.bonuspoint.sql.point;

import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;
import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/4 17:56
 */
@Slf4j
public class CompanyPointSqlFactory {

    /**
     * 获取通用积分结算信息SQL
     *
     * @param uid 传输参数
     * @return SQL
     */
    public String getBalanceInfoRecord(Long uid) {
        String sql = new SQL() {{
            SELECT("1 AS rate");
            SELECT("c.point_rate AS pointRate");
            SELECT("c.cash_Rate AS cashRate");
            SELECT("COALESCE(SUM(g.num),0) AS num");
            SELECT("u.public_key AS bankPublickey");
            SELECT("(SELECT u.public_key " +
                    "FROM bp_user u " +
                    "INNER JOIN bp_platform_info p " +
                    "ON p.uid=u.id " +
                    "WHERE u.id=" + 1 + ") " +
                    "AS paltformPublickey");
            FROM("bp_user u");
            INNER_JOIN("bp_company_info c ON c.uid=u.id");
            INNER_JOIN("bp_general_point_relation g ON g.user_id=c.uid");
            WHERE("u.id=" + uid);
        }}.toString();
        log.info("获取通用积分结算信息SQL:\n" + sql);
        return sql;
    }

    /**
     * 获取集团已兑换通用积分记录
     *
     * @param companyPointDTO 传输参数
     * @return sql语句
     */
    public String listPointExchangeRecord(CompanyPointDTO companyPointDTO) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("bp_general_point_record");
            WHERE("uid=" + companyPointDTO.getUid());
            WHERE("category=" + GeneralPointRecordConstant.CATEGORY_COMPANY_SETTLE);
        }}.toString();
        log.info("获取集团已兑换通用积分记录SQL:\n" + sql);
        return sql;
    }

    /**
     * 列出集团活动信息
     * @param uid 集团管理UID
     * @param  showOutTime 是否显示过期活动
     * @return
     */
    public String listActivity(Long uid, Boolean showOutTime) {
        String sql = new SQL() {{
            SELECT("point.id AS pointId, point.name AS activityName,point.num,point.num_remain,point.num_used," +
                    "point.is_life, point.start_at, point.end_at");
            FROM("bp_point_info AS point");
            INNER_JOIN("bp_company_info AS company ON point.company = company.id");
            WHERE("company.uid = " + uid.toString());
            if (showOutTime != null && showOutTime) {
                WHERE(" (point.status = 3 or point.status = 1)");
            } else {
                WHERE("  point.status = 1");
            }
            ORDER_BY(" point.created_at DESC");
        }}.toString();
        log.info("列出集团活动信息SQL:\n" + sql);
        return sql;
    }

}
