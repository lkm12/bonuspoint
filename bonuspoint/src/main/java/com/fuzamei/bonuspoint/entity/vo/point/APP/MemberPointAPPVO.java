package com.fuzamei.bonuspoint.entity.vo.point.APP;

import lombok.Data;

import java.util.List;

@Data
public class MemberPointAPPVO {
    /**对方角色（2，集团 4，用户）*/
    private String role;
    /**通用积分数量*/
    private String generalPoint;
    private List<CompanyPointAPPVO> companyPointPOList;
    /**用户变为集团用户的时间*/
    private Long joinAt;
    /**集团名称*/
    private String companyName;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户手机号
     */
    private String mobile;
    /**
     * 地址
     */
    private String  publicKey;
    /**
     * 拥有会员身份所属集团的积分总量
     */
    private Long totalCompanyPoint;
    /**
     * 拥有通用积分总量
     */
    private Long totalGeneralPoint;
}
