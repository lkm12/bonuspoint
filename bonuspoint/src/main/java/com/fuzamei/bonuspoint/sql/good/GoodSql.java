package com.fuzamei.bonuspoint.sql.good;

import com.fuzamei.bonuspoint.constant.GoodConstant;
import com.fuzamei.bonuspoint.entity.dto.good.GoodExchangeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryGoodDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import com.fuzamei.bonuspoint.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类SQL
 * @author liumeng
 * @create 2018年4月18日
 */
public class GoodSql {
    private GoodSql() {
        throw new AssertionError("不能实例化 GoodSql");
    }
    /**
     * 添加商品信息
     * @param goodPO    商品信息
     * @return SQL
     */
    public static String savaGoodWithOutKey(GoodPO goodPO) {
        return new SQL() {
            {
                INSERT_INTO("bp_good");
                if (goodPO.getId() != null){
                    VALUES("id",   goodPO.getId().toString() );
                }
                if (goodPO.getGid() != null) {
                    VALUES("gid", "'" + goodPO.getGid() + "'");
                }
                if (goodPO.getSid() != null) {
                    VALUES("sid", "'" + goodPO.getSid() + "'");
                }
                if (goodPO.getName() != null) {
                    VALUES("name", "'" + goodPO.getName() + "'");
                }
                if (goodPO.getPrice() != null) {
                    VALUES("price", "'" + goodPO.getPrice() + "'");
                }
                if (goodPO.getGlobalPrice() != null) {
                    VALUES("global_price", "'" + goodPO.getGlobalPrice() + "'");
                }
                if (goodPO.getNum() != null) {
                    VALUES("num", "'" + goodPO.getNum() + "'");
                }
                if (goodPO.getNumUsed() != null) {
                    VALUES("num_used", "'" + goodPO.getNumUsed() + "'");
                }
                if (goodPO.getWorth() != null) {
                    VALUES("worth", "'" + goodPO.getWorth() + "'");
                }
                if (goodPO.getImgSrc() != null) {
                    VALUES("img_src", "'" + goodPO.getImgSrc() + "'");
                }
                if (goodPO.getDetails() != null) {
                    VALUES("details", "'" + goodPO.getDetails() + "'");
                }
                if (goodPO.getTopLevel() != null) {
                    VALUES("top_level", "'" + goodPO.getTopLevel() + "'");
                }
                if (goodPO.getOrderLevel() != null) {
                    VALUES("order_level", "'" + goodPO.getOrderLevel() + "'");
                }
                if (goodPO.getIsLife() != null) {
                    VALUES("is_life", "'" + (goodPO.getIsLife() ? 1 : 0) + "'");
                }
                if (goodPO.getStartAt() != null) {
                    VALUES("start_at", "'" + goodPO.getStartAt() + "'");
                }
                if (goodPO.getEndAt() != null) {
                    VALUES("end_at", "'" + goodPO.getEndAt() + "'");
                }
                if (goodPO.getStatus() != null) {
                    VALUES("status", "'" + goodPO.getStatus() + "'");
                }
                VALUES("created_at", "'" + System.currentTimeMillis() + "'");
                VALUES("updated_at", "'" + System.currentTimeMillis() + "'");

            }
        }.toString();

    }
    /**
     * 添加商品信息
     * @param goodPO    商品信息
     * @return SQL
     */
    public static String savaGood(GoodPO goodPO) {
        return new SQL() {
            {
                INSERT_INTO("bp_good");
                if (goodPO.getGid() != null) {
                    VALUES("gid", "'" + goodPO.getGid() + "'");
                }
                if (goodPO.getSid() != null) {
                    VALUES("sid", "'" + goodPO.getSid() + "'");
                }
                if (goodPO.getName() != null) {
                    VALUES("name", "'" + goodPO.getName() + "'");
                }
                if (goodPO.getPrice() != null) {
                    VALUES("price", "'" + goodPO.getPrice() + "'");
                }
                if (goodPO.getGlobalPrice() != null) {
                    VALUES("global_price", "'" + goodPO.getGlobalPrice() + "'");
                }
                if (goodPO.getNum() != null) {
                    VALUES("num", "'" + goodPO.getNum() + "'");
                }
                if (goodPO.getNumUsed() != null) {
                    VALUES("num_used", "'" + goodPO.getNumUsed() + "'");
                }
                if (goodPO.getWorth() != null) {
                    VALUES("worth", "'" + goodPO.getWorth() + "'");
                }
                if (goodPO.getImgSrc() != null) {
                    VALUES("img_src", "'" + goodPO.getImgSrc() + "'");
                }
                if (goodPO.getDetails() != null) {
                    VALUES("details", "'" + goodPO.getDetails() + "'");
                }
                if (goodPO.getTopLevel() != null) {
                    VALUES("top_level", "'" + goodPO.getTopLevel() + "'");
                }
                if (goodPO.getOrderLevel() != null) {
                    VALUES("order_level", "'" + goodPO.getOrderLevel() + "'");
                }
                if (goodPO.getIsLife() != null) {
                    VALUES("is_life", "'" + (goodPO.getIsLife() ? 1 : 0) + "'");
                }
                if (goodPO.getStartAt() != null) {
                    VALUES("start_at", "'" + goodPO.getStartAt() + "'");
                }
                if (goodPO.getEndAt() != null) {
                    VALUES("end_at", "'" + goodPO.getEndAt() + "'");
                }
                if (goodPO.getStatus() != null) {
                    VALUES("status", "'" + goodPO.getStatus() + "'");
                }
                VALUES("created_at", "'" + System.currentTimeMillis() + "'");
                VALUES("updated_at", "'" + System.currentTimeMillis() + "'");

            }
        }.toString();

    }

