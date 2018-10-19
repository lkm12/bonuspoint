package com.fuzamei.bonuspoint.entity.dto.asset;
import lombok.Data;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/2 16:19
 */
@Data
public class PlatformCashRecordDTO {

    /** 流水号 */
    private Long id;
    /** 用户id */
    private Long uid;
//
//    /** 对方用户id */
//    @ApiModelProperty(value = "用户id", required = true)
//    private Long oppositeUid;
//
    /** 出入金标志（1->入金,2->出金）*/
    private Integer type;
//
//    /** 出入金类型(\r\n1->集团备付金充值,\r\n2->集团支付备付金,
//     * \r\n3->平台收入备付金,\r\n4->平台结算集团通用积分,
//     * \r\n5->集团结算收入备付金,\r\n6->集团提现\r\n) */
//    @ApiModelProperty(value = "出入金类型")
//    private Integer category;
//
    /** 金额 */
    private Double amount;
    /** 状态（\r\n0->未审核,\r\n1->审核通过，\r\n2->审核未通过，\r\n3->成功，\r\n4->失败\r\n） */
    private Integer status;
    /** 拒绝原因 */
    private String reason;
    /** 当前页 */
    private Integer currentPage;
    /** 每页记录数 */
    private Integer pageSize;
    /** 查询开始时间 */
    private Long startTime;
    /** 查询结束时间 */
    private Long endTime;
//
//    /** 交易密码 */
//    @ApiModelProperty(value = "交易密码", required = true)
//    private String payword;
    /** 会员手机号码 */
    private String mobile;
    /** 商户名称 */
    private String companyName;

}
