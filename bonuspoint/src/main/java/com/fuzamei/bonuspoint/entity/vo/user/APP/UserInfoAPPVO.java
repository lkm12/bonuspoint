package com.fuzamei.bonuspoint.entity.vo.user.APP;

import lombok.Data;

@Data
public class UserInfoAPPVO {

    /**用户id*/
    private String id;
    /**邀请码*/
    private String inviteCode;
    /**角色（0->超级管理,1->平台,2->集团,3->商户,4->个人用户）*/
    private String role;
    /**是否初始化*/
    private String isInitialize;
    /**所属父级*/
    private String pId;
    /**登录用户名（用户同手机号)*/
    private String username;
    /**随机数*/
    private String random;

    /**加密密码*/
    private String passwordHash;

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
    private String defaultAddress;

    /**用户状态(0->冻结,10->可用)*/
    private String status;

    /**注册时间 */
    private String createdAt;
    /** 修改时间*/
    private String updatedAt;
    /**修改交易密码时间*/
    private String paywordAt;
    /**登录时间*/
    private String loginAt;
    /** 收货地址*/
    private String areaDetail;

    private String uid;
    private String companyName;
    private String companyAddress;
    private String publickey;

    /** 二维码*/
    private String name;
    /**
     * 注册
     */
    private Long userId;
    /**
     * 联系人
     */
    private String contactsId;
    private String remark;
    private String createdTime;

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
    /**二维码的存储路径*/
    private String filePath;
}
