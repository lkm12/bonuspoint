package com.fuzamei.bonuspoint.entity.vo.user;

import lombok.Data;

/**
 * @program: bonus-point-cloud
 * @description: 公司基本信息
 * @author: WangJie
 * @create: 2018-07-09 16:32
 **/
@Data
public class CompanyBaseInfoVO {
    /** 集团名称 */
    private String companyName;
    /** 集团详细地址 */
    private String companyAddress;
    /** 集团负责人 */
    private String companyLeader;
    /** 法人身份证号*/
    private String companyLeaderIdCard;
    /** 法人手机 */
    private String companyLeaderMobile;
    /** 集团电话 */
    private String companyTelephone;
    /** 公司邮箱 */
    private String companyEmail;
    /** 开店日期 */
    private Long createdAt;

    /** 公司logo */
    private String logoUrl;

}
