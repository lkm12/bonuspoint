package com.fuzamei.bonuspoint.sql.asset;

import com.fuzamei.bonuspoint.constant.CashRecordConstant;
import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.entity.dto.asset.PlatformCashRecordDTO;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@Slf4j
public class PlatformCashSqlFactory {

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取会员用户资产信息SQL
     */
    public String listMemberCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("COALESCE(SUM(p.num),0) AS pointTotal");
            SELECT("g.num AS generalPoint");
            SELECT("u.id AS memberId");
            SELECT("u.mobile AS mobile");
            SELECT("u.public_key AS address");
            SELECT("COALESCE((SELECT username FROM bp_user WHERE id = r.p_id),'无') AS referee");
            FROM("bp_user u");
            LEFT_OUTER_JOIN("bp_point_relation p ON p.user_id = u.id");
            LEFT_OUTER_JOIN("bp_general_point_relation g ON g.user_id = u.id");
            LEFT_OUTER_JOIN("bp_relation r ON r.uid = u.id");
            WHERE("u.role = " + Roles.MEMBER);
            if (platformCashRecordDTO.getMobile() != null) {
                WHERE("u.username = " + platformCashRecordDTO.getMobile());
            }
            GROUP_BY("u.id");
            GROUP_BY("r.p_id");
            GROUP_BY("g.num");
        }}.toString();
        log.info("获取会员用户资产信息SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取集团资产信息SQL
     */
    public String listCompanyCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("c.id AS companyId");
            SELECT("c.company_name AS companyName");
            SELECT("c.cash_rate AS cashRate");
            SELECT("COALESCE(c.amount,0) AS amount");
            SELECT("COALESCE(COALESCE(SUM(p.num_remain),0)/SUM(p.num),0) AS numRate");
            SELECT("COALESCE(SUM(p.num_remain),0) AS numRemain");
            SELECT("COALESCE(SUM(p.num)-COALESCE(SUM(p.num_remain),0)-COALESCE(SUM(p.num_used),0),0) AS numOutside");
            SELECT("COALESCE(g.num,0) AS generalPoint");
            SELECT("COALESCE(g.num,0)*i.cash_rate AS amountExchange");
            FROM("bp_company_info c");
            LEFT_OUTER_JOIN("bp_point_info p ON p.company = c.id");
            LEFT_OUTER_JOIN("bp_general_point_relation g ON g.user_id = c.id");
            LEFT_OUTER_JOIN("bp_user u ON u.id = c.uid");
            LEFT_OUTER_JOIN("bp_platform_info i ON i.uid = u.p_id");
            if (platformCashRecordDTO.getCompanyName() != null) {
                WHERE("company_name LIKE " + "'%" + platformCashRecordDTO.getCompanyName() + "%'");
            }
            GROUP_BY("c.id");
            GROUP_BY("g.num");
            GROUP_BY("i.cash_rate");
        }}.toString();
        log.info("获取集团资产信息列表SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取集团充值记录SQL
     */
    public String getRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("r.hash AS hash");
            SELECT("r.height AS height");
            SELECT("i.company_name AS companyName");
            SELECT("r.id AS id");
            SELECT("r.status AS status");
            SELECT("r.amount AS amount");
            SELECT("r.created_at AS createTime");
            SELECT("r.updated_at AS updateTime");
            FROM("bp_company_info i");
            LEFT_OUTER_JOIN("bp_cash_record r ON r.uid = i.uid");
            WHERE("r.category = " + CashRecordConstant.RECHARGE_CASH);
            WHERE("r.type = " + CashRecordConstant.INCOME_ASSETS);
            if (platformCashRecordDTO.getCompanyName() != null) {
                WHERE("i.company_name LIKE " + "'%" + platformCashRecordDTO.getCompanyName() + "%'");
            }
            if (platformCashRecordDTO.getStartTime() != null) {
                WHERE("r.created_at >= " + platformCashRecordDTO.getStartTime());
            }
            if (platformCashRecordDTO.getEndTime() != null) {
                WHERE("r.created_at <=" + platformCashRecordDTO.getEndTime());
            }
            ORDER_BY("r.created_at desc");
        }}.toString();
        log.info("获取集团充值记录SQL:\n" + sql);
        return sql;
    }


    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取集团提现记录SQL
     */
    public String getWithdrawCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("r.hash AS hash");
            SELECT("r.height AS height");
            SELECT("i.company_name AS companyName");
            SELECT("r.id AS id");
            SELECT("r.status AS status");
            SELECT("r.amount AS amount");
            SELECT("r.created_at AS createTime");
            SELECT("r.updated_at AS updateTime");
            FROM("bp_company_info i");
            LEFT_OUTER_JOIN("bp_cash_record r ON r.uid = i.uid");
            WHERE("r.category = " + CashRecordConstant.WITHDRAW_CASH);
            WHERE("r.type = " + CashRecordConstant.COST_ASSETS);
            if (platformCashRecordDTO.getCompanyName() != null) {
                WHERE("i.company_name LIKE " + "'%" + platformCashRecordDTO.getCompanyName() + "%'");
            }
            if (platformCashRecordDTO.getStartTime() != null) {
                WHERE("r.created_at >= " + platformCashRecordDTO.getStartTime());
            }
            if (platformCashRecordDTO.getEndTime() != null) {
                WHERE("r.created_at <=" + platformCashRecordDTO.getEndTime());
            }
            ORDER_BY("r.created_at desc");
        }}.toString();
        log.info("获取集团提现记录SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取备付金充值列表SQL
     */
    public String listRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            log.info("平台获取备付金充值列表sql");
            SELECT("*");
            FROM("bp_cash_record");
            if (platformCashRecordDTO.getStartTime() != null) {
                WHERE("created_at" + '>' + '=' + "'" + platformCashRecordDTO.getStartTime() + "'");
            }
            if (platformCashRecordDTO.getEndTime() != null) {
                WHERE("created_at" + '<' + '=' + "'" + platformCashRecordDTO.getEndTime() + "'");
            }
            ORDER_BY("created_at desc");
        }}.toString();
        log.info("获取备付金充值列表SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取备付金信息SQL
     */
    public String getProvisionsCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("bp_cash_record");
            if (platformCashRecordDTO.getUid() != null) {
                WHERE("id" + '=' + "'" + platformCashRecordDTO.getUid() + "'");
            }
        }}.toString();
        log.info("获取备付金信息SQL:\n" + sql);
        return sql;
    }

    /**
     * @param uid 平台id
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取平台资产信息SQL
     */
    public String getPlatformCashRecord(Long uid) {
        String sql = new SQL() {{
            SELECT("(SELECT COALESCE(SUM(num),0) " +
                    "FROM bp_general_point_record " +
                    "WHERE category=" + GeneralPointRecordConstant.CATEGORY_USER_EXCHANGE + ") " +
                    "AS numExchange");
            SELECT("(SELECT COALESCE(SUM(num),0) " +
                    "FROM bp_general_point_record " +
                    "WHERE category=" + GeneralPointRecordConstant.CATEGORY_COMPANY_SETTLE + ") " +
                    "AS numBalance");
            SELECT("(SELECT COALESCE(SUM(num),0) " +
                    "FROM bp_platform_info i " +
                    "LEFT OUTER JOIN bp_general_point_relation r " +
                    "ON r.platform_id = i.id " +
                    "WHERE i.uid=" + uid + ") " +
                    "AS numOutside");
            SELECT("COALESCE(SUM(c.amount),0) AS amount");
            FROM("bp_platform_info i");
            LEFT_OUTER_JOIN("bp_user u ON u.p_id = i.id");
            LEFT_OUTER_JOIN("bp_company_info c ON c.uid = u.id");
            WHERE("i.uid = " + uid);
        }}.toString();
        log.info("获取平台资产信息SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取集团列表SQL
     */
    public String listCompany(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("bp_cash_record");
        }}.toString();
        log.info("获取集团列表SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取备付金预警通知列表SQL
     */
    public String listProvisionsNotice(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("c.company_name AS companyName");
            SELECT("c.amount AS amount");
            SELECT("c.cash_rate AS cashRate");
            SELECT("c.point_rate AS pointRate");
            SELECT("COALESCE(g.num,0) AS generalPoint");
            SELECT("COALESCE(SUM(p.num_remain),0) AS numRemain");
            SELECT("COALESCE(SUM(p.num)-SUM(p.num_remain)-SUM(p.num_used),0) AS numOutside");
            FROM("bp_company_info c");
            LEFT_OUTER_JOIN("bp_general_point_relation g ON g.user_id = c.uid");
            LEFT_OUTER_JOIN("bp_point_info p ON p.company = c.id");
            WHERE("c.amount <= " + 500);
            WHERE("p.status =" + CashRecordConstant.CHECK_PASS);
            GROUP_BY("c.id");
            GROUP_BY("g.num");
        }}.toString();
        log.info("获取备付金预警通知列表SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表SQL
     */
    public String listMemberPointCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("bp_cash_record");
            if (platformCashRecordDTO.getUid() != null) {
                WHERE("id" + '=' + "'" + platformCashRecordDTO.getUid() + "'");
            }
            WHERE("type" + '=' + '1');
        }}.toString();
        log.info("获取会员用户集团积分列表SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列详情SQL
     */
    public String getMemberPointCashRecordDetail(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("bp_cash_record");
            if (platformCashRecordDTO.getUid() != null) {
                WHERE("id" + '=' + "'" + platformCashRecordDTO.getUid() + "'");
            }
            WHERE("type" + '=' + '3');
        }}.toString();
        log.info("获取会员用户集团积分列详情SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 获取集团备付金比例信息列表SQL
     */
    public String listCompanyRateCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            SELECT("company_name AS companyName");
            SELECT("cash_rate AS cashRate");
            FROM("bp_company_info");
        }}.toString();
        log.info("获取集团比例信息列表SQL:\n" + sql);
        return sql;
    }

    /**
     * @param uid 集团id
     * @return SQL语句
     * @author qbanxiaoli
     * @description 备付金审核通过SQL
     */
    public String checkRechargeCashRecord(Long uid) {
        String sql = new SQL() {{
            UPDATE("bp_cash_record");
            SET("status=" + "'" + CashRecordConstant.CHECK_PASS + "'");
            SET("updated_at=" + "'" + System.currentTimeMillis() + "'");
            WHERE("id=" + "'" + uid + "'");
        }}.toString();
        log.info("备付金审核通过SQL:\n" + sql);
        return sql;
    }

    /**
     * @param platformCashRecordDTO 拒绝信息数据传输类
     * @return SQL语句
     * @author qbanxiaoli
     * @description 备付金审核拒绝SQL
     */
    public String refuseRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO) {
        String sql = new SQL() {{
            UPDATE("bp_cash_record");
            if (platformCashRecordDTO.getStatus() != null) {
                SET("status=" + "'" + platformCashRecordDTO.getStatus() + "'");
            }
            if (StringUtil.isNotBlank(platformCashRecordDTO.getReason())) {
                SET("reason=" + "'" + platformCashRecordDTO.getReason() + "'");
            }
            SET("updated_at=" + "'" + TimeUtil.timestamp() + "'");
            WHERE("id=" + "'" + platformCashRecordDTO.getId() + "'");
        }}.toString();
        log.info("备付金审核拒绝SQL:\n" + sql);
        return sql;
    }

}
