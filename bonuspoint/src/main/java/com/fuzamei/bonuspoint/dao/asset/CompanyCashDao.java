package com.fuzamei.bonuspoint.dao.asset;

import com.fuzamei.bonuspoint.entity.dto.asset.CompanyCashRecordDTO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.vo.asset.CashFlowVO;
import com.fuzamei.bonuspoint.entity.vo.asset.CompanyCashRecordVO;
import com.fuzamei.bonuspoint.sql.asset.CompanyCashSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/17
 */
@Mapper
@Repository
public interface CompanyCashDao {

    /**
     * @param companyCashRecordDTO 充值记录分页查询数据传输类
     * @return List<CompanyCashRecordVO>
     * @author qbanxiaoli
     * @description 获取单个集团充值记录
     */
    @SelectProvider(type = CompanyCashSqlFactory.class, method = "getRechargeCashRecord")
    List<CompanyCashRecordVO> getRechargeCashRecord(CompanyCashRecordDTO companyCashRecordDTO);

    /**
     * @param companyCashRecordDTO 提现记录分页查询数据传输类
     * @return List<CompanyCashRecordVO>
     * @author qbanxiaoli
     * @description 获取单个集团提现记录
     */
    @SelectProvider(type = CompanyCashSqlFactory.class, method = "getWithdrawCashRecord")
    List<CompanyCashRecordVO> getWithdrawCashRecord(CompanyCashRecordDTO companyCashRecordDTO);

    /**
     * @param uid 用户id
     * @return CompanyCashRecordVO
     * @author qbanxiaoli
     * @description 获取单个集团资产信息
     */
    @SelectProvider(type = CompanyCashSqlFactory.class, method = "getCompanyCashRecord")
    CompanyCashRecordVO getCompanyCashRecord(Long uid);

    /**
     * @param uid 用户id
     * @return CompanyInfoPO
     * @author qbanxiaoli
     * @description 获取备付金信息
     */
    @SelectProvider(type = CompanyCashSqlFactory.class, method = "getProvisionsCashRecord")
    CompanyInfoPO getProvisionsCashRecord(Long uid);
    @Select("<script>" +
            "SELECT bp_cash_record.id, bp_platform_info.platform_name AS oppositeName , " +
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
            "<where>" +
            " bp_cash_record.uid = #{uid} " +
            " <if test='startTime != null'>  AND bp_cash_record.updated_at  &gt;= #{startTime} </if>" +
            " <if test='endTime != null'> AND bp_cash_record.updated_at &lt;= #{endTime} </if> " +
            "</where> " +
            "</script>")
    List<CashFlowVO> getCompanyCashFlow(CompanyCashRecordDTO companyCashRecordDTO);
}
