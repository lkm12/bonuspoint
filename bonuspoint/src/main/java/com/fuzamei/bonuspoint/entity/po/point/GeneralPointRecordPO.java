package com.fuzamei.bonuspoint.entity.po.point;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 通用积分记录
 * @author liumeng
 * @create 2018年5月9日
 */
@Data
@Table(name = "bp_general_point_record")
public class GeneralPointRecordPO {

    /** 流水号 */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    /** 用户id */
    private Long uid;
    /** 对方用户id */
    private Long oppositeUid;
    /** 积分编号 */
    private Long pointId;
    /** 加减积分标志（1->加积分,2->减积分）*/
    private Integer type;
    /** 积分交易类型 */
    private Integer category;
    /** 积分数量 */
    private BigDecimal num;
    /**  用户交易订单Id */
    private Long orderId;
    /** 交易备注信息 */
    private String memo;
    /** 创建时间 */
    private Long createdAt;
    /** 更新时间 */
    private Long updatedAt;
    /** 区块高度*/
    private Long height;
    /** 区块hash*/
    private String hash;

    /** 平台现金兑换比例（金额：平台积分）*/
    private Float platformPointRate;

}
