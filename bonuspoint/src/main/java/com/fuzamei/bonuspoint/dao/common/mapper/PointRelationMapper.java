package com.fuzamei.bonuspoint.dao.common.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.*;
import com.fuzamei.bonuspoint.entity.dto.point.PointRelationDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.po.point.PointRelationPO;
import com.fuzamei.bonuspoint.entity.vo.point.PointOrderVO;
import com.fuzamei.bonuspoint.sql.point.PointRelationSqlFactory;
import org.springframework.stereotype.Repository;


/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/9 10:29
 */
@Mapper
@Repository
public interface PointRelationMapper extends TkMapper<PointRelationPO> {

    /**
     * 查询用户积分可用总数
     *
     * @param uid       uid
     * @param companyId 集团id
     * @return
     */
    @SelectProvider(type = PointRelationSqlFactory.class, method = "queryPointTotal")
    BigDecimal queryPointTotal(Long uid, Long companyId);

    /**
     * 查询用户在冒个集团所有可用积分（按过期时间顺序排序）
     *
     * @param uid
     * @param companyId
     * @return
     */
    @SelectProvider(type = PointRelationSqlFactory.class, method = "listPointOrders")
    List<PointOrderVO> listPointOrders(Long uid, Long companyId);

    /**
     * 用户消费积分
     *
     * @param id  关系id
     * @param num 消费量
     * @return
     */
    @Update("update  bp_point_relation set num = num - #{num} where id = #{id}")
    int spendPoint(@Param("id") Long id, @Param("num") BigDecimal num);


    /**
     * 用户查看持有集团积分记录
     *
     * @param queryPointDTO
     * @return
     * @author wangjie
     */
    @SelectProvider(type = PointRelationSqlFactory.class, method = "queryCompanyPointRelation")
    List<PointRelationDTO> queryCompanyPointRelation(QueryPointDTO queryPointDTO);

}
