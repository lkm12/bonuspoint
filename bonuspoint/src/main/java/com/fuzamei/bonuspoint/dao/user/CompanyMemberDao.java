package com.fuzamei.bonuspoint.dao.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 购买商品成为会员
 *
 * @author liumeng
 * @create 2018年5月14日
 */
@Mapper
@Repository
public interface CompanyMemberDao {
    /**
     * 查看用户是否是会员
     *
     * @param uid       用户id
     * @param companyId 集团id
     * @return
     */
    @Select("select count(0) from bp_company_member where company_id = #{companyId} and member_id = #{uid}")
    Boolean isMember(@Param("uid") Long uid, @Param("companyId") Long companyId);

    /**
     * 添加集团会员
     *
     * @param uid       用户uid
     * @param companyId 集团id
     * @param joinAt    加入时间
     * @return
     */
    @Insert("insert into bp_company_member(company_id,member_id,join_at) values(#{companyId},#{uid},#{joinAt})")
    int saveMember(@Param("uid") Long uid, @Param("companyId") Long companyId, @Param("joinAt") Long joinAt);


    /**
     * 查集团会员数
     * @param companyId 集团信息id
     * @param time 成为会员时间
     * @return
     */
    @Select("select count(*) from bp_company_member where company_id = #{companyId} and join_at > #{time}")
    int countMember(@Param("companyId") Long companyId,@Param("time") Long time);

    /**
     * 统计会员活跃数据
     * @param companyId 集团信息数据
     * @param time 会员登录时间
     * @return
     */
    @Select("select count(*) from bp_company_member inner join bp_user on member_id = bp_user.id where company_id = #{companyId} and login_at >#{time}")
    int countActivityMember(@Param("companyId") Long companyId,@Param("time") Long time);
}
