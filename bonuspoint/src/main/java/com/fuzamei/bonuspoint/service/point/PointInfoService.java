package com.fuzamei.bonuspoint.service.point;

import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;

import java.util.List;

public interface PointInfoService {
    /**
     * 使积分过期
     * @param pointId
     */
    int setPointExpired(Long pointId);

    /**
     * 查积分失效时间小于某时间的积分
     * @param time
     * @return
     */
    List<PointInfoPO> listExpiredPoint(long time);
}
