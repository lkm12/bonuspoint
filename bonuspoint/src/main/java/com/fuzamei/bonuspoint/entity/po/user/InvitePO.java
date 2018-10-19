package com.fuzamei.bonuspoint.entity.po.user;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "bp_relation")
public class InvitePO {
    /**邀请码注册人uid*/
    @Id
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
