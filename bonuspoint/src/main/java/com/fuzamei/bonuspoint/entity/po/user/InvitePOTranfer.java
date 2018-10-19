package com.fuzamei.bonuspoint.entity.po.user;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "bp_relation")
public class InvitePOTranfer {

    private Long id;
    /**用户id*/
    private Long uid;
    /**上级链*/
    private String chains;
    /**父级id*/
    private Long pId;

    /**爷级id*/
    private Long gpid;
    /**邀请码*/
    private String inviteCode;
    /**创建时间*/
    private Long createdAt;


}
