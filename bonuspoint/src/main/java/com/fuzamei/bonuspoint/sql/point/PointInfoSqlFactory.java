package com.fuzamei.bonuspoint.sql.point;

import com.fuzamei.bonuspoint.constant.PointRecordConstant;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;

/**
 * PointInfo Sql
 *
 * @author liumeng
 * @create 2018年5月7日
 */
@Slf4j
public class PointInfoSqlFactory {

    /**
     * 保存积分信息SQL
     *
     * @param pointInfoPO 积分信息
     * @return SQL
     */
    public String savePointInfo(PointInfoPO pointInfoPO) {
        String sql = new SQL() {{
            INSERT_INTO("bp_point_info");

            if (pointInfoPO.getName() != null) {
                VALUES("name", "'" + pointInfoPO.getName() + "'");
            }
            if (pointInfoPO.getCompany() != null) {
                VALUES("company", pointInfoPO.getCompany().toString());
            }
            if (pointInfoPO.getIssuePlatform() != null) {
                VALUES("issue_platform", pointInfoPO.getIssuePlatform().toString());
            }
            if (pointInfoPO.getMemo() != null) {
                VALUES("memo", "'" + pointInfoPO.getMemo() + "'");
            }
            if (pointInfoPO.getNum() != null) {
                VALUES("num", pointInfoPO.getNum().toString());
            }
            if (pointInfoPO.getNumRemain() != null) {
                VALUES("num_remain", pointInfoPO.getNumRemain().toString());
            }
            if (pointInfoPO.getNumUsed() != null) {
                VALUES("num_used", pointInfoPO.getNumUsed().toString());
            }
            if (pointInfoPO.getStatus() != null) {
                VALUES("status", pointInfoPO.getStatus().toString());
            }
            if (pointInfoPO.getReason() != null) {
                VALUES("reason", "'" + pointInfoPO.getReason() + "'");
            }
            if (pointInfoPO.getIsLife() != null) {
                VALUES("is_life", pointInfoPO.getIsLife().toString());
            }
            if (pointInfoPO.getStartTime() != null) {
                VALUES("start_at", pointInfoPO.getStartTime());
            }
            if (pointInfoPO.getEndTime() != null) {
                VALUES("end_at", pointInfoPO.getEndTime());
            }
            VALUES("created_at", String.valueOf(System.currentTimeMillis()));
            VALUES("updated_at", String.valueOf(System.currentTimeMillis()));
        }}.toString();
        log.info("保存积分信息SQL:\n" + sql);
        return sql;
    }

    /**
     * 更新积分信息
     *
     * @param pointInfoPO 积分信息
     * @return SQL
     */
    public String updatePointInfo(PointInfoPO pointInfoPO) {
        String sql = new SQL() {{
            UPDATE("bp_point_info");
            if (pointInfoPO.getName() != null) {
                SET("name = " + "'" + pointInfoPO.getName() + "'");
            }
            if (pointInfoPO.getCompany() != null) {
                SET("company = " + pointInfoPO.getCompany().toString());
            }
            if (pointInfoPO.getIssuePlatform() != null) {
                SET("issue_platform = " + pointInfoPO.getIssuePlatform().toString());
            }
            if (pointInfoPO.getMemo() != null) {
                SET("memo = " + "'" + pointInfoPO.getMemo() + "'");
            }
            if (pointInfoPO.getNum() != null) {
                SET("num = " + pointInfoPO.getNum().toString());
            }
            if (pointInfoPO.getNumRemain() != null) {
                SET("num_remain = " + pointInfoPO.getNumRemain().toString());
            }
            if (pointInfoPO.getNumUsed() != null) {
                SET("num_used = " + pointInfoPO.getNumUsed().toString());
            }
            if (pointInfoPO.getStatus() != null) {
                SET("status = " + pointInfoPO.getStatus().toString());
            }
            if (pointInfoPO.getReason() != null) {
                SET("reason = " + "'" + pointInfoPO.getReason() + "'");
            }
            if (pointInfoPO.getIsLife() != null) {
                SET("is_life = " + pointInfoPO.getIsLife().toString());
            }
            if (pointInfoPO.getStartTime() != null) {
                SET("start_at = " + pointInfoPO.getStartTime());
            }
            if (pointInfoPO.getEndTime() != null) {
                SET("end_at = " + pointInfoPO.getEndTime());
            }

            SET("updated_at = " + String.valueOf(System.currentTimeMillis()));
            WHERE("id = " + pointInfoPO.getId());
        }}.toString();
        log.info("更新积分信息SQL:\n" + sql);
        return sql;
    }

