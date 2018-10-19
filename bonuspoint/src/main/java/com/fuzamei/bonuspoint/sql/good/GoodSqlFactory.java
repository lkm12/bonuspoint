package com.fuzamei.bonuspoint.sql.good;

import com.fuzamei.bonuspoint.entity.dto.good.GoodExchangeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryGoodDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryOrderDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodOrderPO;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import com.fuzamei.bonuspoint.entity.po.good.GoodSubTypePO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodExchangeVO;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品SQL工厂类
 * @author liumeng
 * @create 2018年4月18日
 */
@Slf4j
public class GoodSqlFactory {

    /**
     * 获取更新商品子类sql
     * @param goodSubTypePO
     * @return sql
     */
    public String updateSubType(GoodSubTypePO goodSubTypePO) {
        return GoodTypeSql.updateSubType(goodSubTypePO);
    }

    /**
     * 添加商品信息(不生成主键)
     * @param goodPO    商品信息
     * @return SQL
     */
    public String savaGoodWithOutKey(GoodPO goodPO) {
        return GoodSql.savaGoodWithOutKey(goodPO);
    }


    /**
     * 添加商品信息
     * @param goodPO    商品信息
     * @return SQL
     */
    public String savaGood(GoodPO goodPO) {
        return GoodSql.savaGood(goodPO);
    }

    /**
     * 更新商品信息
     * @param goodPO
     * @return
     */
    public String updateGood(GoodPO goodPO) {
        return GoodSql.updateGood(goodPO);
    }

    /**
     * 根据商品id查询商品信息
     * @param id 商品id
     * @return
     */
    public String getGoodInfo(Long id) {
        return GoodSql.getGoodInfo(id);
    }

    /**
     * 根据查询条件获取商品信息
     * @param queryGoodDTO
     * @return SQL
     */
    public String queryGood(QueryGoodDTO queryGoodDTO) {
        String sql = GoodSql.queryGood(queryGoodDTO);
        log.info("\n" + sql + "\n");
        return sql;
    }

    /**
     * 添加订单信息
     * @param goodOrderPO 订单信息
     * @return sql
     */
    public String addGoodOrder(GoodOrderPO goodOrderPO) {
        return GoodOrderSql.addGoodOrder(goodOrderPO);
    }

    /**
     * 更新订单信息
     * @param goodOrderPO
     * @return sql
     */
    public String updateGoodOrder(GoodOrderPO goodOrderPO) {
        return GoodOrderSql.updateGoodOrder(goodOrderPO);
    }

    /**
     * 根据流水号获取订单信息
     * @param id 流水号
     * @return sql
     */
    public String getGoodOrderInfo(Long id) {
        return GoodOrderSql.getGoodOrderInfo(id);
    }

    /**
     * 根据流水号和 uid 获取订单信息
     * @param id 流水号
     * @param uid uid
     * @return sql
     */
    public String getUserOrderInfo(Long id, Long uid) {
        return GoodOrderSql.getUserOrderInfo(id, uid);
    }

    /**
     * 根据查询条件获取订单信息
     * @param queryOrderDTO
     * @return
     */
    public String queryGoodOrder(QueryOrderDTO queryOrderDTO) {
        String sql = GoodOrderSql.queryGoodOrder(queryOrderDTO);
        log.info("\n" + sql + "\n");
        return sql;
    }

    /**
     * 商品兑换详情查询
     * @param exchangeDTO 查询条件
     * @return SQL结果
     */
    public static String queryGoodExchange(GoodExchangeDTO exchangeDTO) {
        String sql = GoodSql.queryGoodExchange(exchangeDTO);
        log.info(sql);
        return sql;
    }

    /**
     * f分类预览sql
     * @param tid 分类id
     * @param  pid 平台id
     * @return
     */
    public static String previewGood(Long tid , Long pid) {
        String sql = GoodSql.previewGood(tid,pid);
        log.info(sql);
        return sql;
    }
}
