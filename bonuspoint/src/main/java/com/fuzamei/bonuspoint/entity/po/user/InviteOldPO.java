package com.fuzamei.bonuspoint.entity.po.user;

import lombok.Data;

@Data
public class InviteOldPO {

    private Long id;
    /**用户id*/
    private Long uid;
    /**上级链*/
    private String chain;
    /**父级id*/
    private Long pid;

    /**爷级id*/
    private Long gpid;
    /**邀请码*/
    private String inviteCode;
    /**创建时间*/
    private Long createdAt;
}
