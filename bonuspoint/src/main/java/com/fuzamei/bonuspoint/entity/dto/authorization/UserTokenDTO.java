package com.fuzamei.bonuspoint.entity.dto.authorization;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-08 10:28
 **/
@Data
@NoArgsConstructor
public class UserTokenDTO {
    /** 编号*/
    private Long id;
    /** token */
    private String token;
    /** 用户id */
    private Long uid;
    /** 上次添加或修改时的时间*/
    private Long time;

    /** 新的token */
    private String newToken;
}
