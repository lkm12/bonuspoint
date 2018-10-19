package com.fuzamei.bonuspoint.dao.asset;

import com.fuzamei.bonuspoint.entity.dto.asset.PlatformCashRecordDTO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.vo.asset.CashFlowVO;
import com.fuzamei.bonuspoint.entity.vo.asset.PlatformCashRecordVO;
import com.fuzamei.bonuspoint.sql.asset.PlatformCashSqlFactory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
public interface PlatformCashDao {

    /**
     * @param platformCashRecordDTO 用户资产分页查询数据传输类
     * @return List<PlatformCashRecordVO>
     * @author qbanxiaoli
     * @description  获取会员用户资产信息
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "listMemberCashRecord")
    List<PlatformCashRecordVO> listMemberCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 集团资产分页查询数据传输类
     * @return List<PlatformCashRecordVO>
     * @author qbanxiaoli
     * @description  获取集团资产信息
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "listCompanyCashRecord")
    List<PlatformCashRecordVO> listCompanyCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 充值记录分页查询数据传输类
     * @return List<PlatformCashRecordVO>
     * @author qbanxiaoli
     * @description 获取集团充值记录
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "getRechargeCashRecord")
    List<PlatformCashRecordVO> getRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 提现记录分页查询数据传输类
     * @return List<PlatformCashRecordVO>
     * @author qbanxiaoli
     * @description  获取集团提现记录
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "getWithdrawCashRecord")
    List<PlatformCashRecordVO> getWithdrawCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 备付金充值分页查询数据传输类
     * @return List<CashRecordPO>
     * @author qbanxiaoli
     * @description  获取备付金充值列表
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "listRechargeCashRecord")
    List<CompanyInfoPO> listRechargeCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param uid 会员id
     * @return PlatformCashRecordVO
     * @author qbanxiaoli
     * @description 获取平台资产信息
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "getPlatformCashRecord")
    PlatformCashRecordVO getPlatformCashRecord(Long uid);

    /**
     * @param platformCashRecordDTO 集团列表分页查询数据传输类
     * @return List<CashRecordPO>
     * @author qbanxiaoli
     * @description  获取集团列表
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "listCompany")
    List<CompanyInfoPO> listCompany(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 备付金预警通知分页查询数据传输类
     * @return List<CashRecordPO>
     * @author qbanxiaoli
     * @description  获取备付金预警通知列表
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "listProvisionsNotice")
    List<PlatformCashRecordVO> listProvisionsNotice(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 集团积分分页查询数据传输类
     * @return List<CashRecordPO>
     * @author qbanxiaoli
     * @description  获取会员用户集团积分列表
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "listMemberPointCashRecord")
    List<CompanyInfoPO> listMemberPointCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 集团积分详情分页查询数据传输类
     * @return CashRecordPO
     * @author qbanxiaoli
     * @description  获取会员用户集团积分详情
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "getMemberPointCashRecordDetail")
    CompanyInfoPO getMemberPointCashRecordDetail(PlatformCashRecordDTO platformCashRecordDTO);

    /**
     * @param platformCashRecordDTO 集团比例信息分页查询数据传输类
     * @return List<CashRecordPO>
     * @author qbanxiaoli
     * @description  获取集团比例信息列表
     */
    @SelectProvider(type = PlatformCashSqlFactory.class, method = "listCompanyRateCashRecord")
    List<PlatformCashRecordVO> listCompanyRateCashRecord(PlatformCashRecordDTO platformCashRecordDTO);

    @Select("<script>" +
            "SELECT bp_cash_record.id, bp_platform_info.platform_name AS oppositeName , username,bp_company_info.company_name," +
            "CASE WHEN TYPE = 1 THEN '入金' WHEN TYPE = 2 THEN '出金' END AS typeStr," +
            "CASE WHEN category = 1 THEN '集团备付金充值' WHEN category = 2 THEN '集团支付备付金' WHEN category = 3 " +
            "THEN '平台收入备付金' WHEN category = 4 THEN '平台结算集团通用积分' WHEN category = 5 THEN '集团结算收入备用金' " +
            "WHEN category = 6 THEN '集团提现' END AS categoryStr , " +
            "bp_cash_record.amount," +
            "CASE WHEN bp_cash_record.status = 0 THEN '未审核' WHEN bp_cash_record.status = 1 THEN '通过审核' WHEN bp_cash_record.status = 2 THEN '成功' WHEN bp_cash_record.status = 3 THEN '失败' " +
            "END AS statusStr," +
            "bp_cash_record.reason," +
            "bp_cash_record.created_at,bp_cash_record.updated_at,bp_cash_record.height,bp_cash_record.hash FROM bp_cash_record " +
            "LEFT JOIN bp_platform_info ON bp_cash_record.opposite_uid = bp_platform_info.uid " +
            "left join bp_company_info on bp_cash_record.uid = bp_company_info.uid " +
            "left join bp_user on bp_user.id = bp_cash_record.uid " +
            "<where>" +
            " <if test='startTime != null'> bp_cash_record.updated_at  &gt;= #{startTime} </if>" +
            " <if test='endTime != null'> AND bp_cash_record.updated_at &lt;= #{endTime} </if> " +
            "</where>" +
            "</script>")
    List<CashFlowVO> getPlatformCashFlow(PlatformCashRecordDTO platformCashRecordDTO);
}
