package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.good.GoodPayDTO;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fuzamei.bonuspoint.constant.GoodOrderConstant;
import com.fuzamei.bonuspoint.entity.dto.good.GoodOrderDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.good.GoodOrderService;

/**
 * 订单操作Controller
 * @author liumeng
 * @create 2018年4月26日
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/good")
public class GoodOrderManageController {

    private final GoodOrderService goodOrderService;

    @Autowired
    public GoodOrderManageController(GoodOrderService goodOrderService) {
        this.goodOrderService = goodOrderService;
    }

    /**
     * 用户订购商品(待支付-完成)
     * @param goodOrderDTO 订购信息
     * @param token token
     * @return
     */
    @PostMapping("/order")
    public ResponseVO orderGood(@RequestAttribute("token") Token token, @RequestBody GoodOrderDTO goodOrderDTO) {
        log.info("用户订购商品（待付款）");
        return goodOrderService.orderGood(goodOrderDTO, token.getUid());
    }

    /**
     * 用户取消订单
     * @param id 订单标号
     * @param token   用户标识
     * @return
     */
    @GetMapping("/order/cancel")
    public ResponseVO cancelOrder(@RequestAttribute("token") Token token,@RequestParam Long id) {
        log.info("用户取消订单");
        return goodOrderService.cancelOrder(id, token.getUid());
    }

    /**
     * 用户支付订单
     * @param  goodPayDTO 支付信息
     * @return
     */
    @PostMapping("/order/pay/{id}")
    public ResponseVO payOrder(@PathVariable Long id ,@RequestAttribute("token") Token token, @RequestBody GoodPayDTO goodPayDTO) {
        return goodOrderService.payGoodOrder(id,token.getUid(),goodPayDTO);
    }

    /**
     * 商户发送货物
     * @param id    订单号
     * @param logisticsInfo 物流单号
     * @return
     */
    @GetMapping("/company/order/send/logisticsInfo/{id}/{logisticsInfo}")
    public ResponseVO sendGood(@PathVariable("id")  Long id, @RequestAttribute("token") Token token, @PathVariable("logisticsInfo") String logisticsInfo) {
        return goodOrderService.sendGood(id, token.getUid(), logisticsInfo);
    }

    /**
     * 商户修改发货物流订单号
     * @param id
     * @param token
     * @param logisticsInfo
     * @return
     */
    @GetMapping("/company/order/update/logisticsInfo/{id}/{logisticsInfo}")
    public ResponseVO updateGoodLogisticsInfo(@PathVariable("id")  Long id, @RequestAttribute("token") Token token, @PathVariable("logisticsInfo") String logisticsInfo) {
        return goodOrderService.updateGoodLogisticsInfo(id, token.getUid(), logisticsInfo);
    }


    /**
     * 用户确认收货
     * @param id 订单号
     * @return
     */
    @GetMapping ("/order/confirm")
    public ResponseVO confirmGood(@RequestParam Long id, @RequestAttribute("token") Token token) {

        return goodOrderService.confirmGood(id, token.getUid());
    }

    /**
     * 用户申请退货
     * @param id 订单号
     * @param backMemo 退货理由
     * @return
     */
    @GetMapping("/order/applyback")
    ResponseVO applyBackGood(@RequestParam Long id, @RequestAttribute("token") Token token,
            @RequestParam(required = false) String backMemo) {
        return goodOrderService.applyBackGood(id, token.getUid(), backMemo);
    }

    /**
     * 商户同意退货
     * @param id 订单号
     * @return
     */
    @GetMapping("/order/confirmback")
    public ResponseVO confirmBackGood(@PathVariable Long id, @RequestAttribute("token") Token token) {

        return goodOrderService.confirmBackGood(id, token.getUid());
    }

    /**
     * 用户退货
     * @param id 订单号
     * @param backLogisticsInfo 退货物流号
     * @return
     */
    @GetMapping("/order/back")
    public ResponseVO backGood(@RequestParam Long id,@RequestAttribute("token") Token token, @RequestParam String backLogisticsInfo) {

        return goodOrderService.backGood(id, token.getUid(), backLogisticsInfo);
    }

    /**
     * 商家确认退货成功
     * @param id    订单物流号
     * @return
     */
    @GetMapping("/order/backsuccess")
    public ResponseVO backGoodSuccess(@RequestParam Long id,@RequestAttribute("token") Token token) {

        return goodOrderService.backGoodSuccess(id, token.getUid());
    }
}
