package com.fuzamei.bonuspoint.dao.point;

import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.dto.point.PointRecordDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.QueryUserDTO;
import com.fuzamei.bonuspoint.entity.po.data.excel.ExcelPO;
import com.fuzamei.bonuspoint.entity.po.point.MemberPointPO;
import com.fuzamei.bonuspoint.entity.po.point.PointPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.vo.point.APP.CompanyPointAPPVO;
import com.fuzamei.bonuspoint.entity.vo.point.APP.GeneralPointRecordAPPVO;
import com.fuzamei.bonuspoint.entity.vo.point.APP.PointRecordAPPVO;
import com.fuzamei.bonuspoint.sql.point.MemberPointSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 18519 on 2018/5/5.
 * lkm
 */
@Mapper
@Repository
public interface MemberPointDao {


    /**
     * 通过集团id与userid查找对应的积分信息
     * lkm
     */
    @SelectProvider(type = MemberPointSqlFactory.class, method = "findCompanyPointByUserIdAndCompanyId")
    List<PointPO> findCompanyPointByUserIdAndCompanyId(Long id, Long groupId);


    /**
     * 修改发送者中数量不为0的固定集团的积分
     */
    @UpdateProvider(type = MemberPointSqlFactory.class, method = "updatePointByPointIdAndUserId")
    Integer updatePointByPointIdAndUserId(PointPO pointPO);

    @UpdateProvider(type = MemberPointSqlFactory.class, method = "updatePointOpByPointIdAndUserId")
    Integer updatePointOpByPointIdAndUserId(PointPO po);

    /**
     * 会员用户查询集团积分使用明细
     * from bp_point_record
     *
     * @param queryPointDTO
     * @return
     * @wangjie
     */
    @SelectProvider(type = MemberPointSqlFactory.class, method = "queryCompanyPointRecord")
    List<PointRecordDTO> queryCompanyPointRecord(QueryPointDTO queryPointDTO);

    /**
     * 会员用户查通用积分使用明细
     * from bp_point_record
     *
     * @param queryPointDTO
     * @return
     * @wangjie
     */
    @SelectProvider(type = MemberPointSqlFactory.class, method = "queryGeneralPointRecord")
    List<PointRecordDTO> queryGeneralPointRecord(QueryPointDTO queryPointDTO);


    /**
     * 根据uid查找用户
     * lkm
     * @param uid
     * @return
     */
    @Select("select * from bp_user where id = #{uid}")
    UserPO findUserById(Long uid);

    @Select("SELECT bp_company_info.id,bp_company_info.company_name,bp_company_info.point_rate,a.public_key ,a.headimgurl," +
            "SUM(bp_point_relation.num) AS num,ROUND(SUM(bp_point_relation.num)/bp_company_info.point_rate,4) AS numExchange FROM " +
            "bp_company_info " +
            "LEFT JOIN bp_point_info ON bp_company_info.id = bp_point_info.company " +
            "LEFT JOIN bp_point_relation ON bp_point_relation.point_id = bp_point_info.id " +
            "LEFT JOIN bp_user ON bp_user.id = bp_point_relation.user_id " +
            "LEFT JOIN bp_user a ON a.id =  bp_company_info.uid " +
            "WHERE bp_user.id = #{uid} GROUP BY bp_point_info.company")
    List<CompanyPointAPPVO> findTranpointByUidCompany(Long uid);

    /**
     * 通过用户id查找其对应的通用积分数量
     * lkm
     * @param uid
     * @return
     */
    @Select("SELECT num FROM  bp_general_point_relation WHERE user_id = #{uid}")
    String findGeneralPointByUserId(Long uid);

    @Select("<script>" +
            "SELECT bp_general_point_record.id,num,bp_general_point_record.updated_at as updateTime,hash,height, bp_platform_info.point_rate,bp_general_point_record.platform_point_rate," +
            "ROUND(num * bp_general_point_record.platform_point_rate,4) AS numCash FROM bp_general_point_record " +
            "left join bp_user on bp_user.id = bp_general_point_record.uid " +
            "left join bp_platform_info on bp_platform_info.uid = bp_user.p_id " +
            "<where> " +
            "category = 4 AND bp_general_point_record.uid = #{id}  " +
            "<if test='startTime != null'>" +
            " AND updated_at &gt;= #{startTime} " +
            "</if>" +
            "<if test='endTime != null'>" +
            " AND updated_at &lt;= #{endTime}" +
            "</if>" +
            "</where>" +
            "</script>")
    List<PointPO> findexchangeInfoByUidAndGroupId(PagePointDTO pagePointDTO);

