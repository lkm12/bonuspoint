package com.fuzamei.bonuspoint.entity.dto.point;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: bonus-point-cloud
 * @description: 用户持有各项积分数量和对应公司信息
 * @author: WangJie
 * @create: 2018-05-09 11:33
 **/
@Data
@NoArgsConstructor
public class PointRelationDTO {
    /** 公司编号*/
    private String companyId;
    /** 公司名称 */
    private String companyName;
    /** 公钥地址 */
    private String publicKey;
    /** 公司logo地址 */
    private String logoUrl;
    /** 积分名称 活动名称 */
    private String name;
    /** 积分持有数量 */
    private BigDecimal num;
    /** 积分兑换比率 */
    private Float pointRate;
    /** 积分状态 3->已过期    其他->可兑换*/
    private Integer status;
}
