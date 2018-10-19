package com.fuzamei.bonuspoint.entity.vo.asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qbanxiaoli
 * @description 会员资产数据返类
 * @create 2018/5/2 17:23
 */
@Data
public class MemberCashRecordVO {

    /** 积分总数量 */
    private BigDecimal pointNum;
    /** 通用积分兑现比例 */
    private BigDecimal cashRate;
    /** 通用积分 */
    private BigDecimal generalPoint;
    /** 商户积分总数 */
    private BigDecimal pointTotal;
    /** 用户id */
    private Long memberId;
    /** 用户手机号 */
    private String mobile;
    /** 用户地址 */
    private String address;
    /** 推荐人 */
    private String referee;

}
