package com.fuzamei.bonuspoint.entity.vo.user.APP;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InviteAPPVO {

    /**邀请码注册人uid*/
    private Long id;
    /**上级id*/
    private Long uid;
    /**上级链*/
    private String chains;
    /**收到的平台积分*/
    private Long platformPoint;
    /**用户手机*/
    private String mobile;
    /**推荐人数*/
    private Integer recommendNum;
    /**用户公钥*/
    private String publicKey;

    private Long pId;
}
