package com.fuzamei.bonuspoint.entity.po.data.excel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformCashRecordVOExcel {
    /** 流水号 */
    private Long id;
    /** 商户编号 */
    private Long companyId;
    /** 商户名称 */
    private String companyName;
    /** 充值时间 */
    private Long createTime;
    /** 到账时间 */
    private Long updateTime;
    /** 备用金比例 */
    private Float cashRate;
    /** 金额 */
    private BigDecimal amount;
    /** 余额比例 */
    private Float numRate;
    /** 会员积分余额 */
    private Long numRemain;
    /** 流通中会员积分 */
    private Long numOutside;
    /** 可兑换金额 */
    private BigDecimal amountExchange;
    /** 通用积分 */
    private Long generalPoint;
    /** 商户积分总数 */
    private Long pointTotal;
    /** 用户id */
    private Long memberId;
    /** 用户手机号 */
    private String mobile;
    /** 用户地址 */
    private String address;
    /** 推荐人 */
    private String referee;
    /** 充值时间 datetime*/
    private String createTimeDate;
    /** 到账时间 datetime*/
    private String updateTimeDate;
}
