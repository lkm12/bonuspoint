package com.fuzamei.bonuspoint.service.asset;

import com.fuzamei.bonuspoint.entity.dto.asset.CompanyCashRecordDTO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.CompanyCashRecordVO;
import com.fuzamei.common.bean.PageBean;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/17
 */
public interface CompanyCashService {

    /**
     * @param companyCashRecordDTO 充值记录分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团充值记录
     */
    ResponseVO<PageBean<CompanyCashRecordVO>> getRechargeCashRecord(CompanyCashRecordDTO companyCashRecordDTO);

    /**
     * @param companyCashRecordDTO 提现记录分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团提现记录
     */
    ResponseVO<PageBean<CompanyCashRecordVO>> getWithdrawCashRecord(CompanyCashRecordDTO companyCashRecordDTO);

    /**
     * @param uid 用户id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个集团资产信息
     */
    ResponseVO<CompanyCashRecordVO> getCompanyCashRecord(Long uid);

    /**
     * @param uid 集团id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取备付金信息
     */
    ResponseVO<CompanyInfoPO> getProvisionsCashRecord(Long uid);

    /**
     * @param companyCashRecordDTO 充值信息数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 备付金充值
     */
    ResponseVO saveRechargeCashRecord(CompanyCashRecordDTO companyCashRecordDTO);

    /**
     * @param companyCashRecordDTO 提现信息数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 集团提现
     */
    ResponseVO saveWithdrawCashRecord(CompanyCashRecordDTO companyCashRecordDTO);

    /**
     * 获取集团备付金流水
     * lkm
     * @param companyCashRecordDTO
     * @return
     */
    ResponseVO getCompanyCashFlow(CompanyCashRecordDTO companyCashRecordDTO);
}

