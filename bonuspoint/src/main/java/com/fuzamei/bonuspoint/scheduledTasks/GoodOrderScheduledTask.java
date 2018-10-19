package com.fuzamei.bonuspoint.scheduledTasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fuzamei.bonuspoint.service.good.GoodOrderService;

import lombok.extern.log4j.Log4j;

/**
 * 清除作废商品订单
 * @author liumeng
 * @create 2018年7月3日
 */
@Slf4j
@Component
public class GoodOrderScheduledTask {

    private final GoodOrderService goodOrderService;

    @Autowired
    public GoodOrderScheduledTask(GoodOrderService goodOrderService) {
        super();
        this.goodOrderService = goodOrderService;
    }
    /**
     * 自动取消订单
     * 没10分钟扫描
     */
    @Scheduled(cron = "0 0/10 * * * ?",zone = "CST")
    public void autoCancelOrders() {
        log.info("清理过期订单!");
        Long outTime = new Long(24 * 60 * 60 * 1000);
        goodOrderService.autoCancelOrder(outTime);
    }
}
