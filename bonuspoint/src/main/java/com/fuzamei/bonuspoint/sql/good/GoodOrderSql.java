package com.fuzamei.bonuspoint.sql.good;

import java.util.ArrayList;
import java.util.List;

import com.fuzamei.bonuspoint.constant.GoodOrderConstant;
import org.apache.ibatis.jdbc.SQL;

import com.fuzamei.bonuspoint.entity.dto.good.QueryOrderDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodOrderPO;
import com.fuzamei.bonuspoint.util.StringUtil;

/**
 * 订单SQL
 * @author liumeng
 * @create 2018年4月25日
 */
public class GoodOrderSql {
    private GoodOrderSql() {
        throw new AssertionError("不能实例化 GoodOrderSql");
    }

    public static String addGoodOrder(GoodOrderPO goodOrderPO) {
        return new SQL() {
            {
                INSERT_INTO("bp_good_orders");
                if (!StringUtil.isBlank(goodOrderPO.getBlockSid())) {
                    VALUES("block_sid", "'" + goodOrderPO.getBlockSid() + "'");
                }
                if (goodOrderPO.getUid() != null) {
                    VALUES("uid", goodOrderPO.getUid().toString());
                }
                if (goodOrderPO.getGid() != null) {
                    VALUES("gid", goodOrderPO.getGid().toString());
                }
                if (goodOrderPO.getGoodId() != null) {
                    VALUES("good_id", goodOrderPO.getGoodId().toString());
                }
                if (goodOrderPO.getGoodPrice() != null) {
                    VALUES("good_price", goodOrderPO.getGoodPrice().toString());
                }
                if (goodOrderPO.getGoodGlobalPrice() != null) {
                    VALUES("good_global_price", goodOrderPO.getGoodGlobalPrice().toString());
                }
                if (goodOrderPO.getNum() != null) {
                    VALUES("num", goodOrderPO.getNum().toString());
                }
                if (goodOrderPO.getPayMode() != null) {
                    VALUES("pay_mode", goodOrderPO.getPayMode().toString());
                }
                if (goodOrderPO.getPayTotal() != null) {
                    VALUES("pay_total", goodOrderPO.getPayTotal().toString());
                }
                if (!StringUtil.isBlank(goodOrderPO.getDistribution())) {
                    VALUES("distribution", "'" + goodOrderPO.getDistribution() + "'");
                }
                if (!StringUtil.isBlank(goodOrderPO.getMessage())) {
                    VALUES("message", "'" + goodOrderPO.getMessage() + "'");
                }
                if (goodOrderPO.getAddressId() != null) {
                    VALUES("address_id", goodOrderPO.getAddressId().toString());
                }
                if (goodOrderPO.getAddressName() != null) {
                    VALUES("address_name","'"+ goodOrderPO.getAddressName() + "'");
                }
                if (goodOrderPO.getAddressMobile() != null){
                    VALUES("address_mobile" , "'" + goodOrderPO.getAddressMobile() + "'");
                }
                if (goodOrderPO.getAddressDistrict() !=null){
                    VALUES("address_district","'" + goodOrderPO.getAddressDistrict() +"'");
                }
                if (goodOrderPO.getAddressDetail() != null){
                    VALUES("address_detail","'" + goodOrderPO.getAddressDetail() + "'");
                }
                if (goodOrderPO.getStatus() != null) {
                    VALUES("status", goodOrderPO.getStatus().toString());
                }
                if (StringUtil.isNotBlank(goodOrderPO.getLogisticsInfo())) {
                    VALUES("logistics_info", "'" + goodOrderPO.getLogisticsInfo() + "'");
                }
                if (StringUtil.isNotBlank(goodOrderPO.getBackLogisticsInfo())) {
                    VALUES("back_logistics_info", "'" + goodOrderPO.getBackLogisticsInfo() + "'");
                }
                if (StringUtil.isNotBlank(goodOrderPO.getBackMemo())) {
                    VALUES("back_memo", "'" + goodOrderPO.getBackMemo() + "'");
                }
                if (goodOrderPO.getPayedAt() != null) {
                    VALUES("payed_at", goodOrderPO.getPayedAt().toString());
                }
                if (goodOrderPO.getBargainAt() != null) {
                    VALUES("bargain_at", goodOrderPO.getBargainAt().toString());
                }
                VALUES("created_at", String.valueOf(System.currentTimeMillis()));
                VALUES("updated_at", String.valueOf(System.currentTimeMillis()));
            }
        }.toString();
    }

