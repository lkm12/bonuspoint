package com.fuzamei.bonuspoint.service.data.version;

import com.fuzamei.bonuspoint.entity.dto.data.version.AppVersioinDTO;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface AppVersionService {
    ResponseVO addAppVersion(AppVersioinDTO appVersioinDTO);

    ResponseVO getNewestAppVersionForAndroid();

    ResponseVO getNewestAppVersionForIOS();

    ResponseVO listAppVersion(PageDTO pageDTO);
}