    /**
     * 更新商品信息
     * @param goodPO
     * @return sql
     */
    public static String updateGood(GoodPO goodPO) {
        return new SQL() {
            {
                UPDATE("bp_good");
                if (goodPO.getGid() != null) {
                    SET("gid" + "=" + "'" + goodPO.getGid() + "'");
                }
                if (goodPO.getSid() != null) {
                    SET("sid" + "=" + "'" + goodPO.getSid() + "'");
                }
                if (goodPO.getName() != null) {
                    SET("name" + "=" + "'" + goodPO.getName() + "'");
                }
                if (goodPO.getPrice() != null) {
                    SET("price" + "=" + "'" + goodPO.getPrice() + "'");
                }
                if (goodPO.getGlobalPrice() != null) {
                    SET("global_price" + "=" + "'" + goodPO.getGlobalPrice() + "'");
                }
                if (goodPO.getNum() != null) {
                    SET("num" + "=" + "'" + goodPO.getNum() + "'");
                }
                if (goodPO.getNumUsed() != null) {
                    SET("num_used" + "=" + "'" + goodPO.getNumUsed() + "'");
                }
                if (goodPO.getWorth() != null) {
                    SET("worth" + "=" + "'" + goodPO.getWorth() + "'");
                }
//                if (StringUtil.isNoneBlank(goodPO.getImgSrc())) {
//                    SET("img_src" + "=" + "'" + goodPO.getImgSrc() + "'");
//                }
                // 更新时输入空格删除图像
                if (goodPO.getImgSrc() != null) {
                    if ("".equals(goodPO.getImgSrc())) {
                        SET("img_src" + "=" + "NULL");
                    } else {
                        SET("img_src" + "=" + "'" + goodPO.getImgSrc() + "'");
                    }
                }
                if (goodPO.getDetails() != null) {
                    SET("details" + "=" + "'" + goodPO.getDetails() + "'");
                }
                if (goodPO.getTopLevel() != null) {
                    SET("top_level" + "=" + "'" + goodPO.getTopLevel() + "'");
                }
                if (goodPO.getOrderLevel() != null) {
                    SET("order_level" + "=" + "'" + goodPO.getOrderLevel() + "'");
                }
                if (goodPO.getIsLife() != null) {
                    SET("is_life" + "=" + "'" + (goodPO.getIsLife() ? 1 : 0) + "'");
                }
                if (goodPO.getStartAt() != null) {
                    SET("start_at" + "=" + "'" + goodPO.getStartAt() + "'");
                }
                if (goodPO.getEndAt() != null) {
                    SET("end_at" + "=" + "'" + goodPO.getEndAt() + "'");
                }
                if (goodPO.getStatus() != null) {
                    SET("status" + "=" + "'" + goodPO.getStatus() + "'");
                }
                SET("updated_at" + "=" + "'" + System.currentTimeMillis() + "'");

                WHERE("id" + "=" + "'" + goodPO.getId() + "'");

            }
        }.toString();

    }

