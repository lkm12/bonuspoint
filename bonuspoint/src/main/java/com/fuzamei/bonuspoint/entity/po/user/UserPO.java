package com.fuzamei.bonuspoint.entity.po.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: bonuspoint
 * @description:
 * @author: WangJie
 * @create: 2018-04-17 11:16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPO {

    /**用户id*/
    private Long id;
    /**邀请码*/
    private String inviteCode;
    /**角色（0->超级管理,1->平台,2->集团,3->商户,4->个人用户）*/
    private Integer role;
    /**是否初始化*/
    private Integer isInitialize;
    /**所属父级*/
    private Long pId;
    /**登录用户名（用户同手机号)*/
    private String username;
    /**随机数*/
    private String random;

    /**加密密码*/
    private String passwordHash;

    /**加密付款密码*/
    private String paywordHash;

    /**手机所属国家*/
    private String country;

    /**手机号码*/
    private String mobile;

    /**邮箱地址 */
    private String email;

    /**用户昵称 */
    private String nickname;

    /**头像图片url地址 */
    private String headimgurl;

    /**二维码地址*/
    private String qrCode;

    /**默认收货地址id（用户）*/
    private Long defaultAddress;

    /**用户状态(0->冻结,10->可用)*/
    private Integer status;

    /**注册时间 */
    private Long createdAt;
    /** 修改时间*/
    private Long updatedAt;
    /**修改交易密码时间*/
    private Long paywordAt;
    /**登录时间*/
    private Long loginAt;
    /** 收货地址*/
    private String areaDetail;

    private Long uid;
    private String companyName;
    private String companyAddress;
    private String publickey;


    private String name;
    private String address;
    /**
     * 注册
     */
    private Long userId;
    /**
     * 联系人
     */
    private Long contactsId;
    private String remark;
    private Long createdTime;

    /**平台名称*/
    private String platformName;
    private String payword;
    /**街道名称*/
    private String streetName;
    /**区域名称*/
    private String districtName;
    /**城市名称*/
    private String cityName;
    /**省或直辖市名称*/
    private String provinceName;

}
