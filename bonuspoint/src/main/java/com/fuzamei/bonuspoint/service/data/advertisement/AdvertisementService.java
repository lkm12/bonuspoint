package com.fuzamei.bonuspoint.service.data.advertisement;

import com.fuzamei.bonuspoint.entity.dto.data.advertisement.AdvertisementDTO;
import com.fuzamei.common.model.vo.ResponseVO;

public interface AdvertisementService {

    
    ResponseVO addAdvertisement(AdvertisementDTO advertisementDTO);

    ResponseVO deleteAdvertisement(Integer id);

    ResponseVO listAdvertisement();

    ResponseVO updateAdvertisement(AdvertisementDTO advertisementDTO);
}