    /**
     * 更新订单信息
     * @param goodOrderPO
     * @return
     */
    public static String updateGoodOrder(GoodOrderPO goodOrderPO) {
        return new SQL() {
            {

                UPDATE("bp_good_orders");
                if (!StringUtil.isBlank(goodOrderPO.getBlockSid())) {
                    SET("block_sid = " + "'" + goodOrderPO.getBlockSid() + "'");
                }
                if (goodOrderPO.getUid() != null) {
                    SET("uid = " + goodOrderPO.getUid().toString());
                }
                if (goodOrderPO.getGid() != null) {
                    SET("gid = " + goodOrderPO.getGid().toString());
                }
                if (goodOrderPO.getGoodId() != null) {
                    SET("good_id = " + goodOrderPO.getGoodId().toString());
                }
                if (goodOrderPO.getGoodPrice() != null) {
                    SET("good_price = " + goodOrderPO.getGoodPrice().toString());
                }
                if (goodOrderPO.getGoodGlobalPrice() != null) {
                    SET("good_global_price = " + goodOrderPO.getGoodGlobalPrice().toString());
                }
                if (goodOrderPO.getNum() != null) {
                    SET("num = " + goodOrderPO.getNum().toString());
                }
                if (goodOrderPO.getPayMode() != null) {
                    SET("pay_mode = " + goodOrderPO.getPayMode().toString());
                }
                if (goodOrderPO.getPayTotal() != null) {
                    SET("pay_total = " + goodOrderPO.getPayTotal().toString());
                }
                if (!StringUtil.isBlank(goodOrderPO.getDistribution())) {
                    SET("distribution = " + "'" + goodOrderPO.getDistribution() + "'");
                }
                if (!StringUtil.isBlank(goodOrderPO.getMessage())) {
                    SET("message = " + "'" + goodOrderPO.getMessage() + "'");
                }
                if (goodOrderPO.getAddressId() != null) {
                    SET("address_id = " + goodOrderPO.getAddressId().toString());
                }
                if (goodOrderPO.getStatus() != null) {
                    SET("status = " + goodOrderPO.getStatus().toString());
                }
                if (StringUtil.isNotBlank(goodOrderPO.getLogisticsInfo())) {
                    SET("logistics_info = " + "'" + goodOrderPO.getLogisticsInfo() + "'");
                }
                if (StringUtil.isNotBlank(goodOrderPO.getBackLogisticsInfo())) {
                    SET("back_logistics_info = " + "'" + goodOrderPO.getBackLogisticsInfo() + "'");
                }
                if (StringUtil.isNotBlank(goodOrderPO.getBackMemo())) {
                    SET("back_memo = " + "'" + goodOrderPO.getBackMemo() + "'");
                }
                if (goodOrderPO.getPayedAt() != null) {
                    SET("payed_at = " + goodOrderPO.getPayedAt().toString());
                }
                if (goodOrderPO.getBargainAt() != null) {
                    SET("bargain_at = " + goodOrderPO.getBargainAt().toString());
                }
                if(goodOrderPO.getHeight() != null) {
                    SET("height = " + goodOrderPO.getHeight().toString());
                }
                if(goodOrderPO.getHash() != null) {
                    SET("hash = " + "'" + goodOrderPO.getHash() + "'");
                }
                SET("updated_at = " + String.valueOf(System.currentTimeMillis()));
                WHERE("id = " + goodOrderPO.getId());
            }
        }.toString();
    }

