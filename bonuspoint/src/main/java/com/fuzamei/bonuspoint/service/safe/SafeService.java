package com.fuzamei.bonuspoint.service.safe;


import com.fuzamei.bonuspoint.entity.dto.account.SecrecyDTO;
import com.fuzamei.common.model.vo.ResponseVO;


/**
 * @program: bonuspoint
 * @description:
 * @author: WangJie
 * @create: 2018-04-17 14:10
 **/

public interface SafeService {

    ResponseVO getSafeStatusInfo(Long uid);

    ResponseVO updatePassword(SecrecyDTO secrecyDTO);

    ResponseVO updatePayWord(SecrecyDTO secrecyDTO);

    boolean hasMobile(Long uid);

    boolean hasEmail(Long uid);

    ResponseVO updateMobile(SecrecyDTO secrecyDTO);

    ResponseVO updateEmail(SecrecyDTO secrecyDTO);
}
