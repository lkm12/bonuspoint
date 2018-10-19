package com.fuzamei.bonuspoint.entity.dto.account;

import lombok.Data;

/**
 * @program: bonus-point-cloud
 * @description: 用户密保相关字段
 * @author: WangJie
 * @create: 2018-06-25 15:16
 **/
@Data
public class SecrecyDTO {


    /**用户id*/
    private Long id;
    /**新密码md5加密密码*/
    private String passwordHash;

    /**原密码md5加密密码*/
    private String oldPasswordHash;


    /**支付密码*/
    private String paywordHash;


    /**手机归属国家*/
    private String country;

    /**手机号*/
    private String mobile;

    /**邮箱*/
    private String email;
    /**更新时间*/
    private Long updatedAt;

    /**密码更新时间*/
    private Long paywordAt;

    /** 是否初始化*/
    private Boolean isInitialize;

}
