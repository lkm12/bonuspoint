package com.fuzamei.bonuspoint.service.user.company;

import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.vo.user.CompanyBaseInfoVO;
import com.fuzamei.common.model.vo.ResponseVO;

import java.util.List;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/14 14:56
 */
public interface CompanyInfoService {

    /**
     * 获取单个集团信息
     * @param uid uid
     * @return 请求响应
     */
    ResponseVO getCompanyInfo(Long uid);

    /**
     * 查看集团信息列表
     * @param companyInfoDTO
     * @return
     */
    ResponseVO getCompanyInfoList(CompanyInfoDTO companyInfoDTO);

    /**
     * 获取集团信息列表
     * @param
     * @return 请求响应
     */
   // ResponseVO listCompanyInfo();

    Long getCompanyIdByUid(Long uid);

    ResponseVO updateCompanyInfo(CompanyInfoDTO companyInfoDTO);

    /**
     * 平台设置集团备付金比例
     * @param companyInfoDTO
     * @return
     */
    ResponseVO setCompanyCashRate(CompanyInfoDTO companyInfoDTO);

    /**
     * 平台设置集团积分兑换比例
     * @param companyInfoDTO
     * @return
     */
    ResponseVO setCompanyPointRate(CompanyInfoDTO companyInfoDTO);

    /**
     * 平台添加集团
     * @param accountDTO 要添加的集团用户信息
     * @param companyInfoDTO 要添加的集团信息
     * @return
     */
    ResponseVO createCompany(AccountDTO accountDTO, CompanyInfoDTO companyInfoDTO);

    /**
     * 通过集团所属平台查平台下属集团备付金比例
     * @param companyInfoDTO
     * @return
     */
    ResponseVO getCompanyCashRateList(CompanyInfoDTO companyInfoDTO);

    /**
     * 通过集团所属平台查平台下属集团积分的兑换比例
     * @param companyInfoDTO
     * @return
     */
    ResponseVO getCompanyPointRateList(CompanyInfoDTO companyInfoDTO);

    ResponseVO<CompanyBaseInfoVO> getCompanyBaseInfo(Long uid);

    ResponseVO updateCompanyBaseInfo(CompanyInfoDTO companyInfoDTO);

    /**
     * 删除商家
     * @param platformUid 平台管理员id
     * @param companyId 商户id
     * @return
     */
    ResponseVO deleteCompany(Long platformUid, Long companyId);

    /**
     * 获取平台下属所有集团
     * @param platformUid 平台管理员id
     * @return
     */
    List<CompanyInfoDTO> getAllCompanyInSamePlatform(Long platformUid);
}
