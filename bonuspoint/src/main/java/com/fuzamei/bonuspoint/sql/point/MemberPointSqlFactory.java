package com.fuzamei.bonuspoint.sql.point;

import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.QueryUserDTO;
import com.fuzamei.bonuspoint.entity.po.point.PointPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by 18519 on 2018/5/5.
 * lkm
 */
@Slf4j
public class MemberPointSqlFactory {

    /**
     * 通过集团id与userid查找对应的积分信息
     *lkm
     */
    public String findCompanyPointByUserIdAndCompanyId(Long id, Long groupId) {
        Long time = 0L;
        return new SQL() {{
            SELECT("point_rate,company,bp_point_relation.num AS num,end_at AS endTime,point_id,user_id,num_used,bp_point_info.id");
            FROM("bp_point_info");
            LEFT_OUTER_JOIN("bp_point_relation ON bp_point_info.id = point_id ");
            LEFT_OUTER_JOIN("bp_company_info ON bp_company_info.id = company ");
            WHERE("user_id = "+id+" and company ="+ groupId);
            WHERE("end_at > " + time);
           // WHERE("bp_point_relation.num != 0");
            ORDER_BY("end_at");
        }}.toString();
    }

    /**
     * 修改发送者的集团积分
     *
     */
    public String updatePointByPointIdAndUserId(PointPO pointPO) {
        return new SQL() {{
            UPDATE("bp_point_relation");
            SET("num = #{num}");
            WHERE("user_id = #{userId}");
            WHERE("point_id = #{pointId}");
        }}.toString();
    }



    /**
     * update 接收者的集团积分数据
     * lkm
     * @param pointPO
     * @return
     */
    public String updatePointOpByPointIdAndUserId(PointPO pointPO) {
        return new SQL() {{
            UPDATE("bp_point_relation");
            SET("num = #{num}");
            WHERE("user_id = #{opUserId} and point_id = #{pointId}");
        }}.toString();
    }


    /**
     * 查询集团积分流水
     * @param queryPointDTO
     * @return
     * @wangjie
     */
    public String queryCompanyPointRecord(QueryPointDTO queryPointDTO){
        SQL sql = new SQL(){
            {
                SELECT("*");
                FROM("bp_point_record");
                WHERE("uid="+queryPointDTO.getUid());
                if (queryPointDTO.getType() != null){
                    WHERE("type ="+queryPointDTO.getType());
                }

                if (queryPointDTO.getStartTime()!=null){
                    WHERE("created_at>="+queryPointDTO.getStartTime());
                }
                if (queryPointDTO.getEndTime()!=null){
                    WHERE("created_at<="+queryPointDTO.getEndTime());
                }
                ORDER_BY("created_at desc");
            }
        };
        log.info("\n------sql-----queryMemberPointRecord-----------------\n"+sql.toString());
        return sql.toString();
    }

    /**
     * 查询通用积分流水
     * @param queryPointDTO
     * @return
     */
    public String queryGeneralPointRecord(QueryPointDTO queryPointDTO){
        SQL sql = new SQL(){
            {
                SELECT("*");
                FROM("bp_general_point_record");
                WHERE("uid="+queryPointDTO.getUid());
                if (queryPointDTO.getType() != null){
                    WHERE("type ="+queryPointDTO.getType());
                }

                if (queryPointDTO.getStartTime()!=null){
                    WHERE("created_at>="+queryPointDTO.getStartTime());
                }
                if (queryPointDTO.getEndTime()!=null){
                    WHERE("created_at<="+queryPointDTO.getEndTime());
                }
                ORDER_BY("created_at desc");
            }
        };
        log.info("\n------sql-----queryMemberPointRecord-----------------\n"+sql.toString());
        return sql.toString();
    }


    public String getMemberPointInfoListLikeMobile(QueryUserDTO queryUserDTO){
        SQL sql = new SQL(){
            {
                SELECT("bp_user.id  ");
                SELECT("public_key ");
                SELECT("bp_user.mobile ");
                SELECT("t.total_company_point ");
                SELECT("bp_general_point_relation.num total_general_point");
                //SELECT("join_at ");
                SELECT("bp_user.created_at ");
                FROM("bp_user ");
                LEFT_OUTER_JOIN("bp_general_point_relation ON bp_user.id = bp_general_point_relation.user_id");
                //INNER_JOIN("bp_company_member ON bp_user.id = bp_company_member.member_id");
                INNER_JOIN("( SELECT user_id, SUM( CASE bp_point_info.`status` WHEN 1 THEN bp_point_relation.num ELSE 0 END) total_company_point " +
                        " FROM bp_point_info INNER JOIN bp_point_relation ON bp_point_info.id=bp_point_relation.point_id " +
                        " WHERE company = #{companyId} GROUP BY user_id ) " +
                        " AS t ON t.user_id = bp_user.id ");
                if (queryUserDTO.getMobile()!=null){
                    WHERE("bp_user.mobile like '%"+queryUserDTO.getMobile()+"%' ");
                }
                //WHERE("bp_user.id = bp_company_member.member_id");
                //WHERE(" bp_company_member.company_id = #{companyId}");


            }
        };
        log.info("\n------sql-----getMemberPointInfoListLikeMobile-----------------\n"+sql.toString());
        return sql.toString();
    }



}
