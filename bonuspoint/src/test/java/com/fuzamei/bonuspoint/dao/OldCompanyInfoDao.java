package com.fuzamei.bonuspoint.dao;

import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OldCompanyInfoDao {

    @Select("select * from ld_company_info ")
    List<CompanyInfoPO> getAllCompanyInfo();


    @Select("select id from ld_company_info where uid = #{uid}")
    Long getCompanyIdByUid(Long uid);

    @Select("select uid from ld_company_info where id = #{id}")
    Long getCompanyUidById(Long id);

}
