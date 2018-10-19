package com.fuzamei.bonuspoint.entity.po.point;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: bonus-point-cloud
 * @description: 通用积分信息
 * @author: WangJie
 * @create: 2018-07-04 11:12
 **/
@Data
@Table(name = "bp_general_point_info")
public class GeneralPointInfoPO {
    /** 积分编号 */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    /** 所属平台id */
    private Long platformId;

}
