package com.fuzamei.bonuspoint.entity.po.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: bonus-point-cloud
 * @description: 用户收货地址
 * @author: WangJie
 * @create: 2018-04-20 15:15
 **/
@Log
@Data
@Table(name = "bp_user_address")

@NoArgsConstructor
public class UserAddressPO {
    /** 地址id*/
    @Id
    private Long id;
    /** 用户id*/
    private Long uid;
    /** 收件人*/
    private String receiver;
    /** mobile*/
    private String mobile;
    /** 区县编码 */
    private Long districtCode;
    /** 详细地址*/
    private String areaDetail;
    /** 创建时间*/
    private Long createdAt;
    /** 修改时间 */
    private Long updatedAt;


}
