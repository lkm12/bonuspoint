package com.fuzamei.bonuspoint.entity.converter;

import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.bonuspoint.util.TimeUtil;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/24 上午9:39
 */
public class GeneralPointRecordPOAssembly {

    public static GeneralPointRecordPO toDomain(CompanyPointDTO companyPointDTO) {
        if (companyPointDTO == null) {
            return null;
        }
        GeneralPointRecordPO generalPointRecordPO = new GeneralPointRecordPO();
        if (companyPointDTO.getUid() != null) {
            generalPointRecordPO.setUid(companyPointDTO.getUid());
        }
        if (companyPointDTO.getNum() != null) {
            generalPointRecordPO.setNum(companyPointDTO.getNum());
        }
        if (companyPointDTO.getMemo() != null) {
            generalPointRecordPO.setMemo(companyPointDTO.getMemo());
        }
        generalPointRecordPO.setCreatedAt(TimeUtil.timestamp());
        generalPointRecordPO.setUpdatedAt(TimeUtil.timestamp());
        return generalPointRecordPO;
    }
}
