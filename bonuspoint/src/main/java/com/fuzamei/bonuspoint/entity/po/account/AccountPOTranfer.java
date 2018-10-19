package com.fuzamei.bonuspoint.entity.po.account;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "bp_user")
public class AccountPOTranfer {


    /**
     * 用户id
     */
    private Long id;

    /**
     * 角色（0->超级管理,1->平台,2->集团,3->商户,4->个人用户）
     */
    private Integer role;

    /**
     * 所属父级
     */
    private Long pId;
    /**
     * 登录用户名（用户同手机号)
     */
    private String username;

    /**
     * 加密密码
     */
    private String passwordHash;

    /**
     * 加密付款密码
     */
    private String paywordHash;

    /**
     * 手机所属国家
     */
    private String country;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像图片地址
     */
    private String headimgurl;

    /**
     * 二维码地址
     */
    private String qrCode;

    /**
     * 默认收货地址id（用户）
     */
    private Long defaultAddress;

    /**
     * 用户状态(0->冻结,10->可用)
     */
    private Integer status;
    /**
     * 是否初始化(0->否,1->是)
     */
    private Boolean isInitialize;

    /**
     * 用户公钥
     */
    private String publicKey;

    /**
     * 用户私钥
     */
    private String privateKey;
    /**
     * 注册时间
     */
    private Long createdAt;
    /**
     * 修改时间
     */
    private Long updatedAt;
    /**
     * 修改交易密码时间
     */
    private Long paywordAt;
    /**
     * 登录时间
     */
    private Long loginAt;
}
