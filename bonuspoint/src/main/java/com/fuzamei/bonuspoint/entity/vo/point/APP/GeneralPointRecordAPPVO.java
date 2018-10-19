package com.fuzamei.bonuspoint.entity.vo.point.APP;

import lombok.Data;

@Data
public class GeneralPointRecordAPPVO {
    /** 流水号 */
    private String id;
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
    private String num;
    /**  用户交易订单Id */
    private Long orderId;
    /** 交易备注信息 */
    private String memo;
    /** 创建时间 */
    private String createdAt;
    /** 更新时间 */
    private Long updatedAt;
    /** 区块高度*/
    private String height;
    /** 区块hash*/
    private String hash;
    /** 积分类型 string*/
    private String typeStr;
    /** 公钥*/
    private String publicKey;
    /** 头像*/
    private String headimgurl;
}
