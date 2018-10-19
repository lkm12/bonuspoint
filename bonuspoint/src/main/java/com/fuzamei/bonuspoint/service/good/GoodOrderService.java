package com.fuzamei.bonuspoint.service.good;

import com.fuzamei.bonuspoint.entity.dto.good.GoodOrderDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodPayDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryOrderDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface GoodOrderService {
    /**
     * 用户订购商品
     *
     * @param goodOrderDTO 商品信息
     * @param uid          用户id
     * @return
     */
    ResponseVO orderGood(GoodOrderDTO goodOrderDTO, Long uid);

    /**
     * 用户取消订单
     *
     * @param id  订单id
     * @param uid 用户id
     * @return
     */
    ResponseVO cancelOrder(Long id, Long uid);

    /**
     * 商户卖家发货
     *
     * @param id            流水单号
     * @param uid           用户id
     * @param logisticsInfo 物流订单号
     * @return
     */
    ResponseVO sendGood(Long id, Long uid, String logisticsInfo);

    /**
     * 用户确认收货
     *
     * @param uid 用户id
     * @param id  流水单号
     * @return
     */
    ResponseVO confirmGood(Long id, Long uid);

    /**
     * 用户提出退货
     *
     * @param id       流水单号
     * @param uid      用户id
     * @param backMemo 退货理由
     * @return
     */
    ResponseVO applyBackGood(Long id, Long uid, String backMemo);

    /**
     * 商户确认退货
     *
     * @param id
     * @param uid 用户id
     * @return
     */
    ResponseVO confirmBackGood(Long id, Long uid);

    /**
     * 用户退货，添加物流单号
     *
     * @param id                流水单号
     * @param uid               用户id
     * @param backLogisticsInfo 退货物流单号
     * @return
     */
    ResponseVO backGood(Long id, Long uid, String backLogisticsInfo);

    /**
     * 商家确认成功退 货
     *
     * @param id  物流单号
     * @param uid 用户id
     * @return
     */
    ResponseVO backGoodSuccess(Long id, Long uid);

    /**
     * 用户和商户查询接口
     *
     * @param queryOrderDTO 查询条件
     * @param uid           用户标识
     * @return
     */
    ResponseVO queryGoodOrder(QueryOrderDTO queryOrderDTO, Long uid);

    /**
     * 获取对应订单信息
     *
     * @param id  订单信息
     * @param uid 用户信息
     * @return
     */
    ResponseVO getGoodOrder(Long id, Long uid);

    /**
     * 用户支付商品
     *
     * @param id         商品id
     * @param uid        用户id
     * @param goodPayDTO 支付信息
     * @return
     */
    ResponseVO payGoodOrder(Long id, Long uid, GoodPayDTO goodPayDTO);

    /**
     * 自动取消过期过期订单
     *
     * @param outTime 过期时间
     */
    void autoCancelOrder(Long outTime);


    ResponseVO updateGoodLogisticsInfo(Long id, Long uid, String logisticsInfo);
}
