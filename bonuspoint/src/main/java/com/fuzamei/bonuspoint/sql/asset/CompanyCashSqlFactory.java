package com.fuzamei.bonuspoint.sql.asset;

import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import com.fuzamei.bonuspoint.constant.CashRecordConstant;
import com.fuzamei.bonuspoint.constant.GoodOrderConstant;
import com.fuzamei.bonuspoint.constant.GoodStatusConstant;
import com.fuzamei.bonuspoint.entity.dto.asset.CompanyCashRecordDTO;

/**
 * @author wangtao
 * @description
 * @create 2018/4/18
 */
@Slf4j
public class CompanyCashSqlFactory {

    /**
     * @param companyCashRecordDTO 充值记录分页查询数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取单个集团备付金充值记录SQL
     */
    public String getRechargeCashRecord(CompanyCashRecordDTO companyCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("r.hash AS hash");
            SELECT("r.height AS height");
            SELECT("r.id AS id");
            SELECT("r.amount AS amount");
            SELECT("r.status AS status");
            SELECT("r.created_at AS createTime");
            SELECT("r.updated_at AS updateTime");
            SELECT("i.company_name AS companyName");
            FROM("bp_cash_record r");
            INNER_JOIN("bp_company_info i ON i.uid = r.uid");
            if (companyCashRecordDTO.getStartTime() != null) {
                WHERE("r.created_at >= " + companyCashRecordDTO.getStartTime());
            }
            if (companyCashRecordDTO.getEndTime() != null) {
                WHERE("r.updated_at <= " + companyCashRecordDTO.getEndTime());
            }
            WHERE("r.category = " + CashRecordConstant.RECHARGE_CASH);
            WHERE("r.uid = " + companyCashRecordDTO.getUid());
            ORDER_BY("r.created_at desc");
        }}.toString();
        log.info("获取单个集团备付金充值记录SQL:\n" + sql);
        return sql;
    }

    /**
     * @param companyCashRecordDTO 提现记录分页查询数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取单个集团提现记录SQL
     */
    public String getWithdrawCashRecord(CompanyCashRecordDTO companyCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("r.hash AS hash");
            SELECT("r.height AS height");
            SELECT("r.id AS id");
            SELECT("r.amount AS amount");
            SELECT("r.status AS status");
            SELECT("r.created_at AS createTime");
            SELECT("r.updated_at AS updateTime");
            SELECT("i.company_name AS companyName");
            FROM("bp_cash_record r");
            INNER_JOIN("bp_company_info i ON i.uid = r.uid");
            if (companyCashRecordDTO.getStartTime() != null) {
                WHERE("r.created_at >= " + companyCashRecordDTO.getStartTime());
            }
            if (companyCashRecordDTO.getEndTime() != null) {
                WHERE("r.updated_at <= " + companyCashRecordDTO.getEndTime());
            }
            WHERE("r.category = " + CashRecordConstant.WITHDRAW_CASH);
            WHERE("r.uid = " + companyCashRecordDTO.getUid());
            ORDER_BY("r.created_at desc");
        }}.toString();
        log.info("获取单个集团提现记录SQL:\n" + sql);
        return sql;
    }

    /**
     * @param uid 用户id
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取单个集团资产信息SQL
     */
    public String getCompanyCashRecord(Long uid) {
        String sql = new SQL() {{
            SELECT("(SELECT COALESCE(SUM(num),0) " +
                    "FROM bp_company_info c " +
                    "LEFT JOIN bp_point_info p " +
                    "ON c.id = p.company " +
                    "WHERE c.uid = " + uid +
                    " AND (p.status = " + CashRecordConstant.CHECK_PASS +
                    " OR p.status =" + CashRecordConstant.OUT_OF_DATE + "))" +
                    "AS num");
            SELECT("(SELECT COALESCE(SUM(num_remain),0) " +
                    "FROM bp_company_info c " +
                    "LEFT JOIN bp_point_info p " +
                    "ON c.id = p.company " +
                    "WHERE c.uid = " + uid +
                    " AND p.status = " + CashRecordConstant.CHECK_PASS + ")" +
                    "AS numRemain");
            SELECT("(SELECT COALESCE(SUM(num_used),0) " +
                    "FROM bp_company_info c " +
                    "LEFT JOIN bp_point_info p " +
                    "ON c.id = p.company " +
                    "WHERE c.uid = " + uid +
                    " AND (p.status = " + CashRecordConstant.CHECK_PASS +
                    " OR p.status =" + CashRecordConstant.OUT_OF_DATE + "))" +
                    "AS numUsed");
            SELECT("(SELECT COALESCE(SUM(num),0) - COALESCE(SUM(num_remain),0) " +
                    "FROM bp_company_info c " +
                    "LEFT JOIN bp_point_info p " +
                    "ON c.id = p.company " +
                    "WHERE c.uid = " + uid +
                    " AND (p.status = " + CashRecordConstant.CHECK_PASS +
                    " OR p.status =" + CashRecordConstant.OUT_OF_DATE + "))" +
                    "AS numSend");
            SELECT("(SELECT COALESCE(SUM(num),0) - COALESCE(SUM(num_remain),0) - COALESCE(SUM(num_used),0) " +
                    "FROM bp_company_info c " +
                    "LEFT JOIN bp_point_info p " +
                    "ON c.id = p.company " +
                    "WHERE c.uid = " + uid +
                    " AND p.status = " + CashRecordConstant.CHECK_PASS + ") " +
                    "AS numOutside");
            SELECT("(SELECT COALESCE(SUM(num),0) " +
                    "FROM bp_general_point_record  " +
                    "WHERE uid = " + uid +
                    " AND type = " + GeneralPointRecordConstant.POINT_SUB +
                    " AND category = " + GeneralPointRecordConstant.CATEGORY_COMPANY_SETTLE + ") " +
                    " AS balanceNum");
            SELECT("COALESCE(p.num,0) AS pointNum");
            SELECT("COALESCE(c.amount,0) AS amount");
            SELECT("(SELECT COALESCE(SUM(amount),0) " +
                    "FROM bp_cash_record " +
                    "WHERE uid = " + uid +
                    " AND type = " + CashRecordConstant.COST_ASSETS +
                    " AND (status = " + CashRecordConstant.CHECK_PASS +
                    " OR status = " + CashRecordConstant.SUCCESS + ")) " +
                    "AS amountPay");
            SELECT("c.cash_rate AS cashRate");
            SELECT("(SELECT COALESCE(SUM(amount),0) " +
                    "FROM bp_cash_record " +
                    "WHERE uid = " + uid +
                    " AND type = " + CashRecordConstant.INCOME_ASSETS +
                    " AND category = " + CashRecordConstant.RECHARGE_CASH +
                    " AND (status = " + CashRecordConstant.CHECK_PASS +
                    " OR status = " + CashRecordConstant.SUCCESS + ")) " +
                    "AS amountRecharge");
            SELECT("(SELECT COALESCE(SUM(amount),0) " +
                    "FROM bp_cash_record " +
                    "WHERE uid = " + uid +
                    " AND type = " + CashRecordConstant.COST_ASSETS +
                    " AND category = " + CashRecordConstant.WITHDRAW_CASH +
                    " AND (status = " + CashRecordConstant.CHECK_PASS +
                    " OR status = " + CashRecordConstant.SUCCESS + ")) " +
                    "AS amountWithdraw");
            SELECT("(SELECT COALESCE(COUNT(*),0) " +
                    "FROM bp_company_info i " +
                    "LEFT JOIN bp_good g " +
                    "ON g.gid = i.id " +
                    "WHERE i.uid = " + uid +
                    " AND g.status = " + GoodStatusConstant.SALE + ") " +
                    "AS goodNum");
            SELECT("(SELECT COALESCE(COUNT(*),0) " +
                    "FROM bp_company_info i " +
                    "LEFT JOIN bp_good_orders o " +
                    "ON o.gid = i.id " +
                    "WHERE i.uid=" + uid +
                    " AND o.status = " + GoodOrderConstant.GOOD_SUCCESSED_TRADE +
                    " AND to_days(FROM_UNIXTIME(o.created_at/1000)) = to_days(now())) " +
                    "AS goodNumToday");
            SELECT("(SELECT COALESCE(COUNT(*),0) " +
                    "FROM bp_company_info i " +
                    "LEFT JOIN bp_good_orders o " +
                    "ON o.gid = i.id " +
                    "WHERE i.uid = " + uid +
                    " AND o.status = " + GoodOrderConstant.GOOD_SUCCESSED_TRADE +
                    " AND DATE_FORMAT(FROM_UNIXTIME(o.created_at/1000), '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m')) " +
                    "AS goodNumMonth");
            FROM("bp_company_info c");
            LEFT_OUTER_JOIN("bp_general_point_relation p ON c.uid = p.user_id");
            WHERE("c.uid = " + uid);
            GROUP_BY("c.uid");
            GROUP_BY("p.num");
        }}.toString();
        log.info("获取单个集团资产信息SQL:\n" + sql);
        return sql;
    }

    /**
     * @param uid 用户id
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取备付金信息SQL
     */
    public String getProvisionsCashRecord(Long uid) {
        String sql = new SQL() {{
            SELECT("amount");
            SELECT("cash_rate");
            FROM("bp_company_info");
            if (uid != null) {
                WHERE("uid = " + uid);
            }
        }}.toString();
        log.info("获取备付金信息SQL:\n" + sql);
        return sql;
    }

}
