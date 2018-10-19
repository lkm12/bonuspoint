package com.fuzamei.bonuspoint.scheduledTasks;

import com.fuzamei.bonuspoint.service.good.GoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lmm
 * @description 扫描商品过期
 * @create 2018/7/23 17:23
 */
@Component
@Slf4j
public class GoodScheduledTask {

    private final GoodService goodService;

    @Autowired
    public GoodScheduledTask(GoodService goodService) {
        super();
        this.goodService = goodService;
    }

    /**
     * 每 10 分钟扫描过期商品
     */
    @Scheduled(cron = "0 0/10 * * * ?",zone = "CST")
    public void autoOutTImeGood(){
        log.info("扫描过期商品");
        goodService.autoOutTImeGood();
    }

}
