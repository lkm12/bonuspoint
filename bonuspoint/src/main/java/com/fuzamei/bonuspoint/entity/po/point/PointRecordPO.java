package com.fuzamei.bonuspoint.entity.po.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 积分操作记录类
 * @author liumeng
 * @create 2018年5月7日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bp_point_record")
public class PointRecordPO {
    /** 流水号 */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    /** 用户id */
    private Long uid;
    /** 对方用户id */
    private Long oppositeUid;
    /** 加减积分标志（1->加积分,2->减积分）*/
    private Integer type;
    /** 积分交易类型（1->集团申请积分,2->集团发放积分给用户,3->用户收入集团发放的积分,
     * 4->用户兑换通用积分时，扣除用户的集团积分,5->平台结算通用积分,6->用户购买集团服务,
     * 7->用户转出积分,8->他人转入积分,9->退货支出，10->退货返还'*/
    private Integer category;
    /** 积分Id */
    private Long pointId;
    /** 积分兑换率 */
    private Float pointRate;
    /** 积分数量 */
    private BigDecimal num;
    /**  用户交易订单Id */
    private Long orderId;
    /** 交易备注信息 */
    private String memo;
    /** 创建时间*/
    private Long createdAt;
    /** 更新时间*/
    private Long updatedAt;
    /** 区块高度*/
    private Long height;
    /** 区块hash*/
    private String hash;
    /** 平台现金兑换比例（金额：平台积分）*/
    private Float platformPointRate;

}
