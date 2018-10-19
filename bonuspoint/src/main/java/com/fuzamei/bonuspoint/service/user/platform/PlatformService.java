package com.fuzamei.bonuspoint.service.user.platform;


import com.fuzamei.bonuspoint.entity.dto.PlatformInfoDTO;
import com.fuzamei.bonuspoint.entity.vo.user.PlatformBaseInfoVO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface PlatformService {
    /**
     * 平台查看自己的平台基础信息
     * @param uid 平台管理者id
     * @return
     */
    ResponseVO<PlatformBaseInfoVO> getSelfPlatformBaseInfo(Long uid);

    ResponseVO updatePlatformBaseInfo( PlatformInfoDTO platformInfoDTO);

    /**
     * 平台修改自己的通用积分与人民币兑换比率
     * @param platformUid
     * @param pointRate
     * @return
     */
    ResponseVO updatePlatformPointRate(Long platformUid, Float pointRate);
}
