package com.fuzamei.bonuspoint.entity.dto.user;

import lombok.Data;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-06-25 13:20
 **/
@Data
public class KeyDTO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户公钥
     */
    private String publicKey;
    /**
     * 用户私钥
     */
    private String privateKey;
}
