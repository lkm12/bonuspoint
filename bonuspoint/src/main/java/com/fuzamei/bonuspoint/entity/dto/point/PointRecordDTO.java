package com.fuzamei.bonuspoint.entity.dto.point;

import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-07 14:09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class PointRecordDTO extends PageDTO {

    /** 流水号*/
    private Long id;
    /** 用户id*/
    private Long uid;
    /** 对方用户id*/
    private Long oppositeUid;
    /** 加减积分标志（1->加积分,2->减积分）*/
    private Integer type;
    /** 积分交易类型（1->集团申请积分,2->集团发放积分给用户,3->用户收入集团发放的积分,4->用户兑换通用积分,5->平台结算通用积分,6->用户购买集团服务,7->用户转出积分,8->他人转入积分,9->退货支出，10->退货返还'*/
    private Integer category;
    /** 积分Id*/
    private Long pointId;
    /** 积分兑换率*/
    private Long pointRate;

    /**  用户交易订单Id*/
    private Long orderId;
    /** 交易备注信息*/
    private String memo;

    /**积分数量*/
    private BigDecimal num;

    /** 创建时间*/
    private Long createdAt;
    /** 更新时间*/
    private Long updatedAt;

    /**用户手机号*/
    private String mobile;

    /** 对方头像URL*/
    private String oppsiteImg;
    /** 对方用户名*/
    private String oppsiteName;
    /** 对方公钥*/
    private String oppsitePublickey;

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
     * 积分名称
     */
    private String name;

    /**区块高度 */
    private Long height;
    /** 区块哈希*/
    private String hash;

}
