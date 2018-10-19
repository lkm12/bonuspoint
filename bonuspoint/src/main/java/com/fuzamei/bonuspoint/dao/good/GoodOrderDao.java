package com.fuzamei.bonuspoint.dao.good;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.fuzamei.bonuspoint.entity.dto.good.QueryOrderDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodOrderPO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodOrderVO;
import com.fuzamei.bonuspoint.sql.good.GoodSqlFactory;
import org.springframework.stereotype.Repository;


/**
 * 订单操作接口
 * @author liumeng
 * @create 2018年4月25日
 */
@Mapper
@Repository
public interface GoodOrderDao {
    /**
     * 添加订单信息
     * @param goodOrderPO 订单信息
     * @return
     */
    @InsertProvider(type = GoodSqlFactory.class, method = "addGoodOrder")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int addGoodOrder(GoodOrderPO goodOrderPO);

    /**
     * 更新订单信息
     * @param goodOrderPO
     * @return
     */
    @UpdateProvider(type = GoodSqlFactory.class, method = "updateGoodOrder")
    int updateGoodOrder(GoodOrderPO goodOrderPO);
    /**
     * 根据流水号获取订单
     * @param id 流水号
     * @return
     */
    @Select("select * from  bp_good_orders where id =#{id}")
    GoodOrderPO getGoodOrder(Long id);
    /**
     * 根据流水号和用户uid获取订单
     * @param id 流水号
     * @param uid 用户uid
     * @return
     */
    @Select("select * from  bp_good_orders where id =#{id} and uid = #{uid}")
    GoodOrderPO getUserOrder(Long id,Long uid);   
    
    /**
     * 根据流水号获取订单信息
     * @param id 流水号
     * @return
     */
    @SelectProvider(type = GoodSqlFactory.class, method = "getGoodOrderInfo")
    GoodOrderVO getGoodOrderInfo(Long id);
    /**
     * 根据流水号获取和uid订单信息
     * @param id 流水号
     * @param uid 用户id
     * @return
     */
    @SelectProvider(type = GoodSqlFactory.class, method = "getUserOrderInfo")
    GoodOrderVO getUserOrderInfo(Long id,Long uid);
    /**
     * 根据查询条件获取订单信息
     * @param queryOrderDTO
     * @return
     */
    @SelectProvider(type = GoodSqlFactory.class, method = "queryGoodOrder")
    List<GoodOrderVO> queryGoodOrder(QueryOrderDTO queryOrderDTO);
    /**
     * 查询过期订单
     * @param outTime 过期时间
     * @return 过期订单
     */
    @Select("select * from bp_good_orders where status = 0 and unix_timestamp(now()) * 1000 >= #{outTime} + created_at")
    List<GoodOrderPO> listOutTimeOrders(Long outTime);
    /**
     * 订单超时失效
     * @param id 订单id
     * @return
     */
    @Update("update bp_good_orders as oorder set oorder.status = 100, updated_at = unix_timestamp(now()) * 1000 where id = #{id}")
    int cancelOrder(Long id);
}
