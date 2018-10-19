package com.fuzamei.bonuspoint.entity.po.asset;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/4/19
 */
@Data
@Table(name = "bp_cash_record")
public class CashRecordPO {

    /** 流水号 */
    @Id
    private Long id;
    /** 用户id */
    private Long uid;
    /** 对方用户id */
    private Long oppositeUid;
    /** 出入金标志（1->入金,2->出金）*/
    private Integer type;
    /** 出入金类型(\r\n1->集团备付金充值,\r\n2->集团支付备付金,
     * \r\n3->平台收入备付金,\r\n4->平台结算集团通用积分,
     * \r\n5->集团结算收入备付金,\r\n6->集团提现\r\n) */
    private Integer category;
    /** 金额数量 */
    private BigDecimal amount;
    /** 状态（\r\n0->未审核,\r\n1->通过审核，\r\n2->成功，\r\n3->失败\r\n） */
    private Integer status;
    /** 拒绝原因 */
    private String reason;
    /** 创建时间 */
    private String createdAt;
    /** 修改时间 */
    private String updatedAt;
    /** 区块哈希 */
    private String hash;
    /** 区块高度 */
    private Long height;

}
