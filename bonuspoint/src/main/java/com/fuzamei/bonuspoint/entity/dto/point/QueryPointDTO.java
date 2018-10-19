/**
 * FileName: QueryDTO
 * Author: 王涛
 * Date: 2018/4/24 20:11
 * Description:
 */
package com.fuzamei.bonuspoint.entity.dto.point;

import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询类
 *
 * @author wangtao
 * @create 2018/4/24
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryPointDTO extends PageDTO {
    /** 查询发起人id*/
    private Long uid;

    /** 被查询人id*/
    private Long opId;
    /** 出入标志（1>入，2>出） */
    private Integer type;

    /** 积分交易类型（1->集团申请积分,2->集团发放积分给用户,3->用户收入集团发放的积分,4->用户兑换通用积分,5->平台结算通用积分,6->用户购买集团服务,7->用户转出积分,8->他人转入积分,9->退货支出，10->退货返还'*/
   private int category;
    /** 积分类型（1>集团商户积分，2>通用积分) */
    private Integer pointType;
    /** 手机号 */
    private String mobile;

    /** 积分发放平台 id*/
    private Long issuePlatform;

    /** 积分所属集团编号 */
    private Long company;
    /** 积分所属集团名称 */
    private Long companyName;

    /** 积分发行状态  状态(0->待审核,1->已审核,2->已拒绝,3->已过期)*/
    private Integer status;

    /** 积分名称 活动名称*/
    private String name;


    private Long createdAt;
    private Long updatedAt;
}
