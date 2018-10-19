/**
 * FileName: PointPO
 * Author:
 * Date: 2018/5/2 14:44
 * Description:
 */
package com.fuzamei.bonuspoint.entity.po.point;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * wangtao
 *
 * @author wangtao
 * @create 2018/5/2
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointPO {
    /** 流水号 */
    private Long pointId;
    /** 通用积分id */
    private Long generalPointId;
    /** 积分名称 */
    private String name;
    /** 所属集团id */
    private String company;
    /** 所属平台id */
    private Long platform;
    /** 备注 */
    private String memo;
    /** 积分数量 */
    private BigDecimal num;
    /** 积分已发放数量 */
    private BigDecimal numSend;
    /** 积分已使用数量 */
    private BigDecimal numUsed;
    /** 积分状态 */
    private Integer status;
    /** 拒绝原因 */
    private String reason;
    /** 是否永久有效 */
    private Integer isLife;
    /** 开始时间 */
    private String startTime;
    /** 结束时间 */
    private String endTime;
    /** 创建时间 */
    private Long createTime;
    /** 更新时间 */
    private Long updateTime;

    /**兑换的人民币*/
    private Long numCash;

    /**
     *
     * lkm
     *
     * MemberPoint
     */

    /** 积分id */
    private Long id;
    /** 持有积分的用户id */
    private Long userId;

    /** 接收积分的用户id */
    private Long opUserId;

    /** 剩余积分数量 */
    private BigDecimal numNew;

    /** 通用积分数量 */
    private BigDecimal generalPoint;
    /** 平台名称 */
    private String platformName;

    /** 集团名称 */
    private String companyName;
    /** 集团图片 */
    private String img;
    /** 用户公钥 */
    private String userPublickey;
    /** 用户私钥 */
    private String privatekey;
    /** 集团公钥 */
    private String groupPublickey;

    /** 平台公钥 */
    private String platformPublickey;
    /** 集团兑换通用积分比率 或  备付金/平台积分 */
    private Float pointRate;
    private String hash;
    private Long height;
    /** 集团兑换通用积分比率 或  备付金/平台积分 */
    private String pointRateStr;
    /** 平台现金兑换比例（金额：平台积分）*/
    private Float platformPointRate;

}
