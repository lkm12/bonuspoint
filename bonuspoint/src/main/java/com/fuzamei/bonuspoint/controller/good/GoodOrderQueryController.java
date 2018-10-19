package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.entity.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fuzamei.bonuspoint.entity.dto.good.QueryOrderDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.good.GoodOrderService;

/**
 * 订单查询Controller
 *
 * @author liumeng
 * @create 2018年4月26日
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/good")
public class GoodOrderQueryController {

    private final GoodOrderService goodOrderService;

    @Autowired
    public GoodOrderQueryController(GoodOrderService goodOrderService) {
        this.goodOrderService = goodOrderService;
    }

    /**
     * 用户查询订单
     *
     * @param queryOrderDTO 查询条件
     * @return
     */
    @PostMapping("/queryGoodOrder")
    public ResponseVO queryGoodOrder(@RequestBody QueryOrderDTO queryOrderDTO, @RequestAttribute("token") Token token) {

        return goodOrderService.queryGoodOrder(queryOrderDTO, token.getUid());
    }

    /**
     * 获取对应订单信息
     *
     * @param id 订单id
     * @return
     */
    @GetMapping("/getGoodOrder/{id}")
    public ResponseVO getGoodOrder(@RequestAttribute("token") Token token, @PathVariable Long id) {

        return goodOrderService.getGoodOrder(id, token.getUid());
    }

}
