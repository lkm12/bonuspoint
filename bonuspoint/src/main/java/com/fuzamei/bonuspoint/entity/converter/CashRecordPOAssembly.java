package com.fuzamei.bonuspoint.entity.converter;

import com.fuzamei.bonuspoint.entity.dto.asset.CompanyCashRecordDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.asset.CashRecordPO;
import com.fuzamei.bonuspoint.util.TimeUtil;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/19 19:05
 */
public class CashRecordPOAssembly {

    public static CashRecordPO toDomain(CompanyCashRecordDTO companyCashRecordDTO) {
        if (companyCashRecordDTO == null) {
            return null;
        }
        CashRecordPO cashRecordPO = new CashRecordPO();
        if (companyCashRecordDTO.getUid() != null) {
            cashRecordPO.setUid(companyCashRecordDTO.getUid());
        }
        if (companyCashRecordDTO.getAmount() != null) {
            cashRecordPO.setAmount(companyCashRecordDTO.getAmount());
        }
        cashRecordPO.setCreatedAt(String.valueOf(TimeUtil.timestamp()));
        cashRecordPO.setUpdatedAt(String.valueOf(TimeUtil.timestamp()));
        return cashRecordPO;
    }

    public static CashRecordPO toDomain(BigDecimal amount, AccountPO userPO) {
        if (amount == null || userPO == null) {
            return null;
        }
        CashRecordPO cashRecordPO = new CashRecordPO();
        if (userPO.getPId() != null) {
            cashRecordPO.setUid(userPO.getPId());
        }
        if (userPO.getPId() != null) {
            cashRecordPO.setOppositeUid(userPO.getPId());
        }
        cashRecordPO.setAmount(amount);
        cashRecordPO.setCreatedAt(String.valueOf(TimeUtil.timestamp()));
        cashRecordPO.setUpdatedAt(String.valueOf(TimeUtil.timestamp()));
        return cashRecordPO;
    }

}
