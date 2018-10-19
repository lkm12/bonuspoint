package com.fuzamei.bonuspoint.dao.user;

import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.sql.user.CompanyInfoSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description: 集团（商户）信息
 * @author: WangJie
 * @create: 2018-04-24 22:09
 **/
@Mapper
@Repository
public interface CompanyInfoDao {

    /**
     * 模糊查询集团（商户）信息
     *
     * @param fields     需要查询的字段数组
     * @param companyInfoDTO
     * @return
     * @author wangjie
     */
    @SelectProvider(type = CompanyInfoSqlFactory.class, method = "getCompanyInfoSql")
    List<CompanyInfoDTO> getCampanyInfoList(List<String> fields, CompanyInfoDTO companyInfoDTO);

    @InsertProvider(type = CompanyInfoSqlFactory.class, method = "addCompanyInfoSql")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int addCompanyInfo(CompanyInfoPO companyInfoPO);

    @UpdateProvider(type = CompanyInfoSqlFactory.class, method = "updateCompanyInfoSql")
    int updateCompanyInfo(CompanyInfoDTO companyInfoDTO);

    /**
     * 获取单个集团信息
     *
     * @param uid uid
     * @return PO
     */
    @Select("select * from bp_company_info where uid = #{uid}")
    CompanyInfoPO getCompanyInfo(Long uid);

    /**
     * 通过集团id获取集团所属用户的公私钥
     */
    @Select("select bp_user.id , private_key , public_key from bp_company_info inner join bp_user on bp_company_info.uid = bp_user.id where bp_company_info.id = #{companyId}")
    KeyDTO getCompanyKeyByCompanyId(Long companyId);

    /**
     * 利用集团id查询管理用户id
     *
     * @param companyId 集团id
     * @return
     */
    @Select("select uid from bp_company_info where id = #{companyId} ")
    Long queryUserIdByCompanyId(Long companyId);

    /**
     * 查询管理员管理的集团Id
     * @param uid 管理员
     * @return
     */
    @Select("SELECT id from bp_company_info where uid = #{uid}")
    Long queryUserManagerCompanyId(Long uid);

    /**
     * 通过id 查询商品信息
     * @param id id
     * @return
     */
    @Select("select * from bp_company_info where id = #{id} ")
    CompanyInfoPO getCompanyInfoById(Long id);

    /**
     * app 商家预览
     * @return
     */
    @Select(" SELECT company.id,company.company_name,logo_url ," +
            " company.company_address,company.company_telephone , company.company_email " +
            " FROM bp_company_info AS company where company_status != 0" +
            " WHERE uuser.p_id = #{pid}")
    List<CompanyInfoPO> previewCompany(Long pid);



    /**
     * 获取集团所属平台id
     * @param uid 集团管理员id
     * @return
     */
    @Select("SELECT platform.id FROM bp_user AS uuser  " +
            "LEFT JOIN bp_platform_info AS platform ON platform.uid = uuser.p_id " +
            "WHERE uuser.id = #{uid} ")
    Long queryCompanyPlatformId(Long uid);

    /**
     * 删除商户
     * @param platformUid 商户所属平台管理员id
     * @param companyId 商户id
     * @return
     */
    @Update("update bp_company_info , bp_user set company_status = 0   where  bp_company_info.id = #{companyId} and bp_user.id = bp_company_info.uid and bp_user.p_id = #{platformUid}")
    int deleteCompany(@Param("platformUid") Long platformUid,@Param("companyId") Long companyId);
}
