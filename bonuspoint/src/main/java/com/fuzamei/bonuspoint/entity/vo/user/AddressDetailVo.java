package com.fuzamei.bonuspoint.entity.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lmm
 * @description 用户地址
 * @create 2018/9/13 11:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetailVo {
    /** 地址id*/
    private Long addressId;
    /**收货人姓名*/
    private String addressName;
    /** 收货人电话*/
    private String addressMobile;
    /** 收货地址 */
    private String addressDistrict;
    /** 收货详情*/
    private String addressDetail;

}
