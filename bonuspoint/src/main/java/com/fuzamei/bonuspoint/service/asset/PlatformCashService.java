package com.fuzamei.bonuspoint.service.asset;

import com.fuzamei.bonuspoint.entity.dto.asset.PlatformCashRecordDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.PlatformCashRecordVO;
import com.fuzamei.common.bean.PageBean;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/19
 */
public interface PlatformCashService {

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取会员用户资产信息
     */
    ResponseVO<PageBean<PlatformCashRecordVO>> listMemberCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取集团资产信息
     */
    ResponseVO<PageBean<PlatformCashRecordVO>> listCompanyCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团充值记录
     */
    ResponseVO<PageBean<PlatformCashRecordVO>> getRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取集团提现记录
     */
    ResponseVO<PageBean<PlatformCashRecordVO>> getWithdrawCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取备付金充值列表
     */
    ResponseVO listRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param uid 平台id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取平台资产信息
     */
    ResponseVO<PlatformCashRecordVO> getPlatformCashRecord(Long uid);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取集团列表
     */
    ResponseVO listCompany(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取备付金预警通知列表
     */
    ResponseVO<PageBean<PlatformCashRecordVO>> listProvisionsNotice(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取会员用户集团积分列表
     */
    ResponseVO listMemberPointCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取会员用户集团积分详情
     */
    ResponseVO getMemberPointCashRecordDetail(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 分页查询数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description  获取集团比例信息列表
     */
    ResponseVO<PageBean<PlatformCashRecordVO>> listCompanyRateCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 备付金审核通过
     */
    ResponseVO checkRechargeCashRecord(Long uid);

    /**
     * @param platformCashRecordDTO 审核信息数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 备付金审核拒绝
     */
    ResponseVO refuseRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * 获取平台备付金流水
     * @param uid
     * @return
     */
    ResponseVO getPlatformCashFlow(PlatformCashRecordDTO platformCashRecordDTO);
}
