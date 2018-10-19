/**
 * FileName: CompanyDao
 * Author: wangtao
 * Date: 2018/5/2 14:03
 * Description:
 */
package com.fuzamei.bonuspoint.dao.point;

import com.fuzamei.bonuspoint.entity.dto.data.excel.GoodExcelDTO;
import com.fuzamei.bonuspoint.entity.dto.point.CompanyPointDTO;
import com.fuzamei.bonuspoint.entity.po.data.excel.ExcelPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.bonuspoint.entity.vo.point.CompanyPointVO;
import com.fuzamei.bonuspoint.entity.vo.point.PointActivityVO;
import com.fuzamei.bonuspoint.sql.point.CompanyPointSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangtao
 * @create 2018/5/2
 */
@Mapper
@Repository
public interface CompanyPointDao {

    /**
     * @param uid 集团id
     * @return CompanyPointVO
     * @author qbanxiaoli
     * @description 获取结算信息
     */
    @SelectProvider(type = CompanyPointSqlFactory.class, method = "getBalanceInfoRecord")
    CompanyPointVO getBalanceInfoRecord(Long uid);

    /**
     * @param companyPointDTO 数据传输类
     * @return List<GeneralPointRecordPO>
     * @author qbanxiaoli
     * @description 获取集团已兑换通用积分记录
     */
    @SelectProvider(type = CompanyPointSqlFactory.class, method = "listPointExchangeRecord")
    List<GeneralPointRecordPO> listPointExchangeRecord(CompanyPointDTO companyPointDTO);

    /**
     * 根据集团用户id和交易密码哈希判断是否存在
     *
     * @param uid   集团管理用户id
     * @param payWordHash 交易密码哈希
     * @return 0 or 1
     */
    @Select("select count(*) from bp_user u " +
            "where u.id = #{uid} and u.payword_hash = #{payWordHash}")
    int isPayWord(@Param("uid") Long uid, @Param("payWordHash") String payWordHash);

    /**
     * 列出集团活动信息id
     * @param uid 集团管理UID
     * @param  showOutTime 是否显示过期活动
     * @return
     */
    @SelectProvider(type = CompanyPointSqlFactory.class, method = "listActivity")
    List<PointActivityVO> listActivity(Long uid,Boolean showOutTime);

    /**
     * 通过集团id查询商品兑换列表
     *lkm
     * @param
     * @return sql语句
     */
    @Select("select id from bp_company_info where uid = #{uid}")
    Long findGroupIdByUid(GoodExcelDTO goodExcelDTO);


    @Select("<script> " +
            " SELECT bp_point_record.id AS recordId,bp_user.mobile,a.public_key," +
            " bp_point_record.num AS pointNum, round((bp_point_record.num / bp_point_record.point_rate)* bp_point_record.platform_point_rate ,4) as cashNum ," +
            " bp_point_record.platform_point_rate as platformPointRate,"+
            " ROUND (bp_point_record.num / bp_point_record.point_rate,4) AS generalPoint,bp_point_record.point_rate,hash,height," +
            " bp_point_record.updated_at AS updateTime FROM bp_point_record " +
            " LEFT JOIN bp_user ON bp_point_record.uid = bp_user.id " +
            " LEFT JOIN bp_user a ON a.id = bp_point_record.uid " +
            " LEFT JOIN bp_point_relation ON bp_point_record.point_id = bp_point_relation.point_id AND bp_point_relation.user_id = " +
            " bp_point_record.uid " +
            " LEFT JOIN bp_point_info ON bp_point_info.id = bp_point_relation.point_id " +
            " left join bp_platform_info on bp_platform_info.uid = bp_user.p_id " +
            " <where> " +
            " bp_point_info.company = #{groupId} and bp_point_record.category = 4 <if test='mobile != null'> AND bp_user.mobile LIKE CONCAT('%',#{mobile},'%') </if> " +
            " <if test='startTime != null'>  AND bp_point_record.updated_at  &gt;= #{startTime} </if>" +
            " <if test='endTime != null'> AND bp_point_record.updated_at &lt;= #{endTime} </if> " +
            " </where>" +
            " </script>")
    List<ExcelPO> findExchangeGeneralByPlatform(GoodExcelDTO goodExcelDTO);
}
