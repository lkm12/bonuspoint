package com.fuzamei.bonuspoint.service.captcha;

import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.common.Message;
import com.fuzamei.common.model.vo.ResponseVO;

public interface CaptchaService {

    ResponseVO saveCaptcha(Message message );

    ResponseVO saveCaptcha(Message message , AccountDTO accountDTO);

    boolean checkCaptcha(Message message );
}
