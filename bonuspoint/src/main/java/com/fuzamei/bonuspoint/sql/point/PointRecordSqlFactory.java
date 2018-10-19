package com.fuzamei.bonuspoint.sql.point;

import com.fuzamei.bonuspoint.constant.PointRecordConstant;
import com.fuzamei.bonuspoint.entity.dto.point.PointRecordDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointSendDTO;
import com.fuzamei.bonuspoint.entity.po.point.PointRecordPO;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PointRecordSqlFactory {

    /**
     * 保存积分记录SQL
     *
     * @param pointRecordPO 数据传输类
     * @return SQL
     */
    public String savePointRecord(PointRecordPO pointRecordPO) {
        String sql = new SQL() {{
            INSERT_INTO("bp_point_record");
            if (pointRecordPO.getUid() != null) {
                VALUES("uid", "'" + pointRecordPO.getUid() + "'");
            }
            if (pointRecordPO.getOppositeUid() != null) {
                VALUES("opposite_uid", "'" + pointRecordPO.getOppositeUid() + "'");
            }
            if (pointRecordPO.getType() != null) {
                VALUES("type", "'" + pointRecordPO.getType() + "'");
            }
            if (pointRecordPO.getCategory() != null) {
                VALUES("category", "'" + pointRecordPO.getCategory() + "'");
            }
            if (pointRecordPO.getPointId() != null) {
                VALUES("point_id", "'" + pointRecordPO.getPointId() + "'");
            }
            if (pointRecordPO.getPointRate() != null) {
                VALUES("point_rate", "'" + pointRecordPO.getPointRate() + "'");
            }
            if (pointRecordPO.getNum() != null) {
                VALUES("num", "'" + pointRecordPO.getNum() + "'");
            }
            if (pointRecordPO.getOrderId() != null) {
                VALUES("order_id", "'" + pointRecordPO.getOrderId() + "'");
            }
            if (pointRecordPO.getMemo() != null) {
                VALUES("memo", pointRecordPO.getMemo());
            }
            VALUES("created_at", "'" + TimeUtil.timestamp() + "'");
            VALUES("updated_at", "'" + TimeUtil.timestamp() + "'");
        }}.toString();
        log.info("保存积分记录SQL:\n" + sql);
        return sql;
    }

    /**
     * 更新积分记录SQL
     *
     * @param pointRecordPO 数据传输类
     * @return SQL
     */
    public String updatePointRecord(PointRecordPO pointRecordPO) {
        String sql = new SQL() {
            {
                UPDATE("bp_point_record");
                if (pointRecordPO.getUid() != null) {
                    SET("uid = " + pointRecordPO.getUid().toString());
                }
                if (pointRecordPO.getOppositeUid() != null) {
                    SET("opposite_uid = " + pointRecordPO.getOppositeUid().toString());
                }
                if (pointRecordPO.getType() != null) {
                    SET("type = " + pointRecordPO.getType().toString());
                }
                if (pointRecordPO.getCategory() != null) {
                    SET("category = " + pointRecordPO.getCategory().toString());
                }
                if (pointRecordPO.getPointId() != null) {
                    VALUES("point_id", pointRecordPO.getPointId().toString());
                }
                if (pointRecordPO.getPointRate() != null) {
                    SET("point_rate = " + pointRecordPO.getPointRate().toString());
                }
                if (pointRecordPO.getNum() != null) {
                    SET("num = " + pointRecordPO.getNum().toString());
                }
                if (pointRecordPO.getOrderId() != null) {
                    SET("order_id = " + pointRecordPO.getOrderId().toString());
                }
                if (pointRecordPO.getMemo() != null) {
                    SET("memo = " + "'" + pointRecordPO.getMemo() + "'");
                }
                SET("updated_at = " + String.valueOf(System.currentTimeMillis()));
                WHERE("id = " + pointRecordPO.getId());
            }
        }.toString();
        log.info("更新积分记录SQL:\n" + sql);
        return sql;
    }

    /**
     * 查询集团发送积分
     *
     * @param queryPointSendDTO 查询条件
     * @return SQL
     */
    public String querySendPoint(QueryPointSendDTO queryPointSendDTO) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("record.id");
                columns.add("record.opposite_uid");
                columns.add("user.username");
                columns.add("user.nickname");
                columns.add("record.type");
                columns.add("record.category");
                columns.add("record.point_id");
                columns.add("point.name as point_name");
                columns.add("point.memo as point_memo");
                columns.add("point.memo as point_memo");
                columns.add("point.is_life as point_is_life");
                columns.add("point.start_at as point_start_at");
                columns.add("point.end_at as point_end_at");
                columns.add("record.point_rate");
                columns.add("record.num");
                columns.add("record.created_at");
                columns.add("record.updated_at");
                StringBuffer selectColumns = new StringBuffer();
                boolean first = true;
                for (String column : columns) {
                    if (!first) {
                        selectColumns.append(",");
                    }
                    selectColumns.append(column);
                    first = false;
                }
                SELECT(selectColumns.toString());
                FROM("bp_point_record as record ,bp_point_info as point, bp_user as user");
                WHERE("record.opposite_uid = user.id and record.point_id = point.id");
                WHERE("point.company = " + queryPointSendDTO.getCompanyId());
                WHERE("record.category = " + PointRecordConstant.CATEGORY_GROUP_ISSUED);
                if (StringUtil.isNotBlank(queryPointSendDTO.getPointName())) {
                    WHERE("point.name = '" + queryPointSendDTO.getPointName() + "'");
                }
                if (queryPointSendDTO.getUserId() != null) {
                    WHERE("record.opposite_uid = " + queryPointSendDTO.getUserId());
                }
                if (queryPointSendDTO.getStartTime() != null) {
                    WHERE("record.created_at >= " + queryPointSendDTO.getStartTime());
                }
                if (queryPointSendDTO.getEndTime() != null) {
                    WHERE("record.created_at <= " + queryPointSendDTO.getEndTime());
                }
                ORDER_BY("record.updated_at desc");
            }
        }.toString();
    }


    public String queryPointRecordList(PointRecordDTO pointRecordDTO){
        String sql = new SQL() {
            {
                SELECT("bp_point_record.id");
                SELECT("mobile");
                SELECT("public_key  oppsitePublickey");
                SELECT("bp_point_info.name");
                SELECT("bp_point_record.num");
                SELECT("bp_point_record.memo");
                SELECT("is_life");
                SELECT("start_at");
                SELECT("end_at");
                SELECT("bp_point_record.created_at");
                SELECT("height");
                SELECT("hash");
                FROM(" bp_point_record ");
                INNER_JOIN(" bp_user on bp_user.id = bp_point_record.opposite_uid ");
                INNER_JOIN("bp_point_info on bp_point_record.point_id = bp_point_info.id");
                WHERE("bp_point_record.uid = " + pointRecordDTO.getUid());
                WHERE(" type = "+pointRecordDTO.getType());
                WHERE(" category = "+pointRecordDTO.getCategory());
                if (pointRecordDTO.getMobile()!=null){
                    WHERE(" mobile like '%"+pointRecordDTO.getMobile()+"%' ");
                }
                if (pointRecordDTO.getStartTime()!=null){
                    WHERE("bp_point_record.created_at >"+pointRecordDTO.getStartTime());
                }
                if (pointRecordDTO.getEndTime()!=null){
                    WHERE("bp_point_record.created_at <"+pointRecordDTO.getEndTime());
                }
                ORDER_BY("bp_point_record.created_at desc ");

            }
        }.toString();
        log.info("积分交易记录SQL:\n" + sql);
        return sql;
    }
}
