package com.fuzamei.bonuspoint.dao.common.mapper;

import com.fuzamei.bonuspoint.entity.dto.point.PointInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;
import com.fuzamei.bonuspoint.entity.vo.point.CompanyPointAssetsVO;
import com.fuzamei.bonuspoint.sql.point.PointInfoSqlFactory;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * pointInfo Dap
 *
 * @author liumeng
 * @create 2018年5月7日
 */
@Mapper
@Repository
public interface PointInfoMapper extends TkMapper<PointInfoPO> {

    /**
     * 添加积分信息
     *
     * @param pointInfoPO 积分信息
     * @return
     */
    @InsertProvider(type = PointInfoSqlFactory.class, method = "savePointInfo")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int savePointInfo(PointInfoPO pointInfoPO);

    /**
     * 获取集团资产信息
     *
     * @param companyId
     * @return
     */
    @SelectProvider(type = PointInfoSqlFactory.class, method = "selectCompanyPointAsset")
    CompanyPointAssetsVO selectCompanyPointAsset(Long companyId);

    /**
     * 平台查看积分审批列表，集团查看积分发行记录
     */
    @SelectProvider(type = PointInfoSqlFactory.class, method = "pointIssueList")
    List<PointInfoDTO> pointIssueList(QueryPointDTO queryPointDTO);

    /**
     * 用户使用积分是更新积分使用量
     *
     * @param id   积分id
     * @param used 使用量
     * @return
     */
    @Update("update bp_point_info set  num_used  = num_used + #{used} where id = #{id}")
    int addNumUsed(@Param("id") Long id, @Param("used") BigDecimal used);

    /**
     * 将过期积分状态置为过期
     *
     * @return
     * @author wangjie
     */
    @Update("update bp_point_info set `status`=3 where end_at >= NOW() and is_life =1")
    int checkAndHandelExpiredPoint();

    /**
     * 查找过期而未处理的积分
     * @return
     */
    @Select("select id , name ,company , issue_platform  from bp_point_info where status =1 and end_at < #{nowTime} and end_at!=0")
    List<PointInfoPO> listExpiredPoint(long nowTime);

    /**
     * 将过期而未处理的积分设置为过期
     * @param id
     * @return
     */
    @Update("update bp_point_info set `status`=3 where id =#{id} ")
    int handelExpiredPointById(Long id);

    /**
     * 获取集团积分流通量
     * @param companyId 集团id
     * @return 流通量(可能为NULL ,表示0)
     */
    @Select("SELECT SUM(num-num_used) as nums FROM  bp_point_info  WHERE company=#{companyId} AND `status` = 1 GROUP BY  company")
    BigDecimal companyPointLiquidity(Long companyId);

    /**
     * 获取集团待审核积分数量
     * @param companyId 集团id
     * @return  待审核积分数量（为NULL ,表示 0）
     */
    @Select("SELECT SUM(num) as nums FROM  bp_point_info  WHERE company=#{companyId} AND `status` = 0 GROUP BY  company")
    BigDecimal companyPointToCheck(Long companyId);



}
