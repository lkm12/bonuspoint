package com.fuzamei.bonuspoint.entity.converter;

import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import com.fuzamei.bonuspoint.entity.po.point.PointRecordPO;
import com.fuzamei.bonuspoint.entity.po.point.PointRelationPO;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;

/**
 * @author Q版小李
 * @description
 * @create 2018/8/2 18:48
 */
public class PointRelationPOAssembly {

    public static PointRelationPO toDomain(Long uid, CompanyPointDTO companyPointDTO) {
        if (companyPointDTO == null) {
            return null;
        }
        PointRelationPO pointRelationPO = new PointRelationPO();
        if (uid != null) {
            pointRelationPO.setUserId(uid);
        }
        if (companyPointDTO.getPointId() != null) {
            pointRelationPO.setPointId(companyPointDTO.getPointId());
        }
        if (companyPointDTO.getNum() != null) {
            pointRelationPO.setNum(companyPointDTO.getNum());
        }
        return pointRelationPO;
    }
}