    /**
     * 获取所有会员的会员积分数量
     * @param queryUserDTO
     * @return
     */
    @SelectProvider(type = MemberPointSqlFactory.class, method = "getMemberPointInfoListLikeMobile")
    List<MemberPointPO> getMemberPointInfoListLikeMobile(QueryUserDTO queryUserDTO);


    /**
     * 通过集团id与userid查找对应的有效期为永久的积分信息
     * <p>
     * lkm
     */
    @Select("select point_rate,company,bp_point_relation.num AS num,end_at AS endTime,point_id,user_id,num_used,bp_point_info.id " +
            "from bp_point_info " +
            "left join bp_point_relation ON bp_point_info.id = point_id " +
            "left join bp_company_info ON bp_company_info.id = company " +
            "where user_id = #{uid} and company = #{groupId} and end_at = 0")
    List<PointPO> findCompanyPointByUserIdAndCompanyIdTwo(@Param("uid") Long uid, @Param("groupId") Long groupId);



    @Select("SELECT id FROM bp_platform_info WHERE uid = #{uid}")
    Long findPlatformIdByPlatformUid(ExchangePointDTO exchangeDTO);

    /**
     * 查询一个平台下的所有积分兑换记录（平台）
     *
     * @param
     * @return
     */
    @Select("<script>" +
            " SELECT bp_point_record.id AS recordId,bp_user.mobile,bp_point_info.name,bp_company_info.company_name,bp_point_record.hash,bp_point_record.height, " +
            " bp_point_record.num AS pointNum, ROUND ((bp_point_record.num / bp_point_record.point_rate) * bp_point_record.platform_point_rate,4) AS cashNum," +
            " ROUND (bp_point_record.num / bp_point_record.point_rate,4) AS generalPoint,bp_point_record.platform_point_rate AS platformPointRate, bp_point_record.point_rate," +
            " bp_point_record.updated_at AS updateTime FROM bp_point_record  " +
            " LEFT JOIN bp_user ON bp_point_record.uid = bp_user.id " +
            " LEFT JOIN bp_point_info ON bp_point_info.id = bp_point_record.point_id " +
            " LEFT JOIN bp_company_info ON bp_company_info.uid = bp_point_record.opposite_uid " +
            " LEFT JOIN bp_platform_info ON bp_platform_info.uid = bp_user.p_id " +
            " <where> " +
            " bp_company_info.uid IN (SELECT id FROM bp_user WHERE p_id = #{id}) " +
            " and bp_point_record.category = 4 <if test='mobile != null'> AND mobile = #{mobile} </if> <if test = 'companyName != null'> " +
            " and bp_company_info.company_name  = #{companyName} </if> " +
            " <if test='startTime != null'>  AND bp_point_record.updated_at  &gt;= #{startTime} </if>" +
            " <if test='endTime != null'> AND bp_point_record.updated_at &lt;= #{endTime} </if> " +
            " </where>" +
            "</script>")
    List<ExcelPO> findExchangeGeneralByPlatfromId(PagePointDTO pagePointDTO);

    /**
     * 查询总平台下的所有积分兑换记录（总平台）
     *
     * @param
     * @return
     */
    @Select("<script>" +
            " SELECT bp_point_record.id AS recordId,bp_user.mobile,bp_point_info.name,company_name, " +
            " bp_point_record.num AS pointNum," +
            " ROUND (bp_point_record.num / bp_point_record.point_rate,4) AS generalPoint,bp_point_record.point_rate, " +
            " bp_point_record.updated_at AS updateTime FROM bp_point_record " +
            " LEFT JOIN bp_user ON bp_point_record.uid = bp_user.id " +
            " LEFT JOIN bp_point_info ON bp_point_info.id = bp_point_record.point_id " +
            " LEFT JOIN bp_company_info ON bp_company_info.uid = bp_point_record.opposite_uid " +
            " <where> " +
            " bp_point_record.category = 4 <if test='mobile != null'> AND bp_user.mobile LIKE CONCAT('%',#{mobile},'%') </if> <if test='companyName != null'> and " +
            " bp_company_info.company_name LIKE CONCAT('%',#{companyName},'%') </if>" +
            " <if test='startTime != null'>  AND bp_point_record.updated_at  &gt;= #{startTime} </if>" +
            " <if test='endTime != null'> AND bp_point_record.updated_at &lt;= #{endTime} </if> " +
            " </where>" +
            "</script>")
    List<ExcelPO> findExchangeGeneralBig(PagePointDTO pagePointDTO);

