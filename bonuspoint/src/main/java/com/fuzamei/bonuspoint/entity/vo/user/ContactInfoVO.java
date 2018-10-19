package com.fuzamei.bonuspoint.entity.vo.user;

import lombok.Data;

/**
 * lkm
 */

@Data
public class ContactInfoVO {
    /**联系人表id*/
    private String id;
    /**联系人用户名*/
    private String username;
    /**联系人手机号*/
    private String mobile;
    /**联系人公钥*/
    private String publickey;
    /*备注*/
    private String remark;
    /*创建时间*/
    private String createdTime;
}
