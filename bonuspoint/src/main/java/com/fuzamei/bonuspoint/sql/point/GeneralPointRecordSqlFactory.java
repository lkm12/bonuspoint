package com.fuzamei.bonuspoint.sql.point;

import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;

/**
 * 通用积分记录sql
 * @author liumeng
 * @create 2018年5月9日
 */
@Slf4j
public class GeneralPointRecordSqlFactory {
    /**
     * 添加通用积分交易记录SQL
     * @param generalPointRecordPO  数据传输类
     * @return SQL
     */
    public String saveGeneralPointRecord(GeneralPointRecordPO generalPointRecordPO) {
        String sql = new SQL() {
            {
                INSERT_INTO("bp_general_point_record");

                if (generalPointRecordPO.getUid() != null) {
                    VALUES("uid", generalPointRecordPO.getUid().toString());
                }
                if (generalPointRecordPO.getOppositeUid() != null) {
                    VALUES("opposite_uid", generalPointRecordPO.getOppositeUid().toString());
                }
                if (generalPointRecordPO.getType() != null) {
                    VALUES("type", generalPointRecordPO.getType().toString());
                }
                if (generalPointRecordPO.getCategory() != null) {
                    VALUES("category", generalPointRecordPO.getCategory().toString());
                }
                if (generalPointRecordPO.getNum() != null) {
                    VALUES("num", generalPointRecordPO.getNum().toString());
                }
                if (generalPointRecordPO.getOrderId() != null) {
                    VALUES("order_id", generalPointRecordPO.getOrderId().toString());
                }
                if (generalPointRecordPO.getMemo() != null) {
                    VALUES("memo", "'" + generalPointRecordPO.getMemo() + "'");
                }
                VALUES("created_at", String.valueOf(System.currentTimeMillis()));
                VALUES("updated_at", String.valueOf(System.currentTimeMillis()));
                
            }
        }.toString();
        log.info("添加通用积分交易记录SQL:\n" + sql);
        return sql;
    }

    /**
     * 更新通用积分交易记录SQL
     * @param generalPointRecordPO  数据传输类
     * @return SQL
     */
    public String updateGeneralPointRecord(GeneralPointRecordPO generalPointRecordPO) {
        String sql = new SQL() {
            {
                UPDATE("bp_general_point_record");
                if (generalPointRecordPO.getUid() != null) {
                    SET("uid = " + generalPointRecordPO.getUid());
                }
                if (generalPointRecordPO.getOppositeUid() != null) {
                    SET("opposite_uid = " + generalPointRecordPO.getOppositeUid());
                }
                if (generalPointRecordPO.getType() != null) {
                    SET("type = " + generalPointRecordPO.getType());
                }
                if (generalPointRecordPO.getCategory() != null) {
                    SET("category = " + generalPointRecordPO.getCategory());
                }
                if (generalPointRecordPO.getNum() != null) {
                    SET("num = " + generalPointRecordPO.getNum());
                }
                if (generalPointRecordPO.getOrderId() != null) {
                    SET("order_id = " + generalPointRecordPO.getOrderId());
                }
                if (generalPointRecordPO.getMemo() != null) {
                    SET("memo = " + "'" + generalPointRecordPO.getMemo() + "'");
                }
                WHERE("id = " + generalPointRecordPO.getId());
                
            }
        }.toString();
        log.info("添加通用积分交易记录SQL:\n" + sql);
        return sql;
    }

/*    ("select id , mobile,public_key , num, memo , created_at , height , hash " +
            " from bp_general_point_record " +
            " inner join bp_user on bp_user.id == bp_general_point_record.opposite_uid " +
            " where bp_general_point_record.uid = #{uid} and type = {type} " +
            " and category = #{category} and mobile like %#{mobile}% " +
            " and created_at > #{startTime} " +
            " and created_at < #{endTime}" )*/

    public String grantPointRecordListSQL(QueryPointDTO queryPointDTO){
        String sql = new SQL() {
            {
                SELECT("bp_general_point_record.id");
                SELECT("mobile");
                SELECT("public_key oppsitePublickey");
                SELECT("num");
                SELECT("memo");
                SELECT("bp_general_point_record.created_at");
                SELECT("height");
                SELECT("hash");
                FROM(" bp_general_point_record ");
                INNER_JOIN(" bp_user on bp_user.id = bp_general_point_record.opposite_uid ");
                WHERE("bp_general_point_record.uid = " + queryPointDTO.getUid());
                WHERE(" type = "+queryPointDTO.getType());
                WHERE(" category = "+queryPointDTO.getCategory());
                if (queryPointDTO.getMobile()!=null){
                    WHERE(" mobile like '%"+queryPointDTO.getMobile()+"%' ");
                }
                if (queryPointDTO.getStartTime()!=null){
                    WHERE("bp_general_point_record.created_at >"+queryPointDTO.getStartTime());
                }
                if (queryPointDTO.getEndTime()!=null){
                    WHERE("bp_general_point_record.created_at <"+queryPointDTO.getEndTime());
                }
                ORDER_BY("bp_general_point_record.created_at desc");

            }
        }.toString();
        log.info("添加通用积分交易记录SQL:\n" + sql);
        return sql;
    }

}
