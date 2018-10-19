package com.fuzamei.bonuspoint.service.asset;

import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.MemberCashRecordVO;

import java.util.List;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
public interface MemberCashService {

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个会员资产信息
     */
    ResponseVO<MemberCashRecordVO> getMemberCashRecord(Long uid);

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表
     */
    ResponseVO<List<MemberCashRecordVO>> listMemberPointCashRecord(Long uid);

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分详情
     */
    ResponseVO<List<MemberCashRecordVO>> getMemberPointCashRecordDetail(Long uid);
}

