package com.fuzamei.bonuspoint.sql.asset;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;


/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@Slf4j
public class MemberCashSqlFactory {

    /**
     * @param uid 会员id
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取单个会员资产信息SQL
     */
    public String getMemberCashRecord(Long uid) {
        String sql = new SQL() {{
            SELECT("i.cash_rate AS cashRate");
            SELECT("COALESCE(SUM(p.num),0) + COALESCE(g.num,0) AS pointNum");
            SELECT("COALESCE(SUM(p.num),0) AS pointTotal");
            SELECT("COALESCE(g.num,0) AS generalPoint");
            SELECT("u.id AS memberId");
            SELECT("u.mobile AS mobile");
            SELECT("u.public_key AS address");
            SELECT("COALESCE((SELECT username FROM bp_user WHERE id = r.p_id),'无') AS referee");
            FROM("bp_user u");
            LEFT_OUTER_JOIN("bp_platform_info i ON i.id = u.p_id");
            LEFT_OUTER_JOIN("bp_point_relation p ON p.user_id = u.id");
            LEFT_OUTER_JOIN("bp_general_point_relation g ON g.user_id = u.id");
            LEFT_OUTER_JOIN("bp_relation r ON r.uid = u.id");
            WHERE("u.id = " + uid);
            GROUP_BY("u.id");
            GROUP_BY("r.p_id");
            GROUP_BY("g.num");
        }}.toString();
        log.info("获取单个会员资产信息SQL:\n" + sql);
        return sql;
    }

    /**
     * @param uid 会员id
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表SQL
     */
    public String listMemberPointCashRecord(Long uid) {
        String sql = new SQL() {{
            SELECT("(SELECT COALESCE(SUM(num),0) " +
                    "FROM bp_point_relation " +
                    "WHERE user_id = " + uid + ") " +
                    "AS pointTotal");
        }}.toString();
        log.info("获取会员用户集团积分列表SQL:\n" + sql);
        return sql;
    }

    /**
     * @param uid 会员id
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列详情SQL
     */
    public String getMemberPointCashRecordDetail(Long uid) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("bp_cash_record");
            WHERE("type" + '=' + '6');
        }}.toString();
        log.info("获取会员用户集团积分列详情SQL:\n" + sql);
        return sql;
    }

}
