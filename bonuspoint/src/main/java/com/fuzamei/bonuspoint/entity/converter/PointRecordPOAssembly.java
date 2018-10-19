package com.fuzamei.bonuspoint.entity.converter;

import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import com.fuzamei.bonuspoint.entity.po.point.PointRecordPO;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;

/**
 * @author Q版小李
 * @description
 * @create 2018/8/2 18:10
 */
public class PointRecordPOAssembly {

    public static PointRecordPO toDomain(Long oppositeUid, CompanyPointDTO companyPointDTO) {
        if (companyPointDTO == null) {
            return null;
        }
        PointRecordPO pointRecordPO = new PointRecordPO();
        if (oppositeUid != null) {
            pointRecordPO.setOppositeUid(oppositeUid);
        }
        if (companyPointDTO.getUid() != null) {
            pointRecordPO.setUid(companyPointDTO.getUid());
        }
        if (companyPointDTO.getPointId() != null) {
            pointRecordPO.setPointId(companyPointDTO.getPointId());
        }
        if (companyPointDTO.getNum() != null) {
            pointRecordPO.setNum(companyPointDTO.getNum());
        }
        if (StringUtil.isNoneBlank(companyPointDTO.getMemo())) {
            pointRecordPO.setMemo(companyPointDTO.getMemo());
        }
        pointRecordPO.setCreatedAt(TimeUtil.timestamp());
        pointRecordPO.setUpdatedAt(TimeUtil.timestamp());
        return pointRecordPO;
    }

    public static PointRecordPO toDomain(PointRecordPO p) {
        if (p == null) {
            return null;
        }
        PointRecordPO pointRecordPO = new PointRecordPO();
        if (p.getOppositeUid() != null) {
            pointRecordPO.setUid(p.getOppositeUid());
        }
        if (p.getUid() != null) {
            pointRecordPO.setOppositeUid(p.getUid());
        }
        if (p.getPointId() != null) {
            pointRecordPO.setPointId(p.getPointId());
        }
        if (p.getPointRate() != null) {
            pointRecordPO.setPointRate(p.getPointRate());
        }
        if (p.getNum() != null) {
            pointRecordPO.setNum(p.getNum());
        }
        if (StringUtil.isNoneBlank(p.getMemo())) {
            pointRecordPO.setMemo(p.getMemo());
        }
        pointRecordPO.setCreatedAt(TimeUtil.timestamp());
        pointRecordPO.setUpdatedAt(TimeUtil.timestamp());
        return pointRecordPO;
    }

}
