package com.fuzamei.bonuspoint.entity.po.point;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 积分信息表
 * @author liumeng
 * @create 2018年5月7日
 */
@Data
@Table(name = "bp_point_info")
public class PointInfoPO {

    /** 积分编号 */
    @Id
    private Long id;
    /** 积分名称 */
    private String name;
    /** 所属集团id */
    private Long company;
    /** 所属平台id */
    private Long issuePlatform;
    /** 备注 */
    private String memo;
    /** '申请积分数量 */
    private BigDecimal num;
    /** 积分已发放数量 */
    private BigDecimal numRemain;
    /** 剩余积分数量 */
    private BigDecimal numUsed;
    /** 积分状态(\r\n0->待审核,\r\n1->已审核\r\n2->已过期,\r\3->已拒绝\r\n)*/
    private Integer status;
    /** 拒绝原因 */
    private String reason;
    /** 是否永久有效 */
    private Integer isLife;
    /** 开始时间 */
    @Transient
    private String startTime;
    /** 结束时间 */
    @Transient
    private String endTime;
    /** 创建时间 */
    @Transient
    private String createTime;
    /** 更新时间 */
    @Transient
    private String updateTime;

    private Long startAt;
    private Long endAt;
    private Long createdAt;
    private Long updatedAt;


}
