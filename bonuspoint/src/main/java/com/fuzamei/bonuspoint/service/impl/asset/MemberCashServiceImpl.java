package com.fuzamei.bonuspoint.service.impl.asset;

import com.fuzamei.bonuspoint.dao.asset.MemberCashDao;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.asset.MemberCashRecordVO;
import com.fuzamei.bonuspoint.service.asset.MemberCashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@Slf4j
@Service
@Transactional
public class MemberCashServiceImpl implements MemberCashService {

    private final MemberCashDao memberCashDao;

    @Autowired
    public MemberCashServiceImpl(MemberCashDao memberCashDao) {
        this.memberCashDao = memberCashDao;
    }

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取单个会员资产信息
     */
    @Override
    public ResponseVO<MemberCashRecordVO> getMemberCashRecord(Long uid) {
        //获取单个会员资产信息
        MemberCashRecordVO memberCashRecordVO = memberCashDao.getMemberCashRecord(uid);
        //返回数据
        log.info("获取单个会员资产信息成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, memberCashRecordVO);
    }

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表
     */
    @Override
    public ResponseVO<List<MemberCashRecordVO>> listMemberPointCashRecord(Long uid) {
        //获取会员用户集团积分列表
        List<MemberCashRecordVO> memberCashRecordVOList = memberCashDao.listMemberPointCashRecord(uid);
        //返回数据
        log.info("获取会员用户集团积分列表成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, memberCashRecordVOList);
    }

    /**
     * @param uid 会员id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取会员用户集团积分详情
     */
    @Override
    public ResponseVO<List<MemberCashRecordVO>> getMemberPointCashRecordDetail(Long uid) {
        //获取会员用户集团积分详情
        List<MemberCashRecordVO> memberCashRecordVOList = memberCashDao.getMemberPointCashRecordDetail(uid);
        //返回数据
        log.info("会员用户获取自己的集团积分详情成功");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, memberCashRecordVOList);
    }

}
