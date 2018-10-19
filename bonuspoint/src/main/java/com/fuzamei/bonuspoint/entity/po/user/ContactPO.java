package com.fuzamei.bonuspoint.entity.po.user;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "bp_contacts")
public class ContactPO {
    private Long id;

    private Long uid;
    private Long opUid;
    private String mobile;
    private String publickey;
    private String remark;
    private Long createdAt;
    private Long updatedAt;

}
