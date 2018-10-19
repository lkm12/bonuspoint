package com.fuzamei.bonuspoint.entity.po.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户私密信息PO
 * @author liumeng
 * @create 2018年6月25日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrivatePo {
    /** 用户id */
    private Long id;
    /** 用户公钥 */
    private String publicKey;
    /** 用户私钥 */
    private String privateKey;

}
