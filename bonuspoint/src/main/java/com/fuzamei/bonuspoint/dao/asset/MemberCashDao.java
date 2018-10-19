package com.fuzamei.bonuspoint.dao.asset;

import com.fuzamei.bonuspoint.entity.vo.asset.MemberCashRecordVO;
import com.fuzamei.bonuspoint.sql.asset.MemberCashSqlFactory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/23
 */
@Mapper
@Repository
public interface MemberCashDao {

    /**
     * @param uid 用户id
     * @return MemberCashRecordVO
     * @author qbanxiaoli
     * @description 获取单个会员资产信息
     */
    @SelectProvider(type = MemberCashSqlFactory.class, method = "getMemberCashRecord")
    MemberCashRecordVO getMemberCashRecord(Long uid);

    /**
     * @param uid 用户id
     * @return List<MemberCashRecordVO>
     * @author qbanxiaoli
     * @description 获取会员用户集团积分列表
     */
    @SelectProvider(type = MemberCashSqlFactory.class, method = "listMemberPointCashRecord")
    List<MemberCashRecordVO> listMemberPointCashRecord(Long uid);

    /**
     * @param uid 会员id
     * @return List<MemberCashRecordVO>
     * @author qbanxiaoli
     * @description 获取会员用户集团积分详情
     */
    @SelectProvider(type = MemberCashSqlFactory.class, method = "getMemberPointCashRecordDetail")
    List<MemberCashRecordVO>  getMemberPointCashRecordDetail(Long uid);
}
