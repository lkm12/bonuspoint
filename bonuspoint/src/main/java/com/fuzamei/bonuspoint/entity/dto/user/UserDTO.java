package com.fuzamei.bonuspoint.entity.dto.user;

import com.fuzamei.bonuspoint.validation.group.User;
import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @program: bonuspoint
 * @description:
 * @author: WangJie
 * @create: 2018-04-17 16:04
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO  extends PageDTO {

    private	Long	id;//用户id

    /** token的类别 (APP,网页)*/
    private String tokenType;
    /**token 的有效期*/
    private Long tokenTime;

    private	Integer	role;//角色（0->超级管理,1->平台,2->集团,3->商户,4->个人用户）

    @NotBlank(message = "{USERNAME_BLANK}",groups = {User.LoginCaptcha.class})
    private	String	username;//登录用户名（用户同手机号)

    private	String	passwordHash;//加密密码

    private	String	random;//区块链随机码

    private	String	paywordHash;//加密付款密码
    private	String	country;//手机所属国家
    private	String	mobile;//手机号码
    private	String	email;//邮箱地址
    private	String	nickname;//用户昵称（用户）

    private	String	headimgurl;//头像url地址
    private	String	qrCode; //二维码地址

    private	Long	defaultAddress; //默认收货地址id（用户）
    private	Integer	status; //用户状态(0->冻结,10->可用)
    private Boolean isInitialize; //是否初始化(0->否,1->是)

    private	Long	createdAt; //注册时间
    private	Long	updatedAt;//修改时间
    private	Long	paywordAt;//修改交易密码时间
    /** 登录时间*/
    private Long    loginAt;



    private String password;//密码

    @NotBlank(message = "{CAPTCHA_BLANK}",groups = {User.LoginCaptcha.class})
    private String code;//验证码
    private	String payword;//付款密码


    private	 Long pId; //所属父级

    private String paywordStatus;//用户交易密码设置状态

    private Long paywordTime;//交易密码设置时间

    private String mobileStatus;//手机绑定状态（0：未绑定，1：已绑定）
    private String emailStatus;//邮箱绑定状态（0：未绑定，1：已绑定）

    private String inviteCode;//邀请码


    /** 公钥*/
    private String publickey;
    /** 私钥*/
    private String privatekey;
    /**平台的uuid*/
    private String mark;



}