    /**
     * 根据id 获取订单信息
     * @param id id
     * @return
     */
    public static String getGoodOrderInfo(Long id) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("orders.id");
                columns.add("orders.block_sid");
                columns.add("orders.uid");
                columns.add("orders.gid");
                columns.add("company.company_name as companyName");
                columns.add("orders.good_id");
                columns.add("good.name as goodName");
                columns.add("good.img_src as imgSrc");
                columns.add("orders.good_price");
                columns.add("orders.good_global_price");
                columns.add("orders.num");
                columns.add("orders.pay_mode");
                columns.add("orders.pay_total");
                columns.add("orders.distribution");
                columns.add("orders.message");
                columns.add("orders.address_id");
                columns.add("orders.status");
                columns.add("orders.logistics_info");
                columns.add("orders.back_logistics_info");
                columns.add("orders.back_memo");
                columns.add("orders.payed_at");
                columns.add("orders.bargain_at");
                columns.add("orders.created_at");
                columns.add("orders.updated_at");
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
                FROM("bp_good_orders as orders , bp_good as good , bp_company_info as company");
                WHERE("orders.gid = company.id");
                WHERE("orders.good_id = good.id");
                WHERE("orders.id = " + id);
            }
        }.toString();
    }
    /**
     * 根据id 获取订单信息
     * @param id id
     * @param uid uid
     * @return
     */
    public static String getUserOrderInfo(Long id,Long uid) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("orders.id");
                columns.add("orders.block_sid");
                columns.add("orders.uid");
                columns.add("orders.gid");
                columns.add("company.company_name as companyName");
                columns.add("orders.good_id");
                columns.add("good.name as goodName");
                columns.add("good.img_src as imgSrc");
                columns.add("orders.good_price");
                columns.add("orders.good_global_price");
                columns.add("orders.num");
                columns.add("orders.pay_mode");
                columns.add("orders.pay_total");
                columns.add("orders.distribution");
                columns.add("orders.message");
                columns.add("orders.address_id");
                columns.add("orders.address_name");
                columns.add("orders.address_mobile as address_phone");
                columns.add("CONCAT(orders.address_district,orders.address_detail) as address_details");
                columns.add("orders.status");
                columns.add("orders.logistics_info");
                columns.add("orders.back_logistics_info");
                columns.add("orders.back_memo");
                columns.add("orders.payed_at");
                columns.add("orders.bargain_at");
                columns.add("orders.created_at");
                columns.add("orders.updated_at");
                columns.add("company.company_telephone");
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
                FROM("bp_good_orders as orders , bp_good as good , bp_company_info as company");
                WHERE("orders.gid = company.id");
                WHERE("orders.good_id = good.id");
                WHERE("orders.id = " + id);
                WHERE("orders.uid = " + uid);
            }
        }.toString();
    }
    public static String queryGoodOrder(QueryOrderDTO queryOrderDTO) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("orders.id");
                columns.add("orders.block_sid");
                columns.add("orders.uid");
                columns.add("orders.gid");
                columns.add("company.company_name as companyName");
                columns.add("orders.good_id");
                columns.add("good.name as goodName");
                columns.add("good.img_src as imgSrc");
                columns.add("orders.good_price");
                columns.add("orders.good_global_price");
                columns.add("orders.num");
                columns.add("orders.pay_mode");
                columns.add("CASE orders.pay_mode " +
                        "WHEN 1 THEN '会员积分' " +
                        "WHEN 2 THEN '通用积分' " +
                        "ELSE '' " +
                        "END AS payModeName");
                columns.add("orders.pay_total");
                columns.add("orders.distribution");
                columns.add("orders.message");
                columns.add("orders.address_id");
                columns.add("orders.address_name");
                columns.add("orders.address_mobile as address_phone");
                columns.add("CONCAT(orders.address_district,orders.address_detail) as address_details");
                columns.add("orders.status");
                columns.add("CASE orders.status " +
                        "WHEN 0 THEN '待付款' " +
                        "WHEN 1 THEN '待发货' " +
                        "WHEN 2 THEN '运输中' " +
                        "WHEN 3 THEN '待收货' " +
                        "WHEN 4 THEN '交易完成' " +
                        "WHEN 5 THEN '退货中待集团确认' " +
                        "WHEN 6 THEN '退货已确认' " +
                        "WHEN 7 THEN '退货已发货' " +
                        "WHEN 8 THEN '退货成功' " +
                        "WHEN 99 THEN '取消订单' " +
                        "WHEN 100 THEN '超时失效' " +
                        "WHEN 101 THEN '隐藏订单'"  +
                        "ELSE '' " +
                        "END AS statusName");
                columns.add("orders.logistics_info");
                columns.add("orders.back_logistics_info");
                columns.add("orders.back_memo");
                columns.add("orders.payed_at");
                columns.add("orders.bargain_at");
                columns.add("orders.created_at");
                columns.add("orders.updated_at");
                columns.add("company.company_telephone");
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
                FROM("bp_good_orders as orders , bp_good as good , bp_company_info as company");
                WHERE("orders.gid = company.id");
                WHERE("orders.good_id = good.id");
                if (queryOrderDTO.getId() != null) {
                    WHERE("orders.id = " + queryOrderDTO.getId());
                }
                if (queryOrderDTO.getUid() != null) {
                    WHERE("orders.uid  = " + queryOrderDTO.getUid());
                }
                if (queryOrderDTO.getGid() != null) {
                    WHERE("orders.gid = " + queryOrderDTO.getGid());
                }
                if(StringUtil.isNotBlank(queryOrderDTO.getCompanyName())) {
                    WHERE("company.company_name like " + "'" + queryOrderDTO.getCompanyName() + "%'");
                }
                if(StringUtil.isNotBlank(queryOrderDTO.getGoodName())) {
                    WHERE("good.name like " + "'" + queryOrderDTO.getGoodName() + "%'");
                }
                if (queryOrderDTO.getStatus() != null) {
                    if (GoodOrderConstant.ORDER_ALL_FAILED == queryOrderDTO.getStatus().intValue()){
                        WHERE("(orders.status = 99 or orders.status = 100 )" );
                    }else{
                        WHERE("orders.status = " + queryOrderDTO.getStatus());
                    }
                }
                ORDER_BY("orders.updated_at   desc");

            }
        }.toString();
    }

}
