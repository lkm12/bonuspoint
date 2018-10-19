/**
 * FileName: CompanyService
 * Author: wangtao
 * Date: 2018/4/25 16:18
 * Description:
 */
package com.fuzamei.bonuspoint.service.point;

import com.fuzamei.bonuspoint.entity.dto.data.excel.GoodExcelDTO;
import com.fuzamei.bonuspoint.entity.dto.point.*;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.point.CompanyPointVO;
import com.fuzamei.common.bean.PageBean;

import java.math.BigDecimal;

/**
 * @author wangtao
 * @create 2018/4/25
 */

public interface CompanyPointService {

    /**
     * @param uid 集团id
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取通用积分结算信息
     */
    ResponseVO<CompanyPointVO> getBalanceInfoRecord(Long uid);

    /**
     * @param companyPointDTO 集团积分数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取集团已兑换通用积分记录
     */
    ResponseVO<PageBean<GeneralPointRecordPO>> listPointExchangeRecord(CompanyPointDTO companyPointDTO);

    /**
     * @param companyPointDTO 集团积分数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 集团发放积分
     */
    ResponseVO sendPointToUser(CompanyPointDTO companyPointDTO);

    /**
     * @param companyPointDTO 集团积分数据传输类
     * @return 请求响应
     * @author qbanxiaoli
     * @description 结算通用积分
     */
    ResponseVO balanceCommonPoint(CompanyPointDTO companyPointDTO);

    /**
     * 申请发行积分
     *
     * @param uid           uid
     * @param applyPointDTO 数据传输类
     * @return 响应类
     */
    ResponseVO releasePoint(Long uid, ApplyPointDTO applyPointDTO);

    /**
     * 获取用户管理集团积分资产信息
     *
     * @param uid 用户id
     * @return
     */
    ResponseVO companyPointAsset(Long uid);

    /**
     * 集团已发放积分列表
     *
     * @param uid            uid
     * @param pointRecordDTO 数据传输类
     * @return 响应类
     */
    ResponseVO grantPointList(Long uid, PointRecordDTO pointRecordDTO);

    /**
     * 集团查看积分发行记录
     *
     * @param queryPointDTO
     * @return
     * @wangjie
     */
    ResponseVO pointIssueList(QueryPointDTO queryPointDTO);

    /**
     * 列出集团活动信息
     *
     * @param uid         集团管理用户
     * @param showOutTime 是否包括已过期活动
     * @return
     */
    ResponseVO listActivity(Long uid, Boolean showOutTime);

    /**
     * 集团申请积分转换
     * @param companyId 集团id
     * @param nums 申请积分数量
     */
    void tranterReleasePoint(Long companyId, BigDecimal nums);

    /**
     * 用户积分兑换记录（商户）
     * @param goodExcelDTO
     * @return
     */
    ResponseVO exchangeGeneralCompany(GoodExcelDTO goodExcelDTO);
}
