package com.fuzamei.bonuspoint.entity.vo.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRecordVO {
    /** 流水号 */
    private Long id;
    /** 对方用户id */
    private Long oppositeUid;
    /** 用户名称*/
    private String username;
    /** 对方公钥*/
    private String oppsitePublickey;
    /** 用户昵称*/
    private String nickname;
    /** 加减积分标志（1->加积分,2->减积分）*/
    private Integer type;
    /** 积分交易类型（1->集团申请积分,2->集团发放积分给用户,3->用户收入集团发放的积分,
     * 4->用户兑换通用积分时，扣除用户的集团积分,5->平台结算通用积分,6->用户购买集团服务,
     * 7->用户转出积分,8->他人转入积分,9->退货支出，10->退货返还'*/
    private Integer category;
    /** 积分Id */
    private Long pointId;
    /** 积分名称*/
    private String pointName;
    /** 积分备注*/
    private String pointMemo;
    /**交易备注*/
    private String memo;
    /** 是否有有效期*/
    private Boolean pointIsLife;
    /** 积分开始时间*/
    private Long pointStartAt;
    /** 积分结束时间*/
    private Long pointEndAt; 
    /** 积分兑换率 */
    private float pointRate;
    /** 积分数量 */
    private BigDecimal num;
    /** 创建时间*/
    private Long createdAt;
    /** 更新时间*/
    private Long updatedAt;
    /** 公钥*/
    private String publicKey;
    /** 头像*/
    private String headimgurl;
    /**加减积分标志 string*/
    private String typeStr;
}