    @Select("SELECT bp_point_record.num,TYPE,bp_point_record.created_at,bp_company_info.company_name as name FROM bp_point_record " +
            "LEFT JOIN bp_user ON bp_user.id = bp_point_record.uid " +
            "LEFT JOIN bp_point_info ON bp_point_info.id = bp_point_record.point_id " +
            "LEFT JOIN bp_company_info ON bp_point_info.company = bp_company_info.id WHERE bp_point_record.uid = #{id} AND  "+
            "role = 4")
    List<ExchangePointDTO> findUserPointInfo(PagePointDTO pagePointDTO);

    @Select("SELECT bp_general_point_record.num,TYPE,bp_general_point_record.created_at,bp_platform_info.platform_name as name FROM bp_general_point_record " +
            "LEFT JOIN bp_user ON bp_user.id = bp_general_point_record.uid " +
            "LEFT JOIN bp_platform_info ON bp_platform_info.uid = bp_user.p_id  " +
            "WHERE bp_general_point_record.uid = #{id} AND " +
            " role = 4")
    List<ExchangePointDTO> findUserGeneralPointInfo(PagePointDTO pagePointDTO);


    @Update("update bp_general_point_relation set num = #{num} where platform_id = #{platformId} and user_id = #{uid}")
    Integer updateGeneralPointByPlatformIdAndUserIdDan(ExchangePointDTO exchangeDTO);

    /**
     * 查出某个集团积分明细的使用详情
     * @param exchangePointDTO
     * @return
     */
    @Select("SELECT bp_point_record.id,num," +
            "(CASE WHEN TYPE = 2 THEN '支出' ELSE '收入'  END) AS typeStr," +
            "bp_user.public_key,bp_user.headimgurl,memo,bp_point_record.created_at,bp_point_record.height,bp_point_record.hash FROM bp_point_record " +
            "LEFT JOIN bp_user ON bp_point_record.opposite_uid = bp_user.id " +
            "WHERE bp_point_record.id = #{id}")
    PointRecordAPPVO findPointListInfoCompany(ExchangePointDTO exchangePointDTO);
    /**
     * 查出某个通用积分明细的使用详情
     * @param exchangePointDTO
     * @return
     */
    @Select("SELECT bp_general_point_record.id,num,(CASE WHEN TYPE = 2 THEN '支出' ELSE '收入'  END) AS typeStr," +
            "bp_user.public_key,bp_user.headimgurl,memo,bp_general_point_record.created_at,bp_general_point_record.height,bp_general_point_record.hash FROM bp_general_point_record " +
            "LEFT JOIN bp_user ON bp_general_point_record.opposite_uid = bp_user.id " +
            "WHERE bp_general_point_record.id = #{id}")
    GeneralPointRecordAPPVO findPointListInfoGeneral(ExchangePointDTO exchangePointDTO);

    /**
     * 根据积分id修改已使用的积分
     * @param pointId
     * @param numberUsed
     */
    @Update("UPDATE bp_point_info SET num_used = #{numberUsed} WHERE id = #{pointId}")
    Integer updateUserdPointByPointId(@Param("pointId") Long pointId, @Param("numberUsed") BigDecimal numberUsed);

    /**
     * 根据uid查找集团名称
     * @param uid
     * @return
     */
    @Select("SELECT company_name FROM bp_company_info " +
            "LEFT JOIN bp_user ON bp_user.id = bp_company_info.uid " +
            "WHERE bp_user.id = #{uid} ")
    String findCompanyNameByUid(Long uid);

    /**
     * 获取用户对应集团的可用会员积分(lmm)
     * @param uid uid
     * @param gid 集团id
     * @return
     */

    @Select("SELECT SUM(relation.num) as nums FROM bp_point_relation AS relation " +
            "LEFT JOIN bp_point_info AS point ON relation.point_id = point.id " +
            "WHERE point.status = 1 AND relation.user_id =#{uid} AND point.company = #{gid} " +
            "GROUP BY relation.user_id , point.company ")
    BigDecimal getCompanyPointSum(@Param("uid") Long uid ,@Param("gid") Long gid);

    @Update("<script>" +
            " update bp_point_relation set num = 0 " +
            " where user_id and point_id in (" +
            "<foreach collection='list' item='po' separator=','>" +
            "#{po.userId},#{po.pointId}" +
            "</foreach>" +
            ")" +
            "</script>")
    Integer updatePointList(List<PointPO> li1);
}
