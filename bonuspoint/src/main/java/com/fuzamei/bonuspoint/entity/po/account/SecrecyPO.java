package com.fuzamei.bonuspoint.entity.po.account;

import lombok.Data;

import javax.persistence.Table;

/**
 * @program: bonus-point-cloud
 * @description: 用户密保相关字段
 * @author: WangJie
 * @create: 2018-06-25 15:16
 **/
@Data
@Table(name = "bp_user")
public class SecrecyPO {

    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 密码 ，新密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String passwordRepeat;



}
