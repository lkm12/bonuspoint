package com.fuzamei.bonuspoint.entity.vo.account;

import lombok.Data;

/**
 * @program: bonus-point-cloud
 * @description: 转账时需要的用户信息
 * @author: WangJie
 * @create: 2018-07-09 14:10
 **/
@Data
public class AccountVO {
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
     * 头像图片url地址
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
     * 用户公钥
     */
    private String publicKey;


}
