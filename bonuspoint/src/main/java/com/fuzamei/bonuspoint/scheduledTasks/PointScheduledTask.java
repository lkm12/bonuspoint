package com.fuzamei.bonuspoint.scheduledTasks;

import com.fuzamei.bonuspoint.dao.common.mapper.PointInfoMapper;
import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fuzamei.bonuspoint.service.point.PointInfoService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description: 定时检测过期积分
 * @author: WangJie
 * @create: 2018-05-10 09:54
 **/
@Component
@Slf4j
public class PointScheduledTask {


    private final PointInfoService pointInfoService;


    @Autowired
    public PointScheduledTask(PointInfoService pointInfoService) {
        this.pointInfoService = pointInfoService;
    }

    /**
     * m每天0点0分0秒处理过期积分
     * @author wangjie
     */
    @Scheduled(cron = "0 0 0 * * ?",zone = "CST")
    public void checkAndHandleExpiredPoint(){
        log.info("处理过期积分任务开始");
        int count = 0;
        long now = System.currentTimeMillis()+100;
        List<PointInfoPO> pointInfoPOList = pointInfoService.listExpiredPoint(now);
        for (PointInfoPO pointInfoPO : pointInfoPOList){
            log.info("处理过期积分id:{},name:{}",pointInfoPO.getId(),pointInfoPO.getName());
            count++;
            pointInfoService.setPointExpired(pointInfoPO.getId());
        }
        log.info("处理过期积分任务结束，处理count:{}条。",count);
    }



}
