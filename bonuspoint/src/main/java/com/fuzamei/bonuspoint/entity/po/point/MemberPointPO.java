package com.fuzamei.bonuspoint.entity.po.point;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fuzamei.bonuspoint.entity.po.user.UserAddressPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 18519 on 2018/5/7.
 * lkm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberPointPO {
    private Integer role;
    private List<PointPO> pointPOList;
    private List<CompanyPointPO> companyPointPOList;
    /**用户变为集团用户的时间*/
    private Long joinAt;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户手机号
     */
    private String mobile;

    /** 用户注册时间**/
    private Long createdAt;
    /**
     * 地址
     */
    private String  publicKey;
    /**
     * 拥有会员身份所属集团的积分总量
     */
    private BigDecimal totalCompanyPoint;
    /**
     * 拥有通用积分总量
     */
    private BigDecimal totalGeneralPoint;

}
