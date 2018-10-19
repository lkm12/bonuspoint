package com.fuzamei.bonuspoint.service.user.mainplatform;

import com.fuzamei.bonuspoint.entity.dto.PlatformInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface MainPlatformService {


    /**
     * 添加平台
     * @param accountDTO 平台管理员用户
     * @param platformInfoDTO 平台信息
     * @return
     */
    ResponseVO addPlatform(AccountDTO accountDTO, PlatformInfoDTO platformInfoDTO);

    /**
     * 查看平台列表
     * @param pageDTO
     * @return
     */
    ResponseVO listPlatform(PageDTO pageDTO);


}
