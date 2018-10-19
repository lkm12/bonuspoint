package com.fuzamei.bonuspoint.entity.dto.point;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description: 转积分时数据传输模型
 * @author: WangJie
 * @create: 2018-07-03 15:13
 **/
@Data
public class SendPointDTO {
    /** 转账发起人id */
    private Long fromId;
    /** 转账积分id */
    private Long pointId;
    /** 接收积分用户id */
    private Long toId;
    /** 积分数量 */
    private BigDecimal num;
    /** 备注 */
    private String memo;
    /** 交易类型**/
    private Integer category;
    /** 加减积分标志 */
    private Integer type;

    /** 支付密码*/
    private String payword;

}
