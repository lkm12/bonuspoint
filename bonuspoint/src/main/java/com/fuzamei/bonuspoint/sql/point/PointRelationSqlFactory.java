package com.fuzamei.bonuspoint.sql.point;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.po.point.PointRelationPO;

/**
 * 积分关联dao
 *
 * @author liumeng
 * @create 2018年5月9日
 */
@Slf4j
public class PointRelationSqlFactory {

    /**
     * 保存用户积分关联信息SQL
     *
     * @param pointRelationPO 数据传输类
     * @return SQL
     */
    public String savePointRelation(PointRelationPO pointRelationPO) {
        String sql = new SQL() {
            {
                INSERT_INTO("bp_point_record");
                if (pointRelationPO.getPointId() != null) {
                    VALUES("point_id", "'" + pointRelationPO.getPointId() + "'");
                }
                if (pointRelationPO.getUserId() != null) {
                    VALUES("user_id", "'" + pointRelationPO.getUserId() + "'");
                }
                if (pointRelationPO.getNum() != null) {
                    VALUES("num", "'" + pointRelationPO.getNum() + "'");
                }
            }
        }.toString();
        log.info("保存用户积分关联信息SQL:\n" + sql);
        return sql;
    }

    /**
     * 更新用户积分关联信息SQL
     *
     * @param pointRelationPO 数据传输类
     * @return SQL
     */
    public String updatePointRelation(PointRelationPO pointRelationPO) {
        String sql = new SQL() {
            {
                UPDATE("bp_point_relation");
                if (pointRelationPO.getPointId() != null) {
                    SET("point_id=" + pointRelationPO.getPointId());
                }
                if (pointRelationPO.getUserId() != null) {
                    SET("user_id=" + pointRelationPO.getUserId());
                }
                if (pointRelationPO.getNum() != null) {
                    SET("num=" + pointRelationPO.getNum());
                }
                WHERE("id=" + pointRelationPO.getId());
            }
        }.toString();
        log.info("更新用户积分关联信息SQL:\n" + sql);
        return sql;
    }

    /**
     * 查询用户可用总积分
     *
     * @param uid       用户id
     * @param companyId 集团id
     * @return SQL
     */
    public String queryPointTotal(Long uid, Long companyId) {
        return new SQL() {
            {
                SELECT("ifnull(sum(relation.num),0) as pointTotal");
                FROM("bp_point_relation as relation ,bp_point_info as point");
                WHERE("point.id = relation.point_id");
                WHERE("point.status = " + PointInfoConstant.STATUS_CHECKED);
                WHERE("relation.user_id = " + uid);
                WHERE("point.company = " + companyId);
                GROUP_BY("relation.user_id");
            }
        }.toString();
    }

    /**
     * 查询用户在冒个集团所有可用积分（按过期时间顺序排序）
     *
     * @param uid
     * @param companyId
     * @return
     */
    public String listPointOrders(Long uid, Long companyId) {
        return new SQL() {
            {
                SELECT("relation.id ,relation.point_id,relation.user_id,relation.num,"
                        + "point.company,point.is_life,point.start_at,point.end_at,"
                        + "company.uid as companyUid,company.point_rate");
                FROM("bp_point_relation as relation ,bp_point_info as point,bp_company_info as company");
                WHERE("point.id = relation.point_id");
                WHERE("point.company = company.id");
                WHERE("point.status = " + PointInfoConstant.STATUS_CHECKED);
                WHERE("relation.user_id = " + uid);
                WHERE("point.company = " + companyId);
                ORDER_BY("point.is_life desc");
                ORDER_BY("point.end_at asc");
                ORDER_BY("relation.num asc");
            }
        }.toString();
    }

    /**
     * 用户查看持有集团积分记录
     *
     * @param queryPointDTO
     * @return
     * @author wangjie
     */
    public String queryCompanyPointRelation(QueryPointDTO queryPointDTO) {
        SQL sql = new SQL() {
            {
                SELECT("company companyId");
                SELECT("company_name");
                SELECT("public_key");
                SELECT("name");
                SELECT("logo_url");
                SELECT("point_rate");
                SELECT("bp_point_relation.num");
                SELECT("bp_point_info.status");

                FROM("bp_point_relation");
                INNER_JOIN("bp_point_info on point_id = bp_point_info.id ");

                INNER_JOIN("bp_company_info on company = bp_company_info.id ");

                INNER_JOIN("bp_user on bp_company_info.uid = bp_user.id");


                WHERE("user_id = " + queryPointDTO.getUid());
            }
        };
        log.info("\n------sql-----queryCompanyPointRelation-----------------\n" + sql.toString());
        return sql.toString();
    }
}
