package com.fuzamei.bonuspoint.dao.common.mapper;

import java.util.List;

import com.fuzamei.bonuspoint.entity.dto.point.PointRecordDTO;
import com.fuzamei.common.mapper.TkMapper;
import com.fuzamei.bonuspoint.entity.po.point.PointRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointSendDTO;
import com.fuzamei.bonuspoint.entity.vo.point.PointRecordVO;
import com.fuzamei.bonuspoint.sql.point.PointRecordSqlFactory;
import org.springframework.stereotype.Repository;

/**
 * bp_point_record Dao
 *
 * @author liumeng
 * @create 2018年5月7日
 */
@Mapper
@Repository
public interface PointRecordMapper extends TkMapper<PointRecordPO> {

    /**
     * 查询集团发放积分数目
     *
     * @param queryPointSendDTO 查询条件
     * @return
     */
    @SelectProvider(type = PointRecordSqlFactory.class, method = "querySendPoint")
    List<PointRecordVO> querySendPoint(QueryPointSendDTO queryPointSendDTO);
    @SelectProvider(type = PointRecordSqlFactory.class, method = "queryPointRecordList")
    List<PointRecordDTO> queryPointRecordList(PointRecordDTO pointRecordDTO);
}