    /**
     * 根据查询条件获取商品信息
     * @param queryGoodDTO
     * @return SQL
     */
    public static String queryGood(QueryGoodDTO queryGoodDTO) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("good.id");
                columns.add("good.gid");
                columns.add("company.company_name as companyName");
                columns.add("good.sid");
                columns.add("subtype.name as subTypeName");
                columns.add("type.id as tid");
                columns.add("type.name as typeName");
                columns.add("good.name");
                columns.add("good.price");
                columns.add("good.global_price");
                columns.add("good.num");
                columns.add("good.num_used");
                columns.add("good.worth");
                columns.add("good.img_src");
                columns.add("good.details");
                columns.add("good.top_level");
                columns.add("good.order_level");
                columns.add("good.is_life");
                columns.add("good.start_at");
                columns.add("good.end_at");
                columns.add("good.status");
                columns.add("case  good.status " + "when 0 then '下架' " + "when 1 then '上架' " + "when 2 then '售完' "
                        + "when 3 then '删除' " + "else '未知' " + "end as statusName ");
                columns.add("good.created_at");
                columns.add("good.updated_at");
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
                FROM("bp_good as good ,bp_good_type as type,bp_company_info  as company,bp_good_subtype as subtype,bp_user as uuser");
                WHERE("good.gid = company.id");
                WHERE("good.sid = subtype.id");
                WHERE("subtype.pid = type.id");
                WHERE("uuser.id = company.uid");
                if (queryGoodDTO.getPid() != null){
                    WHERE("uuser.p_id = " + queryGoodDTO.getPid() );
                }
                if (queryGoodDTO.getGid() != null) {
                    WHERE("good.gid=" + queryGoodDTO.getGid());
                }
                if (queryGoodDTO.getTid() != null) {
                    WHERE("type.id=" + queryGoodDTO.getTid());
                }
                if (queryGoodDTO.getSid() != null) {
                    WHERE("good.sid=" + queryGoodDTO.getSid());
                }
                if (StringUtil.isNoneBlank(queryGoodDTO.getStatus())) {
                    WHERE("good.status = " + queryGoodDTO.getStatus());
                }
                if (!StringUtil.isBlank(queryGoodDTO.getGoodName())) {
                    WHERE("good.name like" + "'" + "%" + queryGoodDTO.getGoodName() + "%" + "'");
                }
                if (!StringUtil.isBlank(queryGoodDTO.getCompanyName())) {
                    WHERE("company.company_name like" + "'" + "%" + queryGoodDTO.getCompanyName() + "%" + "'");
                }
                if (queryGoodDTO.getTopOrder() != null) {
                    if (queryGoodDTO.getTopOrder().equals(GoodConstant.TOP_ASC)) {
                        ORDER_BY("good.top_level asc");
                    }
                    if (queryGoodDTO.getTopOrder().equals(GoodConstant.TOP_DESC)) {
                        ORDER_BY("good.top_level desc");
                    }
                }
                if (queryGoodDTO.getOrderOrder() != null) {
                    if (queryGoodDTO.getOrderOrder().equals(GoodConstant.ORDER_ASC)) {
                        ORDER_BY("good.order_level asc");
                    }
                    if (queryGoodDTO.getOrderOrder().equals(GoodConstant.ORDER_DESC)) {
                        ORDER_BY("good.order_level desc");
                    }
                }
                if (queryGoodDTO.getPriceOrder() != null) {
                    if (queryGoodDTO.getPriceOrder().equals(GoodConstant.PRICE_ASC)) {
                        ORDER_BY("good.price asc");
                    }
                    if (queryGoodDTO.getPriceOrder().equals(GoodConstant.PRICE_DESC)) {
                        ORDER_BY("good.price desc");
                    }
                }
                if (queryGoodDTO.getSellOrder() != null) {
                    if (queryGoodDTO.getSellOrder().equals(GoodConstant.SELL_ASC)) {
                        ORDER_BY("good.num_used asc");
                    }
                    if (queryGoodDTO.getSellOrder().equals(GoodConstant.PRICE_DESC)) {
                        ORDER_BY("good.num_used desc");
                    }

                }
                if (queryGoodDTO.getTimeOrder() != null) {
                    if (queryGoodDTO.getTimeOrder().equals(GoodConstant.PRICE_ASC)) {
                        ORDER_BY("good.updated_at asc");
                    }
                    if (queryGoodDTO.getTimeOrder().equals(GoodConstant.TIME_DESC)) {
                        ORDER_BY("good.updated_at desc");
                    }
                }

            }
        }.toString();
    }

    /**
     * 获取指定商品信息
     * @param id 商品id
     * @return SQL
     */
    public static String getGoodInfo(Long id) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("good.id");
                columns.add("good.gid");
                columns.add("company.company_name as companyName");
                columns.add("good.sid");
                columns.add("subtype.name as subTypeName");
                columns.add("type.id as tid");
                columns.add("type.name as typeName");
                columns.add("good.name");
                columns.add("good.price");
                columns.add("good.global_price");
                columns.add("good.num");
                columns.add("good.num_used");
                columns.add("good.worth");
                columns.add("good.img_src");
                columns.add("good.details");
                columns.add("good.top_level");
                columns.add("good.order_level");
                columns.add("good.is_life");
                columns.add("good.start_at");
                columns.add("good.end_at");
                columns.add("good.status");
                columns.add("case  good.status " + "when 0 then '下架' " + "when 1 then '上架' " + "when 2 then '售完' "
                        + "when 3 then '删除 '" + "else '' " + "end as statusName ");
                columns.add("good.created_at");
                columns.add("good.updated_at");
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
                FROM("bp_good as good ,bp_good_type as type,bp_company_info  as company,bp_good_subtype as subtype");
                WHERE("good.gid = company.id");
                WHERE("good.sid = subtype.id");
                WHERE("subtype.pid = type.id");
                WHERE("good.id = " + id);
            }
        }.toString();
    }

    /**
     * 商品兑换详情查询
     * @param exchangeDTO 查询条件
     * @return sql
     */
    public static String queryGoodExchange(GoodExchangeDTO exchangeDTO) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("oorder.id");
                columns.add("oorder.good_price");
                columns.add("oorder.good_global_price");
                columns.add("oorder.logistics_info");
                columns.add("oorder.num");
                columns.add("CASE oorder.pay_mode " + "WHEN 1 THEN '会员积分' " + "WHEN 2 THEN '通用积分' " + "ELSE '' "
                        + "END AS payModel  ");
                columns.add("oorder.pay_total");
                columns.add("oorder.status");
                columns.add("oorder.bargain_at as exchangeTime");
                columns.add("oorder.created_at");
                columns.add("oorder.height");
                columns.add("oorder.hash");
                columns.add("company.company_name");

                columns.add("uuser.mobile");

                columns.add("CONCAT(address.receiver,' ',address.mobile,' ',district.whole_name, address.area_detail) AS address");

                columns.add("good.name as goodName");
                columns.add("good.worth");

                columns.add("type.name as typeName");
                columns.add("subType.name as subTypeName");

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
                FROM("bp_good_orders as oorder");
                LEFT_OUTER_JOIN("bp_good AS good ON oorder.good_id = good.id");
                LEFT_OUTER_JOIN("bp_company_info AS company ON oorder.gid = company.id");
                LEFT_OUTER_JOIN("bp_user AS uuser ON oorder.uid = uuser.id");
                LEFT_OUTER_JOIN("bp_user_address AS address ON oorder.address_id = address.id");
                LEFT_OUTER_JOIN("cn_district AS district ON district.`code` = address.district_code");
                LEFT_OUTER_JOIN("bp_good_subtype AS subType ON good.sid = subType.id");
                LEFT_OUTER_JOIN("bp_good_type AS type ON type.id = subType.pid");

                if (exchangeDTO.getCompanyId() != null) {
                    WHERE("company.id = " + exchangeDTO.getCompanyId().toString());
                }

                if (exchangeDTO.getTypeId() != null) {
                    WHERE("type.id = " + exchangeDTO.getTypeId().toString());
                }
                if (exchangeDTO.getSubTypeId() != null) {
                    WHERE("subType.id = " + exchangeDTO.getSubTypeId().toString());
                }
                if (StringUtil.isNotBlank(exchangeDTO.getMobile())) {
                    WHERE("uuser.mobile = '" + exchangeDTO.getMobile() + "'");
                }
                if (StringUtil.isNotBlank(exchangeDTO.getGoodName())) {
                    WHERE("good.name = '" + exchangeDTO.getGoodName() + "'");
                }
                if (StringUtil.isNotBlank(exchangeDTO.getCompanyName())) {
                    WHERE("company.company_name = '" + exchangeDTO.getCompanyName() + "'");
                }
                if (exchangeDTO.getBegin() != null) {
                    WHERE("oorder.created_at >= " + exchangeDTO.getBegin().toString());
                }
                if (exchangeDTO.getEnd() != null) {
                    WHERE("oorder.created_at <= " + exchangeDTO.getEnd().toString());
                }
                ORDER_BY("oorder.updated_at DESC");
            }
        }.toString();
    }

    /**
     * 分类商品预览sql
     * @param tid 分类id
     * @param pid 平台id
     * @return
     */
    public static String previewGood(Long tid , Long pid) {
        return new SQL() {
            {
                List<String> columns = new ArrayList<>();
                columns.add("good.id");
                columns.add("good.gid");
                columns.add("company.company_name as companyName");
                columns.add("good.sid");
                columns.add("subtype.name as subTypeName");
                columns.add("type.id as tid");
                columns.add("type.name as typeName");
                columns.add("good.name");
                columns.add("good.price");
                columns.add("good.global_price");
                columns.add("good.num");
                columns.add("good.num_used");
                columns.add("good.worth");
                columns.add("good.img_src");
                columns.add("good.details");
                columns.add("good.top_level");
                columns.add("good.order_level");
                columns.add("good.is_life");
                columns.add("good.start_at");
                columns.add("good.end_at");
                columns.add("good.status");
                columns.add("case  good.status " + "when 0 then '下架' " + "when 1 then '上架' " + "when 2 then '售完' "
                        + "when 3 then '删除 '" + "else '' " + "end as statusName ");
                columns.add("good.created_at");
                columns.add("good.updated_at");
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
                FROM("bp_good as good ,bp_good_type as type,bp_company_info  as company,bp_good_subtype as subtype,bp_user as uuser");
                WHERE("good.gid = company.id");
                WHERE("good.sid = subtype.id");
                WHERE("subtype.pid = type.id");
                WHERE("uuser.id = company.uid");
                WHERE("type.id = " + tid);
                WHERE("uuser.p_id = " + pid);
                WHERE("good.status = 1 ");
                ORDER_BY("created_at DESC " + "LIMIT 3 ");

            }
        }.toString();
    }


}