    /**
     * 查询集团资产信息
     *
     * @param companyId
     * @return
     */
    public String selectCompanyPointAsset(Long companyId) {
        return new SQL() {
            {
                SELECT("id");
                SELECT("uid");
                SELECT("company_name");
                SELECT("amount");
                SELECT("cash_num");
                SELECT("cash_rate");
                SELECT("point_rate");
                SELECT("(select IFNULL(sum(num),0) from bp_point_info where status = 1 and company= " + companyId
                        + " group by company) as sums ");
                SELECT("(select IFNULL(sum(num_remain),0) from bp_point_info where status = 1 and company= " + companyId
                        + " group by company) as numRemains ");
                SELECT("(select IFNULL(sum(num_used),0) from bp_point_info where status = 1 and company= " + companyId
                        + " group by company) as numUseds");
                FROM("bp_company_info");
                WHERE("id = " + companyId);

            }
        }.toString();
    }

    /**
     * 平台查看积分发行审批记录，集团查看积分发行记录
     *
     * @author wangjie
     */
    public String pointIssueList(QueryPointDTO queryPointDTO) {
        SQL sql = new SQL() {
            {
                SELECT("bp_point_info.id");
                SELECT("company_name");
                SELECT("bp_point_info.num");
                SELECT("num_remain");
                SELECT("num_used");
                SELECT("status");
                SELECT("name");
                SELECT("bp_point_info.memo");
                SELECT("reason");
                SELECT("is_life");
                SELECT("start_at");
                SELECT("end_at");
                SELECT("bp_point_record.hash");
                SELECT("bp_point_record.height");
                SELECT("bp_point_info.created_at");
                SELECT("bp_point_info.updated_at");
                FROM("bp_point_info ");
                INNER_JOIN("bp_company_info  on company=bp_company_info.id");
                INNER_JOIN("bp_point_record on bp_point_info.id = bp_point_record.point_id");
                WHERE("bp_point_record.category = "+PointRecordConstant.CATEGORY_GROUP_APPLY );
                if (queryPointDTO.getIssuePlatform() != null) {
                    WHERE(" issue_platform =" + queryPointDTO.getIssuePlatform());
                }
                if (queryPointDTO.getCompany() != null) {
                    WHERE(" company =" + queryPointDTO.getCompany());
                }

                if (queryPointDTO.getStartTime() != null) {
                    WHERE(" bp_point_info.created_at>=" + queryPointDTO.getStartTime());
                }
                if (queryPointDTO.getEndTime() != null) {
                    WHERE(" bp_point_info.created_at<=" + queryPointDTO.getEndTime());
                }
                if (queryPointDTO.getMobile() != null) {
                    WHERE(" company_leader_mobile like'%" + queryPointDTO.getMobile() + "%'");
                }
                if (queryPointDTO.getStatus() != null) {
                    WHERE("status =" + queryPointDTO.getStatus());
                }
                if (StringUtil.isNotBlank(queryPointDTO.getFuzzyMatch())) {
                    WHERE("( point.name like '%" + queryPointDTO.getFuzzyMatch() + "%'"+" or "+"company_name like '%" + queryPointDTO.getFuzzyMatch() + "%')");
                }
                if (StringUtil.isNotBlank(queryPointDTO.getName())){
                    WHERE("point.name like '%" + queryPointDTO.getName() + "%'");
                }


                ORDER_BY("created_at desc");

            }
        };
        log.info("\n------sql-----pointIssueList-----------------\n" + sql.toString());
        return sql.toString();
    }
}
