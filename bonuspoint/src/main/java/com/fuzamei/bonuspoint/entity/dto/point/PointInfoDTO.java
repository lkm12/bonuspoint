package com.fuzamei.bonuspoint.entity.dto.point;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-08 19:14
 **/
@Data
@NoArgsConstructor
public class PointInfoDTO {

    /**
     * 积分编号
     */
    private Long id;
    /**
     * 积分名称
     */
    private String name;
    /**
     * 所属集团id
     */
    private String company;

    /**
     * 集团名称
     */
    private String companyName;
    /**
     * 所属平台id
     */
    private String issuePlatform;
    /**
     * 备注
     */
    private String memo;
    /**
     * '申请积分数量
     */
    private BigDecimal num;
    /**
     * 积分已发放数量
     */
    private BigDecimal numRemain;
    /**
     * 剩余积分数量
     */
    private BigDecimal numUsed;
    /**
     * 积分状态(0->待审核,1->已审核,2->已过期,3->已拒绝)
     */
    private Integer status;
    /**
     * 拒绝原因
     */
    private String reason;
    /**
     * 是否永久有效
     */
    private Integer isLife;
    /**
     * 开始时间
     */
    private String startAt;
    /**
     * 结束时间
     */
    private String endAt;
    /**
     * 创建时间
     */
    private String createdAt;
    /**
     * 更新时间
     */
    private String updatedAt;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户地址    会员用户默认收货地址   集团用户公司地址
     */
    private String address;


}
