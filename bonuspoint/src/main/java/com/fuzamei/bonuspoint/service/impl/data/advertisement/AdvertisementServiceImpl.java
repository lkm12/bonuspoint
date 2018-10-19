package com.fuzamei.bonuspoint.service.impl.data.advertisement;

import com.fuzamei.bonuspoint.dao.data.advertisement.AdvertisementMapper;
import com.fuzamei.bonuspoint.entity.dto.data.advertisement.AdvertisementDTO;
import com.fuzamei.bonuspoint.entity.po.data.advertisement.AdvertisementPO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.data.advertisement.AdvertisementService;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-09-14 10:35
 **/
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementMapper advertisementMapper;

    @Autowired
    public AdvertisementServiceImpl(AdvertisementMapper advertisementMapper) {
        this.advertisementMapper = advertisementMapper;
    }

    @Override
    public ResponseVO addAdvertisement(AdvertisementDTO advertisementDTO) {
        AdvertisementPO advertisementPO = new AdvertisementPO();
        BeanUtils.copyProperties(advertisementDTO,advertisementPO);
        advertisementPO.setCreatedAt(TimeUtil.timestamp());
        advertisementPO.setUpdatedAt(TimeUtil.timestamp());
        int result = advertisementMapper.insertSelective(advertisementPO);
        if (result == 1){
            return new ResponseVO(CommonResponseEnum.ADD_SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.ADD_FAIL);
    }

    @Override
    public ResponseVO deleteAdvertisement(Integer id) {
        AdvertisementPO advertisementPO = new AdvertisementPO();
        advertisementPO.setId(id);
        advertisementPO.setIsDelete(1);
        int result = advertisementMapper.updateByPrimaryKeySelective(advertisementPO);
        if (result == 1){
            return new ResponseVO(CommonResponseEnum.DELETE_SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.DELETE_FAIL);
    }

    @Override
    public ResponseVO listAdvertisement() {
        AdvertisementPO advertisementPO = new AdvertisementPO();
        advertisementPO.setIsDelete(0);
        List<AdvertisementPO> list = advertisementMapper.select(advertisementPO);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,list);
    }

    @Override
    public ResponseVO updateAdvertisement(AdvertisementDTO advertisementDTO) {
        AdvertisementPO advertisementPO = new AdvertisementPO();
        BeanUtils.copyProperties(advertisementDTO,advertisementPO);
        advertisementPO.setUpdatedAt(TimeUtil.timestamp());
        int result = advertisementMapper.updateByPrimaryKeySelective(advertisementPO);
        if (result == 1){
            return new ResponseVO(CommonResponseEnum.UPDATE_SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.UPDATE_FALL);
    }
}
