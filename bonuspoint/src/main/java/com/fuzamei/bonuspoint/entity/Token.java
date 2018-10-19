package com.fuzamei.bonuspoint.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token
 *
 * @author wangtao
 * @create 2018/5/29
 */
@Data

public class Token {

    /** 客户端类型 APP , Browser**/
    private String client;
    /** 用户ID */
    private Long uid;
    /** 随机token */
    private String tokenStr;
    /** 角色 */
    private Integer role;
    /** 平台的uid */
    private Long pId;
    private String accessToken;
    public Token(){}
    public Token(String accessToken) {
        this.accessToken = accessToken;
    }
}
