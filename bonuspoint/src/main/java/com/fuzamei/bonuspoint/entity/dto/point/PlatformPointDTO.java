/**
 * FileName: PlatformDTO
 * Author: wangtao
 * Date: 2018/4/27 15:17
 * Description:
 */
package com.fuzamei.bonuspoint.entity.dto.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 *
 *
 * @author wangtao
 * @create 2018/4/27
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformPointDTO {
    /** 积分 */
    @NotBlank
    private Long pointId;
    /**平台积分*/
    private Long generalPointId;
    /** 集团id */
    private String groupId;
    /** 原因 */
    private String reason;
    /**平台id*/
    private Long platformId;
    /**平台uid*/
    private Long uid;
    /**积分id*/
    private Long id;
    /**状态*/
    private Integer status;
    /**申请积分数量*/
    private BigDecimal num;
    /**备注*/
    private String  memo;
    /**创建时间*/
    private Long createdAt;
    /**修改时间*/
    private Long updatedAt;
    /** 是否有有效期 0->否， 1->是*/
    private Integer isLife;
    /**积分名称*/
    private String name;
    /**剩余积分数量*/
    private Long numRemain;
    /**已兑换积分数量*/
    private BigDecimal numUsed;
    /**开始时间*/
    private Long startAt;
    /**结束时间*/
    private Long endAt;


}
